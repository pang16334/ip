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

        Task[] tasks = new Task[100];
        int taskCount = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                try {
                    if (command.equals("bye")) {
                        System.out.println("Bye! Consistency is the key. Hope to see you again soon!");
                        break;
                    } else if (command.equals("list")) {
                        System.out.println("Here are the tasks in your list:");
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println((i + 1) + "." + tasks[i]);
                        }
                    } else if (command.startsWith("mark ")) {
                        int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                        tasks[taskIndex].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[taskIndex]);
                    } else if (command.startsWith("unmark ")) {
                        int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                        tasks[taskIndex].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[taskIndex]);
                    } else if (command.equals("todo") || command.startsWith("todo ")) {
                        String description = command.substring(4).trim();
                        if (description.isEmpty()) {
                            throw new TrackieException("Oops! A todo needs a description.");
                        }
                        Task task = new Todo(description);
                        taskCount = addTask(tasks, taskCount, task);
                    } else if (command.startsWith("deadline ")) {
                        int byIndex = command.indexOf(" /by ");
                        String description = command.substring(9, byIndex);
                        String by = command.substring(byIndex + 5);
                        Task task = new Deadline(description, by);
                        taskCount = addTask(tasks, taskCount, task);
                    } else if (command.startsWith("event ")) {
                        int fromIndex = command.indexOf(" /from ");
                        int toIndex = command.indexOf(" /to ");
                        String description = command.substring(6, fromIndex);
                        String from = command.substring(fromIndex + 7, toIndex);
                        String to = command.substring(toIndex + 5);
                        Task task = new Event(description, from, to);
                        taskCount = addTask(tasks, taskCount, task);
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
     * Stores a task, displays confirmation, and returns the updated task count.
     *
     * @param tasks array in which tasks are stored
     * @param taskCount number of tasks before the addition
     * @param task task to add
     * @return number of tasks after the addition
     */
    private static int addTask(Task[] tasks, int taskCount, Task task) {
        tasks[taskCount] = task;
        int updatedTaskCount = taskCount + 1;
        String taskWord = updatedTaskCount == 1 ? "task" : "tasks";

        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + updatedTaskCount + " " + taskWord + " in the list.");
        return updatedTaskCount;
    }
}
