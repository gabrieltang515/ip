public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override protected String typeCode() {
        return "T";
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

