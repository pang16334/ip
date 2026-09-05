package trackie.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import trackie.exception.TrackieException;
import trackie.task.Task;

/** Tests command parsing and validation. */
public class ParserTest {
    @Test
    public void parseTaskIndex_validOneBasedNumber_returnsZeroBasedIndex() throws TrackieException {
        assertEquals(1, Parser.parseTaskIndex("mark 2", "mark", 3));
    }

    @Test
    public void parseTaskIndex_outOfRangeNumber_throwsTrackieException() {
        TrackieException exception = assertThrows(TrackieException.class, () -> Parser.parseTaskIndex(
                "delete 4", "delete", 3));

        assertEquals("Oops! Choose a task number between 1 and 3.", exception.getMessage());
    }

    @Test
    public void parseDeadline_validIsoDate_returnsFormattedDeadline() throws TrackieException {
        Task deadline = Parser.parseDeadline("deadline submit report /by 2026-09-15");

        assertEquals("[D][ ] submit report (by: Sep 15 2026)", deadline.toString());
    }

    @Test
    public void parseDeadline_invalidDate_throwsTrackieException() {
        TrackieException exception = assertThrows(TrackieException.class, () -> Parser.parseDeadline(
                "deadline submit report /by 15-09-2026"));

        assertEquals("Oops! Use a deadline date in yyyy-MM-dd format.", exception.getMessage());
    }
}
