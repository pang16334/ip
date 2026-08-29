import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Saves Trackie's tasks to a file on the local disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that uses the given OS-independent path.
     *
     * @param filePath path of the task data file
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Replaces the data file with the current task list.
     *
     * @param tasks tasks to save
     * @throws TrackieException if the folder or file cannot be written
     */
    public void saveTasks(List<Task> tasks) throws TrackieException {
        try {
            Path parentDirectory = this.filePath.getParent();
            if (parentDirectory != null) {
                Files.createDirectories(parentDirectory);
            }
            List<String> taskLines = tasks.stream()
                    .map(Task::toDataString)
                    .toList();
            Files.write(this.filePath, taskLines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new TrackieException("Oops! I couldn't save your tasks.");
        }
    }

    /**
     * Loads tasks from the data file, or returns an empty list on first launch.
     *
     * @return tasks reconstructed from the data file
     * @throws TrackieException if the file cannot be read or contains an unknown task type
     */
    public ArrayList<Task> loadTasks() throws TrackieException {
        if (Files.notExists(this.filePath)) {
            return new ArrayList<>();
        }

        try {
            ArrayList<Task> tasks = new ArrayList<>();
            for (String line : Files.readAllLines(this.filePath, StandardCharsets.UTF_8)) {
                tasks.add(parseTask(line));
            }
            return tasks;
        } catch (IOException exception) {
            throw new TrackieException("Oops! I couldn't load your saved tasks.");
        }
    }

    /**
     * Reconstructs one task from a line in the data file.
     *
     * @param line serialized task
     * @return reconstructed task
     * @throws TrackieException if the task type is unknown
     */
    private Task parseTask(String line) throws TrackieException {
        String[] fields = line.split(" \\| ", -1);
        Task task;
        switch (fields[0]) {
        case "T":
            task = new Todo(fields[2]);
            break;
        case "D":
            task = new Deadline(fields[2], fields[3]);
            break;
        case "E":
            task = new Event(fields[2], fields[3], fields[4]);
            break;
        default:
            throw new TrackieException("Oops! The saved task file contains an unknown task type.");
        }

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
