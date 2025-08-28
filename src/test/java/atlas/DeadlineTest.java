package atlas;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    void toString_formatsPrettyDate_and_toSaveUsesIso() {
        Deadline d = new Deadline("return book", "2025-10-15");

        // Pretty-printed UI (Level 8 requirement)
        assertEquals("[D][ ] return book (by: Oct 15 2025)", d.toString());

        // Persisted as ISO yyyy-MM-dd
        assertEquals("D | 0 | return book | 2025-10-15", d.toSave());

        // After mark() both UI and save format reflect done state
        d.mark();
        assertEquals("[D][X] return book (by: Oct 15 2025)", d.toString());
        assertEquals("D | 1 | return book | 2025-10-15", d.toSave());
    }
}
