package atlas;

/**
 * Handles all user-facing messages and formatting of the chat frame.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private String last;

    /**
     * Shows arbitrary text inside the standard frame.
     *
     * @param body text to display (may contain multiple lines)
     */
    public void show(String body) {
        assert body != null : "UI body must not be null";
        last = body;
        System.out.println(LINE);
        for (String line : body.split("\\R")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }

    /** Shows the greeting message. */
    public void showGreeting() {
        show("Hello! I am atlas.Atlas \nWhat can I do for you?");
    }

    /** Shows the farewell message. */
    public void showBye() {
        show("Bye. Hope to see you again soon!");
    }


    /**
     * Shows an error message in a friendly way.
     *
     * @param msg error details to display
     */
    public void showError(String msg) {
        show("Oops - " + msg);
    }

    public String getLast() {
        return last == null ? "" : last;
    }
}
