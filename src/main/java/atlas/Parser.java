package atlas;

import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Parses user input lines and performs the requested actions by
 * mutating the TaskList, talking to Storage, and using Ui
 * to show messages.
 */
public class Parser {
    
    // Named constants for magic numbers and string literals
    private static final int COMMAND_INDEX = 0;
    private static final int ARGUMENT_INDEX = 1;
    private static final int USER_DISPLAY_OFFSET = 1; // Convert 0-based to 1-based indexing
    
    private static final int BYE_LENGTH = 5; // "/by ".length()
    private static final int FROM_LENGTH = 7; // " /from ".length()
    private static final int TO_LENGTH = 5;   // " /to ".length()
    
    // Error message constants
    private static final String TASK_NUMBER_ERROR = "Task number must be a positive integer.";
    private static final String TASK_OUT_OF_RANGE_ERROR = "Task %s is out of range (1..%d).";
    private static final String UNKNOWN_COMMAND_ERROR = "I don't recognise that command: '%s'.";
    private static final String MISSING_BY_ERROR = "Missing '/by'. Try: deadline return book /by 2019-10-15";
    private static final String MISSING_FROM_TO_ERROR = "Missing '/from' or '/to'. Try: event meeting /from Mon 2pm /to 4pm";
    private static final String INVALID_DATE_ERROR = "Invalid date. Use yyyy-MM-dd (e.g. 2019-10-15).";
    private static final String DESCRIPTION_REQUIRED_ERROR = "Description and ISO date (yyyy-MM-dd) must be provided.";
    private static final String EVENT_DESCRIPTION_REQUIRED_ERROR = "Description, '/from', and '/to' must be provided.";
    private static final String TODO_DESCRIPTION_ERROR = "The description of a todo cannot be empty.\n Try: todo borrow book";
    private static final String DUPLICATE_TASK_ERROR = "This task already exists in your list!";

    /**
     * Parses a single user input line and executes the command.
     *
     * @param input   raw user input
     * @param tasks   task list to operate on
     * @param ui      UI for showing messages to users
     * @param storage storage for persisting changes
     * @return true if the user asked to exit (i.e. bye)
     * @throws AtlasException if the input is invalid or arguments are missing
     */
    public static boolean parse(String input, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        
        String[] parts = input.trim().split("\\s+", 2);
        String cmd = parts[COMMAND_INDEX];
        
        return switch (cmd) {
            case "bye" -> handleBye(ui);
            case "list" -> handleList(tasks, ui);
            case "mark" -> handleMark(parts, tasks, ui, storage);
            case "unmark" -> handleUnmark(parts, tasks, ui, storage);
            case "delete" -> handleDelete(parts, tasks, ui, storage);
            case "todo" -> handleTodo(parts, tasks, ui, storage);
            case "deadline" -> handleDeadline(parts, tasks, ui, storage);
            case "event" -> handleEvent(parts, tasks, ui, storage);
            case "find" -> handleFind(parts, tasks, ui);
            default -> throw new AtlasException(String.format(UNKNOWN_COMMAND_ERROR, cmd));
        };
    }
    
    private static boolean handleBye(Ui ui) {
        ui.showBye();
        return true;
    }
    
    private static boolean handleList(TaskList tasks, Ui ui) {
        ui.show(tasks.formatList());
        return false;
    }
    
    private static boolean handleMark(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        int idx = parseIndex(parts, tasks.size(), "mark");
        tasks.mark(idx);
        ui.show("Nice! I've marked this task as done:\n " + tasks.get(idx));
        persist(storage, tasks, ui);
        return false;
    }
    
    private static boolean handleUnmark(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        int idx = parseIndex(parts, tasks.size(), "unmark");
        tasks.unmark(idx);
        ui.show("OK, I've marked this task as not done yet:\n " + tasks.get(idx));
        persist(storage, tasks, ui);
        return false;
    }
    
    private static boolean handleDelete(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        int idx = parseIndex(parts, tasks.size(), "delete");
        Task removed = tasks.remove(idx);
        showTaskRemoved(removed, tasks, ui, storage);
        return false;
    }
    
    private static boolean handleTodo(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        String desc = requireArg(parts, TODO_DESCRIPTION_ERROR);
        Todo newTodo = new Todo(desc);
        if (tasks.contains(newTodo)) {
            throw new AtlasException(DUPLICATE_TASK_ERROR);
        }
        tasks.add(newTodo);
        showTaskAdded(tasks, ui, storage);
        return false;
    }
    
    private static boolean handleDeadline(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        String rest = requireArg(parts, "Usage: deadline <desc> /by <yyyy-MM-dd>");
        DeadlineParts deadlineParts = extractDeadlineParts(rest);
        validateDeadlineParts(deadlineParts);
        
        try {
            Deadline newDeadline = new Deadline(deadlineParts.description, deadlineParts.by);
            if (tasks.contains(newDeadline)) {
                throw new AtlasException(DUPLICATE_TASK_ERROR);
            }
            tasks.add(newDeadline);
        } catch (DateTimeParseException e) {
            throw new AtlasException(INVALID_DATE_ERROR);
        }
        showTaskAdded(tasks, ui, storage);
        return false;
    }
    
    private static boolean handleEvent(String[] parts, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        String rest = requireArg(parts, "Usage: event <desc> /from <start> /to <end>");
        EventParts eventParts = extractEventParts(rest);
        validateEventParts(eventParts);
        
        Event newEvent = new Event(eventParts.description, eventParts.from, eventParts.to);
        if (tasks.contains(newEvent)) {
            throw new AtlasException(DUPLICATE_TASK_ERROR);
        }
        tasks.add(newEvent);
        showTaskAdded(tasks, ui, storage);
        return false;
    }
    
    private static boolean handleFind(String[] parts, TaskList tasks, Ui ui) throws AtlasException {
        String keyword = requireArg(parts, "Usage: find <keyword>");
        ui.show(tasks.formatMatches(keyword));
        return false;
    }
    
    // Helper methods for extracting and validating command parts
    private static DeadlineParts extractDeadlineParts(String rest) {
        int at = rest.indexOf(" /by ");
        return new DeadlineParts(at, rest);
    }
    
    private static void validateDeadlineParts(DeadlineParts parts) throws AtlasException {
        if (parts.at < 0) {
            throw new AtlasException(MISSING_BY_ERROR);
        }
        
        String desc = parts.rest.substring(0, parts.at).trim();
        String by = parts.rest.substring(parts.at + BYE_LENGTH).trim();
        
        if (desc.isEmpty() || by.isEmpty()) {
            throw new AtlasException(DESCRIPTION_REQUIRED_ERROR);
        }
        
        parts.description = desc;
        parts.by = by;
    }
    
    private static EventParts extractEventParts(String rest) {
        int fromAt = rest.indexOf(" /from ");
        int toAt = rest.indexOf(" /to ");
        return new EventParts(fromAt, toAt, rest);
    }
    
    private static void validateEventParts(EventParts parts) throws AtlasException {
        if (parts.fromAt < 0 || parts.toAt < 0 || parts.toAt <= parts.fromAt) {
            throw new AtlasException(MISSING_FROM_TO_ERROR);
        }
        
        String desc = parts.rest.substring(0, parts.fromAt).trim();
        String from = parts.rest.substring(parts.fromAt + FROM_LENGTH, parts.toAt).trim();
        String to = parts.rest.substring(parts.toAt + TO_LENGTH).trim();
        
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new AtlasException(EVENT_DESCRIPTION_REQUIRED_ERROR);
        }
        
        parts.description = desc;
        parts.from = from;
        parts.to = to;
    }
    
    // Common utility methods to eliminate duplication
    private static void showTaskAdded(TaskList tasks, Ui ui, Storage storage) {
        Task lastTask = tasks.get(tasks.size() - 1);
        String message = String.format("Got it. I've added this task:\n %s\nNow you have %d tasks in the list.",
                lastTask, tasks.size());
        ui.show(message);
        persist(storage, tasks, ui);
    }
    
    private static void showTaskRemoved(Task removed, TaskList tasks, Ui ui, Storage storage) {
        String message = String.format("Noted. I've removed this task:\n %s\nNow you have %d tasks in the list.",
                removed, tasks.size());
        ui.show(message);
        persist(storage, tasks, ui);
    }
    
    // Helper classes for command parts
    private static class DeadlineParts {
        final int at;
        final String rest;
        String description;
        String by;
        
        DeadlineParts(int at, String rest) {
            this.at = at;
            this.rest = rest;
        }
    }
    
    private static class EventParts {
        final int fromAt, toAt;
        final String rest;
        String description, from, to;
        
        EventParts(int fromAt, int toAt, String rest) {
            this.fromAt = fromAt;
            this.toAt = toAt;
            this.rest = rest;
        }
    }

    private static void persist(Storage storage, TaskList tasks, Ui ui) {
        try {
            storage.save(tasks.asList());
        } catch (IOException e) {
            ui.show("Warning: couldn't save tasks to disk.");
        }
    }

    private static String requireArg(String[] parts, String errorMessage) throws AtlasException {
        if (parts.length < 2 || parts[ARGUMENT_INDEX].trim().isEmpty()) {
            throw new AtlasException(errorMessage);
        }
        return parts[ARGUMENT_INDEX].trim();
    }

    private static int parseIndex(String[] parts, int size, String command) throws AtlasException {
        String usage = "Usage: " + command + " <task number>";
        if (parts.length < 2) {
            throw new AtlasException(usage);
        }

        String token = parts[ARGUMENT_INDEX].trim();
        int n;
        try {
            n = Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new AtlasException(TASK_NUMBER_ERROR + " " + usage);
        }
        if (n < 1 || n > size) {
            throw new AtlasException(String.format(TASK_OUT_OF_RANGE_ERROR, token, size));
        }
        return n - USER_DISPLAY_OFFSET;
    }
}
