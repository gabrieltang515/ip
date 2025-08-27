package atlas;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task remove(int idx) {
        return tasks.remove(idx);
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public void mark(int idx) {
        tasks.get(idx).mark();
    }

    public void unmark(int idx) {
        tasks.get(idx).unmark();
    }

    public List<Task> asList() {
        return tasks;
    }

    public String formatList() {
        if (tasks.isEmpty()) {
            return "(no tasks yet)";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list: ").append(System.lineSeparator());
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + 1, tasks.get(i)));
        }
        return sb.toString().trim();
    }
}
