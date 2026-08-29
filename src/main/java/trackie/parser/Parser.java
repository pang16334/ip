package trackie.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import trackie.exception.TrackieException;
import trackie.task.Deadline;
import trackie.task.Event;
import trackie.task.Task;
import trackie.task.Todo;

/** Converts user commands into validated task data. */
public class Parser {
    private Parser() {
    }

    /**
     * Extracts the first word that identifies a command.
     *
     * @param command full command entered by the user
     * @return command word, or an empty string for blank input
     */
    public static String getCommandWord(String command) {
        String trimmedCommand = command.trim();
        int firstSpace = trimmedCommand.indexOf(' ');
        return firstSpace < 0 ? trimmedCommand : trimmedCommand.substring(0, firstSpace);
    }

    /**
     * Parses and validates the task number in a mark, unmark, or delete command.
     *
     * @param command full command entered by the user
     * @param commandName name of the command being parsed
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws TrackieException if the number is missing, invalid, or out of range
     */
    public static int parseTaskIndex(String command, String commandName, int taskCount)
            throws TrackieException {
        if (taskCount == 0) {
            throw new TrackieException("Oops! There are no tasks to " + commandName + ".");
        }
        String numberText = command.substring(commandName.length()).trim();
        if (numberText.isEmpty()) {
            throw new TrackieException("Oops! Please specify a task number after " + commandName + ".");
        }
        try {
            int taskNumber = Integer.parseInt(numberText);
            if (taskNumber < 1 || taskNumber > taskCount) {
                throw new TrackieException("Oops! Choose a task number between 1 and " + taskCount + ".");
            }
            return taskNumber - 1;
        } catch (NumberFormatException exception) {
            throw new TrackieException("Oops! The task number must be a whole number.");
        }
    }

    /**
     * Parses and validates a todo command.
     *
     * @param command full todo command
     * @return todo represented by the command
     * @throws TrackieException if the description is missing
     */
    public static Task parseTodo(String command) throws TrackieException {
        String description = command.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new TrackieException("Oops! A todo needs a description.");
        }
        return new Todo(description);
    }

    /**
     * Parses and validates a deadline command and its ISO date.
     *
     * @param command full deadline command
     * @return deadline represented by the command
     * @throws TrackieException if required data is missing or the date is invalid
     */
    public static Task parseDeadline(String command) throws TrackieException {
        int byIndex = command.indexOf(" /by");
        if (byIndex < 0) {
            throw new TrackieException("Oops! Use: deadline DESCRIPTION /by TIME");
        }
        String description = command.substring("deadline".length(), byIndex).trim();
        String by = command.substring(byIndex + " /by".length()).trim();
        if (description.isEmpty()) {
            throw new TrackieException("Oops! A deadline needs a description.");
        }
        if (by.isEmpty()) {
            throw new TrackieException("Oops! A deadline needs a due date or time after /by.");
        }
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new TrackieException("Oops! Use a deadline date in yyyy-MM-dd format.");
        }
    }

    /**
     * Parses and validates an event command and its two ISO dates.
     *
     * @param command full event command
     * @return event represented by the command
     * @throws TrackieException if required data is missing or either date is invalid
     */
    public static Task parseEvent(String command) throws TrackieException {
        int fromIndex = command.indexOf(" /from");
        int toIndex = command.indexOf(" /to");
        if (fromIndex < 0 || toIndex < 0 || toIndex < fromIndex) {
            throw new TrackieException("Oops! Use: event DESCRIPTION /from START /to END");
        }
        String description = command.substring("event".length(), fromIndex).trim();
        String from = command.substring(fromIndex + " /from".length(), toIndex).trim();
        String to = command.substring(toIndex + " /to".length()).trim();
        if (description.isEmpty()) {
            throw new TrackieException("Oops! An event needs a description.");
        }
        if (from.isEmpty()) {
            throw new TrackieException("Oops! An event needs a start after /from.");
        }
        if (to.isEmpty()) {
            throw new TrackieException("Oops! An event needs an end after /to.");
        }
        try {
            return new Event(description, LocalDate.parse(from), LocalDate.parse(to));
        } catch (DateTimeParseException exception) {
            throw new TrackieException("Oops! Use event dates in yyyy-MM-dd format.");
        }
    }
}
