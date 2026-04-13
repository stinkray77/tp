package seedu.address.logic;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_SUBJECT_DAY_TIME_MISMATCH =
                "Number of subjects, days and times must all match. "
                + "Got %1$d subject(s), %2$d day(s) and %3$d time(s).";
    public static final String MESSAGE_LESSON_SLOT_INCOMPLETE =
                "Subjects, days and times must all be specified together. "
                + "If you provide any of s/, d/, ti/, you must provide all three.";
    public static final String MESSAGE_LESSON_SLOT_NOT_FOUND =
                "Student does not have a lesson for %1$s on %2$s at %3$s";

    private static final Map<Prefix, String> PREFIX_LABELS = Map.ofEntries(
            Map.entry(PREFIX_NAME, "Name"),
            Map.entry(PREFIX_EMAIL, "Email"),
            Map.entry(PREFIX_ADDRESS, "Address"),
            Map.entry(PREFIX_EMERGENCY_CONTACT, "Emergency Contact"),
            Map.entry(PREFIX_SUBJECT, "Subject"),
            Map.entry(PREFIX_DAY, "Day"),
            Map.entry(PREFIX_TIME, "Time"),
            Map.entry(PREFIX_PAYMENT_STATUS, "Payment Status"),
            Map.entry(PREFIX_TAG, "Tag"),
            Map.entry(PREFIX_REMARK, "Remark"),
            Map.entry(PREFIX_ATTENDANCE_STATUS, "Attendance Status"));

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields = Stream.of(duplicatePrefixes)
                .map(p -> PREFIX_LABELS.getOrDefault(p, p.toString()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return MESSAGE_DUPLICATE_FIELDS + String.join(", ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Lessons: ");
        person.getLessonSlots().forEach(ls -> builder.append("[").append(ls).append("] "));
        builder.append("; Remark: ")
                .append(person.getRemark());
        builder.append("; Emergency Contact: ")
                .append(person.getEmergencyContact())
                .append("; Payment Status: ")
                .append(person.getPaymentStatus())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
