package atlas;

public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String typeCode() {
        return "E";
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toSave() {
        return String.format("%s | %d | %s | %s | %s",
                typeCode(), isDone ? 1 : 0, description, from, to);
    }

}
