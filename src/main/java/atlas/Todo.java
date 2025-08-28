package atlas;

/**
 * A simple task without any date/time component.
 */
public class Todo extends Task {

    /**
     * Creates a new {@code Todo} with the given description.
     *
     * @param description task description
     */
    public Todo(String description) {
        super(description);
    }

    @Override protected String typeCode() {
        return "T";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

