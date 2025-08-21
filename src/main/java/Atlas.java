import java.util.Scanner;
import java.util.ArrayList;

public class Atlas {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    private final ArrayList<Task> tasks = new ArrayList<>();
    private int size = 0;

    public static void main(String[] args) { new Atlas().run(); }

    private void run() {
        say("Hello! I'm Atlas\nWhat can I do for you?");
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String input = in.nextLine();
                try {
                    handle(input.trim());   // parse + validate + act
                } catch (AtlasException e) {
                    say("Oops — " + e.getMessage());
                }
            }
        }
    }

    // Handles all cases including invalid inputs
    private void handle(String input) throws AtlasException {
        if (input.isEmpty()) return;

        String[] parts = input.split("\\s+", 2);
        String cmd = parts[0];

        switch (cmd) {
            case "bye":
                say("Bye. Hope to see you again soon!");
                System.exit(0);
                return;

            case "list":
                printList();
                return;

            case "mark": {
                int idx = parseIndex(parts, "mark");
                tasks.get(idx).mark();
                say("Nice! I've marked this task as done:\n " + tasks.get(idx));
                return;
            }

            case "unmark": {
                int idx = parseIndex(parts, "unmark");
                tasks.get(idx).unmark();
                say("OK, I've marked this task as not done yet:\n " + tasks.get(idx));
                return;
            }

            case "todo": {
                String desc = requireArg(parts,
                        "The description of a todo cannot be empty.\n  Try: todo borrow book");
                add(new Todo(desc));
                return;
            }

            case "deadline": {
                String rest = requireArg(parts, "Usage: deadline <desc> /by <when>");
                int at = rest.indexOf(" /by ");
                if (at < 0) throw new AtlasException(
                        "Missing '/by'.\n  Try: deadline return book /by Sunday");
                String desc = rest.substring(0, at).trim();
                String by   = rest.substring(at + 5).trim();
                if (desc.isEmpty() || by.isEmpty()) {
                    throw new AtlasException("Description and '/by' value must be provided.");
                }
                add(new Deadline(desc, by));
                return;
            }

            case "event": {
                String rest = requireArg(parts, "Usage: event <desc> /from <start> /to <end>");
                int fromAt = rest.indexOf(" /from ");
                int toAt   = rest.indexOf(" /to ");
                if (fromAt < 0 || toAt < 0 || toAt <= fromAt) {
                    throw new AtlasException(
                            "Missing '/from' or '/to'.\n  Try: event project /from Mon 2pm /to 4pm");
                }
                String desc = rest.substring(0, fromAt).trim();
                String from = rest.substring(fromAt + 7, toAt).trim();
                String to   = rest.substring(toAt + 5).trim();
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    throw new AtlasException("Description, '/from', and '/to' must be provided.");
                }
                add(new Event(desc, from, to));
                return;
            }

            case "delete": {
                int idx = parseIndex(parts, "delete");
                Task removed = tasks.remove(idx);
                say("Noted. I've removed this task:\n " + removed +
                        "\nNow you have " + tasks.size() + " tasks in the list.");
                return;
            }

            default:
                throw new AtlasException("I don't recognise that command: '" + cmd + "'.");
        }
    }

    private void add(Task t) throws AtlasException {
        tasks.add(t);
        say("Got it. I've added this task:\n " + t + "\nNow you have " + tasks.size() + " tasks in the list.");
    }

    private void printList() {
        if (tasks.isEmpty()) {
            say("(no tasks yet)");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:").append(System.lineSeparator());
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }
        say(sb.toString().trim());
    }

    private void say(String body) {
        System.out.println(LINE);
        for (String line : body.split("\\R")) System.out.println(" " + line);
        System.out.println(LINE);
    }

    // For deletion
    private void delete(Task t) throws AtlasException {

    }

    // Ensures there is a second argument
    private String requireArg(String[] parts, String errorMessage) throws AtlasException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) throw new AtlasException(errorMessage);
        return parts[1].trim();
    }

    // Parses to 0-base index
    private int parseIndex(String[] parts, String command) throws AtlasException {
        String usage = "Usage: " + command + " <task number>";
        if (parts.length < 2) throw new AtlasException(usage);
        String token = parts[1].trim();
        int n;
        try { n = Integer.parseInt(token); }
        catch (NumberFormatException e) {
            throw new AtlasException("Task number must be a positive integer. " + usage);
        }
        if (n < 1 || n > tasks.size()) {
            throw new AtlasException("Task " + token + " is out of range (1.." + size + ").");
        }
        return n - 1;
    }
}
