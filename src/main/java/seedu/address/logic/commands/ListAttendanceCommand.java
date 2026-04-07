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

    /**
     * Retrieves the target student from the filtered person list and formats the student's
     * attendance records for display. If the student has no attendance records, or if the
     * requested subject has no matching records, a no-records message is returned instead.
     *
     * @param model Model containing the currently displayed student list.
     * @return A {@code CommandResult} containing formatted attendance information or a
     *         no-records message.
     * @throws CommandException If the target index is out of range of the displayed student list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToList = lastShownList.get(targetIndex.getZeroBased());
        Map<String, Map<String, AttendanceStatus>> attendanceRecords = personToList.getAttendanceRecords();

        if (attendanceRecords.isEmpty()) {
            return new CommandResult(String.format(MESSAGE_NO_ATTENDANCE_RECORDS, personToList.getName()));
        }

        String filteredSubject = findMatchingSubject(attendanceRecords);
        if (subject != null && filteredSubject == null) {
            return new CommandResult(String.format(
                    MESSAGE_NO_ATTENDANCE_RECORDS_FOR_SUBJECT, personToList.getName(), subject));
        }

        String formattedAttendance = formatAttendanceRecords(personToList, attendanceRecords, filteredSubject);
        return new CommandResult(formattedAttendance);
    }

    /**
     * Returns the stored subject key that matches the requested subject filter, preserving the
     * original subject casing used in the attendance records.
     *
     * @param attendanceRecords Attendance records grouped by subject name.
     * @return The matching stored subject key, or {@code null} if no subject filter was provided
     *         or no matching subject exists.
     */
    private String findMatchingSubject(Map<String, Map<String, AttendanceStatus>> attendanceRecords) {
        if (subject == null) {
            return null;
        }

        return attendanceRecords.keySet().stream()
                .filter(recordedSubject -> recordedSubject.equalsIgnoreCase(subject))
                .findFirst()
                .orElse(null);
    }

    /**
     * Formats attendance records into a readable text block grouped by subject.
     * If a subject filter is applied, only that subject section is included.
     *
     * @param person Student whose attendance records are being formatted.
     * @param attendanceRecords Attendance records grouped by subject and lesson.
     * @param filteredSubject Matching stored subject key if a subject filter was applied, or
     *                        {@code null} if all subjects should be included.
     * @return A formatted text block suitable for display in the command result area.
     */
    private String formatAttendanceRecords(Person person, Map<String, Map<String, AttendanceStatus>> attendanceRecords,
                                           String filteredSubject) {
        StringBuilder builder = new StringBuilder();
        builder.append("Attendance for ").append(person.getName());

        if (filteredSubject != null) {
            appendSubjectAttendance(builder, filteredSubject, attendanceRecords.get(filteredSubject));
            return builder.toString();
        }

        attendanceRecords.forEach((recordedSubject, lessons) ->
                appendSubjectAttendance(builder, recordedSubject, lessons));
        return builder.toString();
    }

    /**
     * Appends one subject section to the formatted attendance output. Lesson order follows the
     * underlying map iteration order stored on the {@code Person}.
     *
     * @param builder String builder accumulating the formatted attendance output.
     * @param recordedSubject Stored subject name to display as the section heading.
     * @param lessons Attendance records for the subject, keyed by lesson name.
     */
    private void appendSubjectAttendance(StringBuilder builder, String recordedSubject,
                                         Map<String, AttendanceStatus> lessons) {
        builder.append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(recordedSubject);
        lessons.forEach((lessonName, attendanceStatus) ->
                builder.append(System.lineSeparator())
                        .append("  ")
                        .append(lessonName)
                        .append(": ")
                        .append(attendanceStatus.value));
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
