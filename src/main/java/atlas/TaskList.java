package atlas;

import java.util.ArrayList;
import java.util.List;

/**
 * Mutable collection of Task objects with convenience operations used
 * by the chatbot.
 */
public class TaskList {
    private final ArrayList<Task> tasks;
    
    // Constants for formatting
    private static final int USER_DISPLAY_OFFSET = 1; // Convert 0-based to 1-based indexing
    private static final String NO_TASKS_MESSAGE = "(no tasks yet)";
    private static final String NO_MATCHING_TASKS_MESSAGE = "(no matching tasks)";
    private static final String TASKS_LIST_HEADER = "Here are the tasks in your list:";
    private static final String MATCHING_TASKS_HEADER = "Here are the matching tasks in your list:";

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list pre-populated with the given tasks.
     *
     * @param initial tasks to start with
     */
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the list has no tasks.
     *
     * @return {@code true} if empty
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param t task to add
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param idx zero-based index
     * @return the removed task
     * @throws IndexOutOfBoundsException if {@code idx} is out of range
     */
    public Task remove(int idx) {
        return tasks.remove(idx);
    }

    /**
     * Returns the task at the given index without removing it.
     *
     * @param idx zero-based index
     * @return the task at {@code idx}
     * @throws IndexOutOfBoundsException if {@code idx} is out of range
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Marks the task at the given index as done.
     *
     * @param idx zero-based index
     */
    public void mark(int idx) {
        tasks.get(idx).mark();
    }

    /**
     * Marks the task at the given index as not done.
     *
     * @param idx zero-based index
     */
    public void unmark(int idx) {
        tasks.get(idx).unmark();
    }

    /**
     * Returns a live view of the underlying list (used for persistence).
     *
     * @return backing list of tasks
     */
    public List<Task> asList() {
        return tasks;
    }

    /**
     * Formats the entire list in the same style used by the UI.
     *
     * @return multi-line string containing the numbered list of tasks
     */
    public String formatList() {
        if (tasks.isEmpty()) {
            return NO_TASKS_MESSAGE;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(TASKS_LIST_HEADER).append(System.lineSeparator());
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(String.format("%d.%s%n", i + USER_DISPLAY_OFFSET, tasks.get(i)));
        }
        return sb.toString().trim();
    }

    /**
     * Returns all tasks whose descriptions contain the given keyword
     * (case-insensitive).
     *
     * @param keyword keyword to search for
     * @return list of matching tasks (order preserved)
     */
    public java.util.List<Task> find(String keyword) {
        java.util.ArrayList<Task> out = new java.util.ArrayList<>();
        String k = keyword.toLowerCase();
        for (Task t : tasks) {
            if (t.description.toLowerCase().contains(k)) {
                out.add(t);
            }
        }
        return out;
    }


    /**
     * Formats the matching tasks in the same style used by the examples.
     * Numbering starts at 1 within the result set.
     *
     * @param keyword keyword to search for
     * @return multi-line formatted result, or "(no matching tasks)" if none
     */
    public String formatMatches(String keyword) {
        java.util.List<Task> matches = find(keyword);
        if (matches.isEmpty()) {
            return NO_MATCHING_TASKS_MESSAGE;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(MATCHING_TASKS_HEADER)
                .append(System.lineSeparator());
        for (int i = 0; i < matches.size(); i++) {
            sb.append(String.format("%d.%s%n", i + USER_DISPLAY_OFFSET, matches.get(i)));
        }
        return sb.toString().trim();
    }
}
