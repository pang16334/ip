/**
 * Represents a task that occurs between a start and end date or time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event with the given description and timing.
     *
     * @param description description of the event
     * @param from date or time when the event starts
    * @param to date or time when the event ends
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event with its type, status, and timing.
     *
     * @return formatted event
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }

    /**
     * Returns a data-file line containing this event's start and end times.
     *
     * @return serialized event
     */
    @Override
    public String toDataString() {
        return super.toDataString() + " | " + this.from + " | " + this.to;
    }
}
