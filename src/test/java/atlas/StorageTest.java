package atlas;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class StorageTest {

    @TempDir
    Path tempDir;  // JUnit provides a fresh temp directory per test

    @Test
    void load_whenFileMissing_returnsEmpty_andCreatesParentDir() throws IOException {
        Path savePath = tempDir.resolve("duke.txt");     // e.g., /tmp/junit123/duke.txt
        Storage storage = new Storage(savePath.toString());

        List<Task> tasks = storage.load();               // First run: file doesn't exist
        assertTrue(tasks.isEmpty(), "Expected empty task list on first run");

        // Our Storage creates parent directories on first load/save
        assertTrue(Files.exists(savePath.getParent()), "Parent directory should be created");
        assertFalse(Files.exists(savePath), "Save file should not be created by load()");
    }

    @Test
    void save_thenReload_roundTripsAllTasks_andFormatsFile() throws IOException {
        Path savePath = tempDir.resolve("duke.txt");
        Storage storage = new Storage(savePath.toString());

        // Build a sample list
        List<Task> list = new ArrayList<>();
        list.add(new Todo("read book"));

        Deadline d = new Deadline("return book", "2025-10-15");
        d.mark();
        list.add(d);

        list.add(new Event("project meeting", "Mon 2pm", "4pm"));

        // Save
        storage.save(list);
        assertTrue(Files.exists(savePath), "Save file should exist after save()");

        // Verify exact file contents (ISO for date)
        List<String> lines = Files.readAllLines(savePath);
        List<String> expected = List.of(
                "T | 0 | read book",
                "D | 1 | return book | 2025-10-15",
                "E | 0 | project meeting | Mon 2pm | 4pm"
        );
        assertEquals(expected, lines);

        // Reload and verify round-trip using toSave() (no getters needed)
        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toSave());
        assertEquals("D | 1 | return book | 2025-10-15", loaded.get(1).toSave());
        assertEquals("E | 0 | project meeting | Mon 2pm | 4pm", loaded.get(2).toSave());
    }
}
