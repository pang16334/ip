/**
 * Represents a task that must be completed by a specific date or time.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline with the given description and due time.
     *
     * @param description description of the deadline
    * @param by date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the deadline with its type, status, and due time.
     *
     * @return formatted deadline
     */
    @Override
    public String toString() {
        return super.toString() + " (by: " + this.by + ")";
    }
}
