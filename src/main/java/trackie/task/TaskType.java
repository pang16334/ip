package trackie.task;

/**
 * Identifies the supported task types and their display icons.
 */
public enum TaskType {
    /** A task without a date. */
    TODO("T"),
    /** A task with a due date. */
    DEADLINE("D"),
    /** A task with start and end dates. */
    EVENT("E");

    private final String icon;

    TaskType(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the single-letter icon used to display this task type.
     *
     * @return task type icon
     */
    public String getIcon() {
        return this.icon;
    }
}
