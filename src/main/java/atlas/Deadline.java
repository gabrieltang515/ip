package atlas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Deadline extends Task {
    protected LocalDate by;
    
    private static final DateTimeFormatter IN = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, String by) {
        super(description);
        this.by = LocalDate.parse(by, IN);
    }
    
    @Override
    protected String typeCode() {
        return "D";
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + OUT.format(by) + ")";
    }

    @Override
    public String toSave() {
        return String.format("%s | %d | %s | %s",
                typeCode(), isDone ? 1 : 0, description, by.toString());
    }
}

