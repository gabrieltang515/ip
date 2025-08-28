package atlas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoEventTest {

    @Test
    void todo_toString_and_toSave_and_mark() {
        Todo t = new Todo("buy bread");
        assertEquals("[T][ ] buy bread", t.toString());
        assertEquals("T | 0 | buy bread", t.toSave());

        t.mark();
        assertEquals("[T][X] buy bread", t.toString());
        assertEquals("T | 1 | buy bread", t.toSave());
    }

    @Test
    void event_toString_and_toSave() {
        Event e = new Event("project meeting", "Mon 2pm", "4pm");
        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)", e.toString());
        assertEquals("E | 0 | project meeting | Mon 2pm | 4pm", e.toSave());

        e.mark();
        assertEquals("[E][X] project meeting (from: Mon 2pm to: 4pm)", e.toString());
        assertEquals("E | 1 | project meeting | Mon 2pm | 4pm", e.toSave());
    }
}
