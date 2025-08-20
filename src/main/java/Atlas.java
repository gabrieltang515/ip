import java.util.Scanner;

public class Atlas {
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        String LINE =
                "____________________________________________________________";

        System.out.println(LINE);
        System.out.println("Hello I'm Atlas!");
        System.out.println("What can I do for you?");
        System.out.println(LINE);

        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String input = in.nextLine();
            if (input.equals("bye")) {
                System.out.println(LINE);
                System.out.println(" Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }
            System.out.println(LINE);
            System.out.println(" " + input);   // echo
            System.out.println(LINE);
        }
        in.close();
    }
}


