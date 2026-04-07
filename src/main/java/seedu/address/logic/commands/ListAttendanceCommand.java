package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Lists attendance records for a student identified using the displayed index.
 * If a subject filter is provided, only attendance records for the matching subject are shown.
 * The command returns a formatted text summary in the result display.
 */
public class ListAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "listattendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists attendance records of the student identified by the index number used in the displayed "
            + "student list.\n"
            + "Parameters: INDEX (must be a positive integer) [s/SUBJECT]\n"
            + "Example: " + COMMAND_WORD + " 1 s/Mathematics";

    public static final String MESSAGE_NO_ATTENDANCE_RECORDS =
            "No attendance records found for %1$s";
    public static final String MESSAGE_NO_ATTENDANCE_RECORDS_FOR_SUBJECT =
            "No attendance records found for %1$s in %2$s";

    private final Index targetIndex;
    private final String subject;

    /**
     * Creates a command to list attendance records for the student at the given index.
     *
     * @param targetIndex Index of the student in the currently displayed list.
     * @param subject Optional subject filter. If non-null, only matching subject attendance is shown.
     */
    public ListAttendanceCommand(Index targetIndex, String subject) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.subject = subject;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null; // Placeholder
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListAttendanceCommand)) {
            return false;
        }

        ListAttendanceCommand otherListAttendanceCommand = (ListAttendanceCommand) other;
        return targetIndex.equals(otherListAttendanceCommand.targetIndex)
                && Objects.equals(subject, otherListAttendanceCommand.subject);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("subject", subject)
                .toString();
    }
}
