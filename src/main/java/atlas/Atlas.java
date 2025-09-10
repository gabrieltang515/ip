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
//        new Atlas().run();
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

    public String getResponse(String input) {
        // Reset command type for this turn
        this.commandType = null;

        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Infer command type for dialog styling (purely for GUI cosmetics)
        String trimmed = input.trim();
        String[] parts = trimmed.split("\\s+", 2);
        String cmd = parts[0];
        switch (cmd) {
        case "todo":
        case "deadline":
        case "event":
            this.commandType = "AddCommand";
            break;
        case "mark":
        case "unmark":
            this.commandType = "ChangeMarkCommand";
            break;
        case "delete":
            this.commandType = "DeleteCommand";
            break;
        default:

        }

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

    public String getCommandType() {
        return commandType;
    }
}
