package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + person.getAddress().value + " ");
        person.getLessonSlots().forEach(ls -> {
            sb.append(PREFIX_SUBJECT + ls.getSubject().subjectName + " ");
            sb.append(PREFIX_DAY + ls.getDay().dayName + " ");
            sb.append(PREFIX_TIME + ls.getTime().timeValue + " ");
        });
        sb.append(PREFIX_EMERGENCY_CONTACT
                + person.getEmergencyContact().value + " ");
        sb.append(PREFIX_PAYMENT_STATUS
                + person.getPaymentStatus().value + " ");
        person.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(
            EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME)
                .append(name.fullName).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL)
                .append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address
                -> sb.append(PREFIX_ADDRESS)
                .append(address.value).append(" "));
        if (descriptor.getLessonSlots().isPresent()) {
            List<LessonSlot> lessonSlots = descriptor.getLessonSlots().get();
            if (lessonSlots.isEmpty()) {
                sb.append(PREFIX_SUBJECT).append(" ");
                sb.append(PREFIX_DAY).append(" ");
                sb.append(PREFIX_TIME).append(" ");
            } else {
                lessonSlots.forEach(ls -> {
                    sb.append(PREFIX_SUBJECT).append(ls.getSubject().subjectName).append(" ");
                    sb.append(PREFIX_DAY).append(ls.getDay().dayName).append(" ");
                    sb.append(PREFIX_TIME).append(ls.getTime().timeValue).append(" ");
                });
            }
        }
        descriptor.getEmergencyContact().ifPresent(ec
                -> sb.append(PREFIX_EMERGENCY_CONTACT)
                .append(ec.value).append(" "));
        descriptor.getPaymentStatus().ifPresent(ps
                -> sb.append(PREFIX_PAYMENT_STATUS)
                .append(ps.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG)
                        .append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
