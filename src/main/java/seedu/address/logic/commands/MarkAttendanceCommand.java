package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Marks the attendance of a student for a specific subject and lesson.
 */
public class MarkAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "markattendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the attendance of the student identified "
            + "by the index number used in the displayed student list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_LESSON + "LESSON "
            + PREFIX_ATTENDANCE_STATUS + "STATUS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_LESSON + "Lesson 1 "
            + PREFIX_ATTENDANCE_STATUS + "Present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS =
            "Marked attendance for %1$s: %2$s - %3$s as %4$s";

    public static final String MESSAGE_SUBJECT_NOT_FOUND =
            "The student does not take the subject: %1$s";

    private final Index targetIndex;
    private final String subject;
    private final String lesson;
    private final AttendanceStatus status;

    /**
     * @param targetIndex of the person in the filtered person list
     * @param subject     the subject name to mark attendance for
     * @param lesson      the lesson name to mark attendance for
     * @param status      the attendance status to record
     */
    public MarkAttendanceCommand(Index targetIndex, String subject, String lesson, AttendanceStatus status) {
        requireNonNull(targetIndex);
        requireNonNull(subject);
        requireNonNull(lesson);
        requireNonNull(status);
        this.targetIndex = targetIndex;
        this.subject = subject;
        this.lesson = lesson;
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

        Person markedPerson = personToMark.markAttendance(matchedSubject, lesson, status);
        model.setPerson(personToMark, markedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                personToMark.getName(), matchedSubject, lesson, status.value));
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
                && lesson.equals(otherCmd.lesson)
                && status == otherCmd.status;
    }
}
