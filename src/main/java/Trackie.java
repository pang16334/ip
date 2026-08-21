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

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String command = scanner.nextLine();

                if (command.equals("bye")) {
                    System.out.println("Bye! Consistency is the key. Hope to see you again soon!");
                    break;
                }

                System.out.println(command);
            }
        }
    }
}
