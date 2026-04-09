package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Marks the attendance of a student for a specific subject, day, and time.
 */
public class MarkAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "markattendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the attendance of the student identified "
            + "by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_DAY + "DAY "
            + PREFIX_TIME + "TIME "
            + PREFIX_ATTENDANCE_STATUS + "STATUS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME + "1400 "
            + PREFIX_ATTENDANCE_STATUS + "Present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS =
            "Marked attendance for %1$s: %2$s - %3$s %4$s as %5$s";

    public static final String MESSAGE_SUBJECT_NOT_FOUND =
            "The student does not take the subject: %1$s";

    private final Index targetIndex;
    private final String subject;
    private final String day;
    private final String time;
    private final AttendanceStatus status;

    /**
     * @param targetIndex of the person in the filtered person list
     * @param subject     the subject name to mark attendance for
     * @param day         the day of the lesson
     * @param time        the time of the lesson
     * @param status      the attendance status to record
     */
    public MarkAttendanceCommand(Index targetIndex, String subject,
            String day, String time, AttendanceStatus status) {
        requireNonNull(targetIndex);
        requireNonNull(subject);
        requireNonNull(day);
        requireNonNull(time);
        requireNonNull(status);
        this.targetIndex = targetIndex;
        this.subject = subject;
        this.day = day;
        this.time = time;
        this.status = status;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());

        String matchedSubject = personToMark.getSubjects().stream()
                .map(s -> s.subjectName)
                .filter(s -> s.equalsIgnoreCase(subject))
                .findFirst()
                .orElseThrow(() -> new CommandException(
                        String.format(MESSAGE_SUBJECT_NOT_FOUND, subject)));

        String dayTime = day + " " + time;

        if (!personToMark.hasLessonSlot(matchedSubject, dayTime)) {
            throw new CommandException(
                    String.format(Messages.MESSAGE_LESSON_SLOT_NOT_FOUND,
                            matchedSubject, day, time));
        }

        Person markedPerson = personToMark.markAttendance(matchedSubject, dayTime, status);
        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), matchedSubject, day, time, status.value));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkAttendanceCommand otherCmd)) {
            return false;
        }
        return targetIndex.equals(otherCmd.targetIndex)
                && subject.equals(otherCmd.subject)
                && day.equals(otherCmd.day)
                && time.equals(otherCmd.time)
                && status == otherCmd.status;
    }
}
