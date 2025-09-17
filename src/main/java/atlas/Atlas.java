package atlas;

import java.util.Scanner;

/**
 * Runs the Atlas chatbot application.
 * This class wires together the UI, storage, and task list, and coordinates
 * the application's lifecycle.
 */
public class Atlas {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage("data/duke.txt");
    private TaskList tasks;
    private String commandType;

    /**
     * Constructs an {@code Atlas} instance.
     * Initializes the UI and storage, then attempts to load previously saved
     * tasks. If loading fails, the application starts with an empty task list.
     */
    public Atlas() {
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.show("Note: couldn't load saved tasks. Starting fresh.");
            tasks = new TaskList();
        }
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Atlas");
    }

    // Private helper: runs the input loop and delegates to Parser.
    private void run() {
        ui.showGreeting();
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String line = in.nextLine();
                try {
                    boolean quit = Parser.parse(line, tasks, ui, storage);
                    if (quit) {
                        return;
                    }
                } catch (AtlasException e) {
                    ui.showError(e.getMessage());
                }
            }
        }
    }

    // Command type constants for dialog styling
    private static final String ADD_COMMAND = "AddCommand";
    private static final String CHANGE_MARK_COMMAND = "ChangeMarkCommand";
    private static final String DELETE_COMMAND = "DeleteCommand";
    
    // Command constants
    private static final String TODO_CMD = "todo";
    private static final String DEADLINE_CMD = "deadline";
    private static final String EVENT_CMD = "event";
    private static final String MARK_CMD = "mark";
    private static final String UNMARK_CMD = "unmark";
    private static final String DELETE_CMD = "delete";

    public String getResponse(String input) {
        resetCommandType();

        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String trimmed = input.trim();
        inferCommandType(trimmed);

        try {
            boolean quit = Parser.parse(trimmed, tasks, ui, storage);
            if (quit) {
                this.commandType = null; // neutral styling on exit
            }
            return ui.getLast();
        } catch (AtlasException e) {
            ui.showError(e.getMessage());
            return ui.getLast();
        }
    }
    
    private void resetCommandType() {
        this.commandType = null;
    }
    
    private void inferCommandType(String input) {
        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];
        
        this.commandType = switch (cmd) {
            case TODO_CMD, DEADLINE_CMD, EVENT_CMD -> ADD_COMMAND;
            case MARK_CMD, UNMARK_CMD -> CHANGE_MARK_COMMAND;
            case DELETE_CMD -> DELETE_COMMAND;
            default -> null;
        };
    }

    public String getCommandType() {
        return commandType;
    }
}
