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
                } else if (command.startsWith("todo ")) {
                    Task task = new Todo(command.substring(5));
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (command.startsWith("deadline ")) {
                    int byIndex = command.indexOf(" /by ");
                    String description = command.substring(9, byIndex);
                    String by = command.substring(byIndex + 5);
                    Task task = new Deadline(description, by);
                    tasks[taskCount] = task;
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + task);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else {
                    tasks[taskCount] = new Task(command);
                    taskCount++;
                    System.out.println("Added: " + command);
                }
            }
        }
    }
}
