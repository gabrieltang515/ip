public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark()   { this.isDone = true;  }
    public void unmark() { this.isDone = false; }

    public String getStatusIcon() { return isDone ? "X" : " "; }

    public String toStatusString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    @Override public String toString() { return toStatusString(); }
}
