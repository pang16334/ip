import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
}
