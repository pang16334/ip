package trackie.task;

/**
 * Identifies the supported task types and their display icons.
 */
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
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
