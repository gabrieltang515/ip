import java.util.Scanner;

public class Atlas {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private final Task[] tasks = new Task[MAX_TASKS];
    private int size = 0;

    public static void main(String[] args) {
        new Atlas().run();
    }

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
                } else if (!input.isEmpty()) {
                    addTask(input);
                }
            }
        }
    }

    private void addTask(String description) {
        if (size >= MAX_TASKS) {
            say("Sorry, task list is full (" + MAX_TASKS + ").");
            return;
        }
        tasks[size++] = new Task(description);
        say("added: " + description);
    }

    private void printList() {
        if (size == 0) {
            say("(no tasks yet)");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:").append(System.lineSeparator());
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%d.[%s] %s%n",
                    i + 1, tasks[i].getStatusIcon(), tasks[i].description));
        }
        say(sb.toString().trim());
    }

    private void markCommand(String input) {
        Integer idx = parseIndex(input);
        if (idx == null || idx < 0 || idx >= size) {
            say("Please provide a valid task number to mark.");
            return;
        }
        tasks[idx].mark();
        say("Nice! I've marked this task as done:\n " + tasks[idx].toStatusString());
    }

    private void unmarkCommand(String input) {
        Integer idx = parseIndex(input);
        if (idx == null || idx < 0 || idx >= size) {
            say("Please provide a valid task number to unmark.");
            return;
        }
        tasks[idx].unmark();
        say("OK, I've marked this task as not done yet:\n " + tasks[idx].toStatusString());
    }

    // Converts to 0-based index, or null if invalid
    private Integer parseIndex(String input) {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) return null;
        try {
            return Integer.parseInt(parts[1]) - 1; // user uses 1-based numbering
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void say(String body) {
        System.out.println(LINE);
        // body may be multi-line (greeting and list); indent once like the sample
        for (String line : body.split("\\R")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }
}






