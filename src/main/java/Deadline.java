public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    protected String typeCode() {
        return "D";
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toSave() {
        return String.format("%s | %d | %s | %s",
                typeCode(), isDone ? 1 : 0, description, by);
    }
}

