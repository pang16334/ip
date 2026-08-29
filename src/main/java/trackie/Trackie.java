package trackie;

import trackie.exception.TrackieException;
import trackie.parser.Parser;
import trackie.storage.Storage;
import trackie.task.Task;
import trackie.task.TaskList;
import trackie.ui.Ui;

/** Coordinates Trackie's user interface, task list, parser, and storage. */
public class Trackie {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /** @param filePath relative path of the task data file */
    public Trackie(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.ui.showWelcome();

        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(this.storage.loadTasks());
        } catch (TrackieException exception) {
            this.ui.showError(exception.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /** Reads and executes commands until the user exits or input ends. */
    public void run() {
        try (this.ui) {
            while (this.ui.hasNextCommand()) {
                String command = this.ui.readCommand();
                try {
                    if (!execute(command)) {
                        return;
                    }
                } catch (TrackieException exception) {
                    this.ui.showError(exception.getMessage());
                }
            }
        }
    }

    /** @param args command-line arguments, which are not used */
    public static void main(String[] args) {
        new Trackie("data/trackie.txt").run();
    }

    private boolean execute(String command) throws TrackieException {
        String commandWord = Parser.getCommandWord(command);
        switch (commandWord) {
        case "bye":
            requireExactCommand(command, "bye");
            this.ui.showGoodbye();
            return false;
        case "list":
            requireExactCommand(command, "list");
            this.ui.showTaskList(this.tasks);
            break;
        case "mark":
            updateTaskStatus(command, true);
            break;
        case "unmark":
            updateTaskStatus(command, false);
            break;
        case "delete":
            deleteTask(command);
            break;
        case "todo":
            addTask(Parser.parseTodo(command));
            break;
        case "deadline":
            addTask(Parser.parseDeadline(command));
            break;
        case "event":
            addTask(Parser.parseEvent(command));
            break;
        case "find":
            this.ui.showMatchingTasks(this.tasks.find(Parser.parseFindKeyword(command)));
            break;
        default:
            throw unknownCommandException();
        }
        return true;
    }

    private void updateTaskStatus(String command, boolean isDone) throws TrackieException {
        String commandName = isDone ? "mark" : "unmark";
        int taskIndex = Parser.parseTaskIndex(command, commandName, this.tasks.size());
        Task task = this.tasks.get(taskIndex);
        if (isDone) {
            task.markAsDone();
            this.storage.saveTasks(this.tasks.asList());
            this.ui.showMarked(task);
        } else {
            task.markAsNotDone();
            this.storage.saveTasks(this.tasks.asList());
            this.ui.showUnmarked(task);
        }
    }

    private void deleteTask(String command) throws TrackieException {
        int taskIndex = Parser.parseTaskIndex(command, "delete", this.tasks.size());
        Task removedTask = this.tasks.remove(taskIndex);
        this.storage.saveTasks(this.tasks.asList());
        this.ui.showDeleted(removedTask, this.tasks.size());
    }

    private void addTask(Task task) throws TrackieException {
        this.tasks.add(task);
        this.storage.saveTasks(this.tasks.asList());
        this.ui.showAdded(task, this.tasks.size());
    }

    private void requireExactCommand(String command, String expectedCommand) throws TrackieException {
        if (!command.equals(expectedCommand)) {
            throw unknownCommandException();
        }
    }

    private TrackieException unknownCommandException() {
        return new TrackieException("Oops! I don't recognize that command.");
    }
}
