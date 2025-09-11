package atlas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists the task list to a simple text file and loads it back.
 * <p>
 * The file is created lazily and parent directories are created if necessary.
 */
public class Storage {
    private final Path file;

    /**
     * Creates a storage instance bound to the given relative/absolute path.
     *
     * @param relativePath path to the save file (e.g. data/Atlas.txt)
     */
    public Storage(String relativePath) {
        assert relativePath != null && !relativePath.trim().isEmpty() : "storage path must not be empty";
        this.file = Paths.get(relativePath);
    }


    /**
     * Loads tasks from disk. If the file does not exist, an empty list is returned
     * and the parent directory is created so that subsequent saves succeed.
     *
     * @return list of tasks loaded from the save file
     * @throws IOException if the file exists but cannot be read
     */
    public List<Task> load() throws IOException {
        List<Task> out = new ArrayList<>();

        if (!Files.exists(file)) {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
                return out;
            }
        }

        List<String> lines = Files.readAllLines(file);
        for (String line : lines) {
            Task t = parse(line);
            if (t != null) {
                out.add(t);
            }
        }
        return out;
    }

    /**
     * Saves all tasks to disk, overwriting the file.
     *
     * @param tasks tasks to write
     * @throws IOException if the file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        assert tasks != null : "tasks to save must not be null";
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toSave());
        }
        Files.write(file, lines);
    }

    // Named constants for array indices
    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DATE_INDEX = 3;
    private static final int FROM_INDEX = 3;
    private static final int TO_INDEX = 4;
    
    // Task type constants
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String DONE_STATUS = "1";

    // Parses a single save line; invalid lines are ignored.
    private Task parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }

        try {
            return createTaskFromParts(parts);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Task createTaskFromParts(String[] parts) {
        String type = parts[TYPE_INDEX];
        boolean done = DONE_STATUS.equals(parts[STATUS_INDEX]);
        
        Task task = switch (type) {
            case TODO_TYPE -> new Todo(parts[DESCRIPTION_INDEX]);
            case DEADLINE_TYPE -> new Deadline(parts[DESCRIPTION_INDEX], parts[DATE_INDEX]);
            case EVENT_TYPE -> new Event(parts[DESCRIPTION_INDEX], parts[FROM_INDEX], parts[TO_INDEX]);
            default -> null;
        };
        
        if (task != null && done) {
            task.mark();
        }
        
        return task;
    }

}
