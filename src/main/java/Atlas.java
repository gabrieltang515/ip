import java.util.Scanner;

public class Atlas {
    private static final String LINE =
            "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private final String[] tasks = new String[MAX_TASKS];
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
                } else if (!input.isEmpty()) {
                    addTask(input);
                }
            }
        }
    }

    private void addTask(String task) {
        if (size >= MAX_TASKS) {
            say("Sorry, task list is full (" + MAX_TASKS + ").");
            return;
        }
        tasks[size++] = task;
        say("added: " + task);
    }

    private void printList() {
        if (size == 0) {
            say("(no tasks yet)");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(String.format("%d. %s%n", i + 1, tasks[i]));
        }
        say(sb.toString().trim());
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
