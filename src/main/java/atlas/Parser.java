package atlas;

import java.io.IOException;
import java.time.format.DateTimeParseException;

public class Parser {
    public static boolean parse(String input, TaskList tasks, Ui ui, Storage storage) throws AtlasException {
        if (input == null) {
            return false;
        }
        input = input.trim();
        if (input.isEmpty()) {
            return false;
        }

        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];

        switch (cmd) {
        case "bye":
            ui.showBye();
            return true;

        case "list":
            ui.show(tasks.formatList());
            return false;

        case "mark": {
            int idx = parseIndex(parts, tasks.size(), "mark");
            tasks.mark(idx);
            ui.show("Nice! I've marked this task as done:\n" + tasks.get(idx));
            persist(storage, tasks, ui);
            return false;
        }

        case "unmark": {
            int idx = parseIndex(parts, tasks.size(), "unmark");
            tasks.unmark(idx);
            ui.show("OK, I've marked this tasks as not done yet :\n" + tasks.get(idx));
            persist(storage, tasks, ui);
            return false;
        }

        case "delete": {
            int idx = parseIndex(parts, tasks.size(), "delete");
            Task removed = tasks.remove(idx);
            ui.show("Noted. I've removed this task:\n" + removed
                    + "\nNow you have " + tasks.size() + " tasks in the list. ");
            persist(storage, tasks, ui);
            return false;
        }

        case "todo": {
            String desc = requireArg(parts,
                    "The description of a todo cannot be empty.\n Try: todo borrow book");
            tasks.add(new Todo(desc));
            ui.show("Got it. I've added this task:\n " + tasks.get(tasks.size() - 1)
                    + "\nNow you have " + tasks.size() + " tasks in the list. ");
            persist(storage, tasks, ui);
            return false;
        }

        case "deadline": {
            String rest = requireArg(parts, "Usage: deadline <desc> /by <yyyy-MM-dd>");
            int at = rest.indexOf(" /by ");
            if (at < 0) {
                throw new AtlasException("Missing '/by'. Try: deadline return book /by 2019-10-15");
            }
            String desc = rest.substring(0, at).trim();
            String by = rest.substring(at + 5).trim();
            if (desc.isEmpty() || by.isEmpty()) {
                throw new AtlasException("Description and ISO date (yyyy-MM-dd) must be provided.");
            }

            try {
                tasks.add(new Deadline(desc, by));
            } catch (DateTimeParseException e) {
                throw new AtlasException("Invalid date. Use yyyy-MM-dd (e.g. 2019-10-15).");
            }
            ui.show("Got it. I've added this task:\n " + tasks.get(tasks.size() - 1)
                    + "\nNow you have " + tasks.size() + " tasks in the list. ");
            persist(storage, tasks, ui);
            return false;
        }

        case "event": {
            String rest = requireArg(parts, "Usage: event <desc> /from <start> /to <end>");
            int fromAt = rest.indexOf(" /from ");
            int toAt = rest.indexOf(" /to ");
            if (fromAt < 0 || toAt < 0 || toAt <= fromAt)
                throw new AtlasException("Missing '/from' or '/to'. Try: event meeting /from Mon 2pm /to 4pm");
            String desc = rest.substring(0, fromAt).trim();
            String from = rest.substring(fromAt + 7, toAt).trim();
            String to = rest.substring(toAt + 5).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty())
                throw new AtlasException("Description, '/from', and '/to' must be provided.");
            tasks.add(new Event(desc, from, to));
            ui.show("Got it. I've added this task:\n " + tasks.get(tasks.size() - 1)
                    + "\nNow you have " + tasks.size() + " tasks in the list.");
            persist(storage, tasks, ui);
            return false;
        }


        default:
            throw new AtlasException("I don't recognise that command: '" + cmd + "'.");
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
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new AtlasException(errorMessage);
            }
            return parts[1].trim();
        }

        private static int parseIndex(String[] parts, int size, String command) throws AtlasException {
            String usage = "Usage: " + command + " <task number>";
            if (parts.length < 2) {
                throw new AtlasException(usage);
            }

            String token = parts[1].trim();
            int n;
            try {
                n = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new AtlasException("atlas.Task number must be a positive integer. " + usage);
            }
            if (n < 1 || n > size) {
                throw new AtlasException("atlas.Task " + token + " is out of range (1.." + size + ").");
            }
            return n - 1;
        }

}
