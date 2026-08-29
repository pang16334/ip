import java.util.Scanner;

/** Reads user commands and displays Trackie's responses. */
public class Ui implements AutoCloseable {
    private static final String BANNER = " _______             _    _\n"
            + "|__   __|           | |  (_)\n"
            + "   | |_ __ __ _  ___| | ___  ___\n"
            + "   | | '__/ _` |/ __| |/ / |/ _ \\\n"
            + "   | | | | (_| | (__|   <| |  __/\n"
            + "   |_|_|  \\__,_|\\___|_|\\_\\_|\\___|\n";
    private final Scanner scanner = new Scanner(System.in);

    /** Displays Trackie's greeting. */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm Trackie.");
        System.out.println("What can I do for you today?");
    }

    /** @return whether another command is available */
    public boolean hasNextCommand() {
        return this.scanner.hasNextLine();
    }

    /** @return the next command entered by the user */
    public String readCommand() {
        return this.scanner.nextLine();
    }

    /** Displays all tasks with one-based numbering. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays a successful mark operation. */
    public void showMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /** Displays a successful unmark operation. */
    public void showUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Displays a successful task removal. */
    public void showDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Displays a successful task addition. */
    public void showAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Displays an error without exposing implementation details. */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays Trackie's farewell. */
    public void showGoodbye() {
        System.out.println("Bye! Consistency is the key. Hope to see you again soon!");
    }

    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }

    /** Releases the input scanner when Trackie exits. */
    @Override
    public void close() {
        this.scanner.close();
    }
}
