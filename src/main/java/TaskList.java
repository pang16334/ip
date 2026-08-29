import java.util.ArrayList;
import java.util.List;

/** Manages the tasks currently known to Trackie. */
public class TaskList {
    private final ArrayList<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /** @param tasks tasks loaded from storage */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** @return number of tasks in the list */
    public int size() {
        return this.tasks.size();
    }

    /** @return task at the given zero-based index */
    public Task get(int index) {
        return this.tasks.get(index);
    }

    /** @param task task to append */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /** @return task removed from the given zero-based index */
    public Task remove(int index) {
        return this.tasks.remove(index);
    }

    /** @return read-only snapshot suitable for saving */
    public List<Task> asList() {
        return List.copyOf(this.tasks);
    }
}
