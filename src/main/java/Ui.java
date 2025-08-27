public class Ui {
    private static final String LINE = "____________________________________________________________";
    
    public void show(String body) {
        System.out.println(LINE);
        for (String line : body.split("\\R")) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }
    
    public void showGreeting() {
        show("Hello! I am Atlas \nWhat can I do for you?");
    }
    
    public void showBye() {
        show("Bye. Hope to see you again soon!");
    }
    
    public void showError(String msg) {
        show("Oops - " + msg);
    }
}
