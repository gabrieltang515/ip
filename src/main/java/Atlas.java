import java.util.Scanner;

public class Atlas {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;

    private final Task[] tasks = new Task[MAX_TASKS];
    private int size = 0;

    public static void main(String[] args) { new Atlas().run(); }

    private void run() {
        say("Hello! I'm Atlas\nWhat can I do for you?");

        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String input = in.nextLine().trim();

                if (input.equals("bye")) {
                    say("Bye. Hope to see you again soon!");
                    break;
                } else if (input.equals("list")) {
                    printList();
                } else if (input.startsWith("mark ")) {
                    markCommand(input);
                } else if (input.startsWith("unmark ")) {
                    unmarkCommand(input);
                } else if (input.startsWith("todo ")) {
                    String desc = input.substring(5).trim();
                    if (desc.isEmpty()) { say("Oops. The description of a todo cannot be empty."); continue; }
                    add(new Todo(desc));
                } else if (input.startsWith("deadline ")) {
                    String rest = input.substring(9).trim(); // "<desc> /by <when>"
                    int at = rest.indexOf(" /by ");
                    if (at < 0) { say("Oops. Use: deadline <desc> /by <when>"); continue; }
                    String desc = rest.substring(0, at).trim();
                    String by   = rest.substring(at + 5).trim();
                    if (desc.isEmpty() || by.isEmpty()) { say("Oops. Description and /by must be provided."); continue; }
                    add(new Deadline(desc, by));
                } else if (input.startsWith("event ")) {
                    String rest = input.substring(6).trim(); // "<desc> /from <start> /to <end>"
                    int fromAt = rest.indexOf(" /from ");
                    int toAt   = rest.indexOf(" /to ");
                    if (fromAt < 0 || toAt < 0 || toAt <= fromAt) { say("Oops. Use: event <desc> /from <start> /to <end>"); continue; }
                    String desc = rest.substring(0, fromAt).trim();
                    String from = rest.substring(fromAt + 7, toAt).trim();
                    String to   = rest.substring(toAt + 5).trim();
                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) { say("Oops. Description, /from and /to must be provided."); continue; }
                    add(new Event(desc, from, to));
                } else if (!input.isEmpty()) {
                    // Fallback: treat a bare line as a Todo
                    add(new Todo(input));
                }
            }
        }
    }

    private void add(Task t) {
        if (size >= MAX_TASKS) { say("Sorry, task list is full (" + MAX_TASKS + ")."); return; }
        tasks[size++] = t;
        say("Got it. I've added this task:\n " + t + "\nNow you have " + size + " tasks in the list.");
    }

    private void printList() {
        if (size == 0) { say("(no tasks yet)"); return; }
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:").append(System.lineSeparator());
        for (int i = 0; i < size; i++) sb.append(String.format("%d.%s%n", i + 1, tasks[i]));
        say(sb.toString().trim());
    }

    private void markCommand(String input) {
        Integer idx = parseIndex(input);
        if (idx == null || idx < 0 || idx >= size) { say("Please provide a valid task number to mark."); return; }
        tasks[idx].mark();
        say("Nice! I've marked this task as done:\n " + tasks[idx]);
    }

    private void unmarkCommand(String input) {
        Integer idx = parseIndex(input);
        if (idx == null || idx < 0 || idx >= size) { say("Please provide a valid task number to unmark."); return; }
        tasks[idx].unmark();
        say("OK, I've marked this task as not done yet:\n " + tasks[idx]);
    }

    private Integer parseIndex(String input) {
        String[] p = input.split("\\s+");
        if (p.length < 2) return null;
        try { return Integer.parseInt(p[1]) - 1; } catch (NumberFormatException e) { return null; }
    }

    private void say(String body) {
        System.out.println(LINE);
        for (String line : body.split("\\R")) System.out.println(" " + line);
        System.out.println(LINE);
    }
}
