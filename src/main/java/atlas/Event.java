package atlas;

/**
 * A task that occurs during a time window described by free-text with
 * from and to fields.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event task.
     *
     * @param description description of the event
     * @param from        start information (free text)
     * @param to          end information (free text)
     */
    public Event(String description, String from, String to) {
        super(description);
        assert description != null && !description.trim().isEmpty() : "Event description must not be empty";
        assert from != null && !from.trim().isEmpty() : "Event 'from' must not be empty";
        assert to != null && !to.trim().isEmpty() : "Event 'to' must not be empty";
        this.from = from;
        this.to = to;
    }

    @Override
    protected String typeCode() {
        return "E";
    }

    /** {@inheritDoc}
     * with a {@code (from: ... to: ...)} suffix.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Serializes the event with its from and to values.
     *
     * @return save line containing the time window
     */
    @Override
    public String toSave() {
        return String.format("%s | %d | %s | %s | %s",
                typeCode(), isDone ? 1 : 0, description, from, to);
    }

    /**
     * Two events are equal if they have the same description, from, and to times.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Event other = (Event) obj;
        return from.equals(other.from) && to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + from.hashCode() + to.hashCode();
    }
}
