package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LESSON;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.AttendanceRecordKey;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.Person;

/**
 * Marks the attendance of a student for a specific subject, day, and time.
 */
public class MarkAttendanceCommand extends Command {

    public static final String COMMAND_WORD = "markattendance";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the attendance of student(s) identified "
            + "by the index number(s) used in the displayed student list.\n"
            + "Parameters: INDEX[|INDEX1,INDEX2,...] (must be positive integer(s)) "
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_DAY + "DAY "
            + PREFIX_TIME + "TIME "
            + PREFIX_LESSON + "LESSON "
            + PREFIX_ATTENDANCE_STATUS + "STATUS\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME + "1400 "
            + PREFIX_LESSON + "2026-04-13 Algebra Lesson 2 "
            + PREFIX_ATTENDANCE_STATUS + "Present\n"
            + "Example: " + COMMAND_WORD + " 1,2,3 "
            + PREFIX_SUBJECT + "Mathematics "
            + PREFIX_DAY + "Monday "
            + PREFIX_TIME + "1400 "
            + PREFIX_LESSON + "Week 1 "
            + PREFIX_ATTENDANCE_STATUS + "Present";

    public static final String MESSAGE_MARK_ATTENDANCE_SUCCESS =
            "Marked attendance for %1$s: %2$s - %3$s %4$s (%5$s) as %6$s";

    public static final String MESSAGE_MARK_ATTENDANCE_MULTIPLE_SUCCESS =
            "Marked attendance for %1$d students in %2$s - %3$s %4$s (%5$s) as %6$s";

    public static final String MESSAGE_SUBJECT_NOT_FOUND =
            "The student does not take the subject: %1$s";

    private final Index[] targetIndices;
    private final String subject;
    private final String day;
    private final String time;
    private final String lesson;
    private final AttendanceStatus status;

    /**
     * @param targetIndices of the person(s) in the filtered person list
     * @param subject     the subject name to mark attendance for
     * @param day         the day of the lesson
     * @param time        the time of the lesson
     * @param lesson      the lesson label to distinguish different sessions
     * @param status      the attendance status to record
     */
    public MarkAttendanceCommand(Index[] targetIndices, String subject,
            String day, String time, String lesson, AttendanceStatus status) {
        requireNonNull(targetIndices);
        requireNonNull(subject);
        requireNonNull(day);
        requireNonNull(time);
        requireNonNull(lesson);
        requireNonNull(status);

        this.targetIndices = targetIndices;
        this.subject = subject;
        this.day = day;
        this.time = time;
        this.lesson = lesson;
        this.status = status;
    }

    /**
     * Convenience constructor for single student (backward compatibility)
     */
    public MarkAttendanceCommand(Index targetIndex, String subject,
            String day, String time, String lesson, AttendanceStatus status) {
        this(new Index[]{targetIndex}, subject, day, time, lesson, status);
    }

    @Override
    public boolean isMutating() {
        return true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Validate all indices are in range
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Get all persons to mark attendance for
        Person[] personsToMark = java.util.Arrays.stream(targetIndices)
                .map(index -> lastShownList.get(index.getZeroBased()))
                .toArray(Person[]::new);

        // Mark attendance for each person
        for (Person personToMark : personsToMark) {
            String matchedSubject = personToMark.getSubjects().stream()
                    .map(s -> s.subjectName)
                    .filter(s -> s.equalsIgnoreCase(subject))
                    .findFirst()
                    .orElseThrow(() -> new CommandException(
                            String.format(MESSAGE_SUBJECT_NOT_FOUND, subject)));

            String dayTime = day + " " + time;
            String attendanceKey = AttendanceRecordKey.of(day, time, lesson);

            if (!personToMark.hasLessonSlot(matchedSubject, dayTime)) {
                throw new CommandException(
                        String.format(Messages.MESSAGE_LESSON_SLOT_NOT_FOUND,
                                matchedSubject, day, time));
            }

            Person markedPerson = personToMark.markAttendance(matchedSubject, attendanceKey, status);
            model.setPerson(personToMark, markedPerson);
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // Return appropriate success message
        String successMessage = targetIndices.length == 1
                ? String.format(MESSAGE_MARK_ATTENDANCE_SUCCESS,
                        personsToMark[0].getName(), subject, day, time, lesson, status.value)
                : String.format(MESSAGE_MARK_ATTENDANCE_MULTIPLE_SUCCESS,
                        targetIndices.length, subject, day, time, lesson, status.value);

        return new CommandResult(successMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof MarkAttendanceCommand otherCmd)) {
            return false;
        }
        return java.util.Arrays.equals(targetIndices, otherCmd.targetIndices)
                && subject.equals(otherCmd.subject)
                && day.equals(otherCmd.day)
                && time.equals(otherCmd.time)
                && lesson.equals(otherCmd.lesson)
                && status.equals(otherCmd.status);
    }
}
