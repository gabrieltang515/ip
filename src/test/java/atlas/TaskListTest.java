package atlas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    void add_mark_unmark_remove_and_formatList() {
        TaskList list = new TaskList();

        Todo t1 = new Todo("read book");
        list.add(t1);

        Deadline d = new Deadline("return book", "2025-10-15");
        list.add(d);

        Event e = new Event("meeting", "Mon 2pm", "4pm");
        list.add(e);

        // mark 2, unmark 2
        list.mark(1);
        assertTrue(list.get(1).toString().contains("[X]"));
        list.unmark(1);
        assertTrue(list.get(1).toString().contains("[ ]"));

        // exact formatting (numbered, each on new line)
        String expected =
                "Here are the tasks in your list:\n" +
                        "1." + t1 + "\n" +
                        "2." + d  + "\n" +
                        "3." + e;
        assertEquals(expected, list.formatList());

        // remove middle
        Task removed = list.remove(1);
        assertEquals(d, removed);
        assertEquals(2, list.size());
    }
}
