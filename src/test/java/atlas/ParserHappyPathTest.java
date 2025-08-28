package atlas;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ParserHappyPathTest {

    // Ui test double to capture the last message body
    static class TestUi extends Ui {
        String last;
        @Override public void show(String body) {
            last = body;
        }
        @Override public void showGreeting() {
            last = "GREETING";
        }
        @Override public void showBye() {
            last = "Bye. Hope to see you again soon!";
        }
        @Override public void showError(String msg) {
            last = "ERR: " + msg;
        }
    }

    @TempDir Path tmp;

    @Test
    void endToEnd_flow_marks_lists_deletes_and_byes() throws Exception {
        Storage storage = new Storage(tmp.resolve("duke.txt").toString());
        TaskList tasks = new TaskList();
        TestUi ui = new TestUi();

        // add todo
        boolean exit = Parser.parse("todo read book", tasks, ui, storage);
        assertFalse(exit);
        assertEquals(1, tasks.size());
        assertEquals("Got it. I've added this task:\n " + tasks.get(0)
                        + "\nNow you have 1 tasks in the list.",
                ui.last);

        // add deadline (Level-8 ISO date)
        exit = Parser.parse("deadline return book /by 2025-10-15", tasks, ui, storage);
        assertFalse(exit);
        assertEquals(2, tasks.size());
        assertEquals("Got it. I've added this task:\n " + tasks.get(1)
                        + "\nNow you have 2 tasks in the list.",
                ui.last);

        // pretty UI for the date
        assertTrue(tasks.get(1).toString().contains("(by: Oct 15 2025)"));

        // mark 2
        exit = Parser.parse("mark 2", tasks, ui, storage);
        assertFalse(exit);
        assertTrue(tasks.get(1).toString().contains("[X]"));
        assertEquals("Nice! I've marked this task as done:\n " + tasks.get(1), ui.last);

        // list
        exit = Parser.parse("list", tasks, ui, storage);
        assertFalse(exit);
        assertEquals(tasks.formatList(), ui.last);

        // delete #2
        Task removed = tasks.get(1); // capture expected before deletion
        exit = Parser.parse("delete 2", tasks, ui, storage);
        assertFalse(exit);
        assertEquals(1, tasks.size());
        assertEquals("Noted. I've removed this task:\n " + removed
                        + "\nNow you have " + tasks.size() + " tasks in the list.",
                ui.last);

        // bye
        exit = Parser.parse("bye", tasks, ui, storage);
        assertTrue(exit);
        assertEquals("Bye. Hope to see you again soon!", ui.last);

        // storage file exists with at least the remaining todo
        List<String> lines = java.nio.file.Files.readAllLines(tmp.resolve("duke.txt"));
        assertTrue(lines.get(0).startsWith("T |"));
    }
}
