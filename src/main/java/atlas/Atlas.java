package atlas;

import java.util.Scanner;

public class Atlas {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage("data/duke.txt");
    private TaskList tasks;

    public Atlas() {
        try {
            tasks = new TaskList(storage.load());
        } catch (Exception e) {
            ui.show("Note: couldn't load saved tasks. Starting fresh.");
            tasks = new TaskList();
        }
    }

    public static void main(String[] args) {
        new Atlas().run();
    }

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
