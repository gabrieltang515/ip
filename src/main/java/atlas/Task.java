package atlas;

/**
 * Base class for all tasks. A task has a description and a completion status.
 */

public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a task with the given description; tasks are not done by default.
     *
     * @param description brief description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Returns the status icon used in the list output.
     *
     * @return "X" if done, otherwise a single space
     */
    public void unmark() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the single-letter type code (e.g., {@code T}, {@code D}, {@code E}).
     *
     * @return type code of the concrete task
     */
    protected abstract String typeCode();

    /**
     * Serializes this task into a single line suitable for saving to disk.
     *
     * @return save line (pipe-separated fields)
     */
    public String toSave() {
        return String.format("%s | %d | %s",
                typeCode(), isDone ? 1 : 0, description);
    }

    public String toStatusString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns a user-facing string of the form {@code [T][ ] description}.
     *
     * @return display form of this task
     */
    @Override
    public String toString() {
        return toStatusString();
    }


}
