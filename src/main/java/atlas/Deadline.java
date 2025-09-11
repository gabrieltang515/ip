package atlas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that needs to be completed by a specific date.
 * <p>
 * Dates are stored as LocalDate and are accepted from the user in
 * ISO format {yyyy-MM-dd}. They are shown in the list as
 * {MMMM dd yyyy} (e.g., {Oct 15 2025}).
 */
public class Deadline extends Task {
    protected LocalDate by;

    private static final DateTimeFormatter IN = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter OUT = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Creates a deadline task.
     *
     * @param description description of the task
     * @param by          due date in ISO format {yyyy-MM-dd}
     * @throws java.time.format.DateTimeParseException if a valid ISO date is not received
     */
    public Deadline(String description, String by) {
        super(description);
        assert description != null && !description.trim().isEmpty() : "Deadline description must not be empty";
        assert by != null && !by.trim().isEmpty() : "Deadline 'by' date must not be empty";
        this.by = LocalDate.parse(by, IN);
    }

    @Override
    protected String typeCode() {
        return "D";
    }

    /**
     * {@inheritDoc}
     * with date rendered in a friendly format.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + OUT.format(by) + ")";
    }


    /**
     * Serializes the task using ISO date for reliable reloading.
     *
     * @return save line containing the ISO due date
     */
    @Override
    public String toSave() {
        return String.format("%s | %d | %s | %s",
                typeCode(), isDone ? 1 : 0, description, by.toString());
    }
}

