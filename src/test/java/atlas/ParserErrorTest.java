package atlas;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class ParserErrorTest {

    @TempDir Path tmp;
    Storage storage;
    TaskList tasks;
    Ui ui;

    @BeforeEach
    void setup() {
        storage = new Storage(tmp.resolve("duke.txt").toString());
        tasks = new TaskList();
        ui = new Ui();
    }

    @Test
    void todo_missingDescription_throws() {
        AtlasException ex = assertThrows(AtlasException.class,
                () -> Parser.parse("todo   ", tasks, ui, storage));
        assertTrue(ex.getMessage().toLowerCase().contains("description"));
    }

    @Test
    void deadline_invalidDate_throws() {
        AtlasException ex = assertThrows(AtlasException.class,
                () -> Parser.parse("deadline report /by not-a-date", tasks, ui, storage));
        assertTrue(ex.getMessage().toLowerCase().contains("invalid date"));
    }

    @Test
    void mark_nonInteger_throws() {
        AtlasException ex = assertThrows(AtlasException.class,
                () -> Parser.parse("mark abc", tasks, ui, storage));
        assertTrue(ex.getMessage().toLowerCase().contains("integer"));
    }

    @Test
    void mark_outOfRange_throws() {
        AtlasException ex = assertThrows(AtlasException.class,
                () -> Parser.parse("mark 1", tasks, ui, storage));
        assertTrue(ex.getMessage().toLowerCase().contains("out of range"));
    }

    @Test
    void unknownCommand_throws() {
        AtlasException ex = assertThrows(AtlasException.class,
                () -> Parser.parse("abracadabra", tasks, ui, storage));
        assertTrue(ex.getMessage().toLowerCase().contains("don't recognise")
                || ex.getMessage().toLowerCase().contains("recognise"));
    }
}
