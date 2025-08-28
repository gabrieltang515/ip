package atlas;

import java.util.Scanner;

/**
 * Runs the Atlas chatbot application.
 * <p>
 * This class wires together the UI, storage, and task list, and coordinates
 * the application's lifecycle.
 */
public class Atlas {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage("data/duke.txt");
    private TaskList tasks;

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
        new Atlas().run();
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
}
