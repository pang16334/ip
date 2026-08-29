import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Trackie chatbot and responds to commands entered by the user.
 */
public class Trackie {
    public static void main(String[] args) {
        String banner = " _______             _    _\n"
                + "|__   __|           | |  (_)\n"
                + "   | |_ __ __ _  ___| | ___  ___\n"
                + "   | | '__/ _` |/ __| |/ / |/ _ \\\n"
                + "   | | | | (_| | (__|   <| |  __/\n"
                + "   |_|_|  \\__,_|\\___|_|\\_\\_|\\___|\n";
        System.out.println(banner);
        System.out.println("Hello! I'm Trackie.");
        System.out.println("What can I do for you today?");

        ArrayList<Task> tasks = new ArrayList<>();
        Storage storage = new Storage("data/trackie.txt");

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                try {
                    if (command.equals("bye")) {
                        System.out.println("Bye! Consistency is the key. Hope to see you again soon!");
                        break;
                    } else if (command.equals("list")) {
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < tasks.size(); i++) {
                            System.out.println((i + 1) + "." + tasks.get(i));
                        }
                    } else if (command.equals("mark") || command.startsWith("mark ")) {
                        int taskIndex = parseTaskIndex(command, "mark", tasks.size());
                        tasks.get(taskIndex).markAsDone();
                        storage.saveTasks(tasks);
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks.get(taskIndex));
                    } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                        int taskIndex = parseTaskIndex(command, "unmark", tasks.size());
                        tasks.get(taskIndex).markAsNotDone();
                        storage.saveTasks(tasks);
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks.get(taskIndex));
                    } else if (command.equals("delete") || command.startsWith("delete ")) {
                        int taskIndex = parseTaskIndex(command, "delete", tasks.size());
                        Task removedTask = tasks.remove(taskIndex);
                        storage.saveTasks(tasks);
                        String taskWord = tasks.size() == 1 ? "task" : "tasks";
                        System.out.println("Noted. I've removed this task:");
                        System.out.println("  " + removedTask);
                        System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
                    } else if (command.equals("todo") || command.startsWith("todo ")) {
                        String description = command.substring(4).trim();
                        if (description.isEmpty()) {
                            throw new TrackieException("Oops! A todo needs a description.");
                        }
                        Task task = new Todo(description);
                        addTask(tasks, task, storage);
                    } else if (command.equals("deadline") || command.startsWith("deadline ")) {
                        Task task = parseDeadline(command);
                        addTask(tasks, task, storage);
                    } else if (command.equals("event") || command.startsWith("event ")) {
                        Task task = parseEvent(command);
                        addTask(tasks, task, storage);
                    } else {
                        throw new TrackieException("Oops! I don't recognize that command.");
                    }
                } catch (TrackieException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        }
    }

    /**
     * Stores a task and displays confirmation.
     *
     * @param tasks list in which tasks are stored
     * @param task task to add
     * @param storage storage used to save the updated list
     * @throws TrackieException if the updated list cannot be saved
     */
    private static void addTask(ArrayList<Task> tasks, Task task, Storage storage)
            throws TrackieException {
        tasks.add(task);
        storage.saveTasks(tasks);
        String taskWord = tasks.size() == 1 ? "task" : "tasks";

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
    }

    /**
     * Parses and validates the task number in a mark, unmark, or delete command.
     *
     * @param command full command entered by the user
     * @param commandName name of the command being parsed
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws TrackieException if the task number is missing, invalid, or out of range
     */
    private static int parseTaskIndex(String command, String commandName, int taskCount)
            throws TrackieException {
        if (taskCount == 0) {
            throw new TrackieException("Oops! There are no tasks to " + commandName + ".");
        }

        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new TrackieException("Oops! Please specify a task number after " + commandName + ".");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new TrackieException("Oops! The task number must be a whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new TrackieException("Oops! Choose a task number between 1 and " + taskCount + ".");
        }
        return taskNumber - 1;
    }

    /**
     * Parses and validates a deadline command.
     *
     * @param command full deadline command
     * @return deadline represented by the command
     * @throws TrackieException if its description or due time is missing
     */
    private static Task parseDeadline(String command) throws TrackieException {
        int byIndex = command.indexOf(" /by");
        if (byIndex < 0) {
            throw new TrackieException("Oops! Use: deadline DESCRIPTION /by TIME");
        }

        String description = command.substring(8, byIndex).trim();
        String by = command.substring(byIndex + 4).trim();
        if (description.isEmpty()) {
            throw new TrackieException("Oops! A deadline needs a description.");
        }
        if (by.isEmpty()) {
            throw new TrackieException("Oops! A deadline needs a due date or time after /by.");
        }
        return new Deadline(description, by);
    }

    /**
     * Parses and validates an event command.
     *
     * @param command full event command
     * @return event represented by the command
     * @throws TrackieException if its description, start, or end is missing
     */
    private static Task parseEvent(String command) throws TrackieException {
        int fromIndex = command.indexOf(" /from");
        int toIndex = command.indexOf(" /to");
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new TrackieException("Oops! Use: event DESCRIPTION /from START /to END");
        }

        String description = command.substring(5, fromIndex).trim();
        String from = command.substring(fromIndex + 6, toIndex).trim();
        String to = command.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new TrackieException("Oops! An event needs a description.");
        }
        if (from.isEmpty()) {
            throw new TrackieException("Oops! An event needs a start after /from.");
        }
        if (to.isEmpty()) {
            throw new TrackieException("Oops! An event needs an end after /to.");
        }
        return new Event(description, from, to);
    }
}
