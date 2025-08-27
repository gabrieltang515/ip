package atlas;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path file;

    public Storage(String relativePath) {
        this.file = Paths.get(relativePath);
    }

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

    public void save(List<Task> tasks) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toSave());
        }
        Files.write(file, lines);
    }

    private Task parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        // Regex trims the spaces between pipes
        String[] p = line.split("\\s*\\|\\s*");

        try {
            String type = p[0];
            boolean done = "1".equals(p[1]);
            switch (type) {
            case "T": {
                Task t = new Todo(p[2]);
                if (done) {
                    t.mark();
                }
                return t;
            }

            case "D": {
                Deadline d = new Deadline(p[2], p[3]);
                if (done) {
                    d.mark();
                }
                return d;
            }

            case "E": {
                Event e = new Event(p[2], p[3], p[4]);
                if (done) {
                    e.mark();
                }
                return e;
            }

            default: {
                return null;
            }
            }
        } catch (Exception ignore) {
            return null;
        }
    }

}
