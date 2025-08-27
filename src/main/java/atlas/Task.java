package atlas;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    // typeCode can return T, D or E (atlas.Task, atlas.Deadline or atlas.Event)
    protected abstract String typeCode();

    public String toSave() {
        return String.format("%s | %d | %s",
                typeCode(), isDone ? 1 : 0, description);
    }

    public String toStatusString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    @Override
    public String toString() {
        return toStatusString();
    }


}
