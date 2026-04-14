package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_LESSON_SLOT_INCOMPLETE;
import static seedu.address.logic.Messages.MESSAGE_SUBJECT_DAY_TIME_MISMATCH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SUBJECT, PREFIX_DAY,
                        PREFIX_TIME, PREFIX_EMERGENCY_CONTACT,
                        PREFIX_PAYMENT_STATUS, PREFIX_REMARK, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS,
                PREFIX_EMAIL, PREFIX_EMERGENCY_CONTACT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_EMERGENCY_CONTACT,
                PREFIX_PAYMENT_STATUS);

        Name name = ParserUtil.parseName(
                argMultimap.getValue(PREFIX_NAME).get());
        Email email = ParserUtil.parseEmail(
                argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(
                argMultimap.getValue(PREFIX_ADDRESS).get());

        List<String> rawSubjects = argMultimap.getAllValues(PREFIX_SUBJECT);
        List<String> rawDays = argMultimap.getAllValues(PREFIX_DAY);
        List<String> rawTimes = argMultimap.getAllValues(PREFIX_TIME);

        boolean hasSubjects = !rawSubjects.isEmpty();
        boolean hasDays = !rawDays.isEmpty();
        boolean hasTimes = !rawTimes.isEmpty();

        // All three must be present together, or all absent
        if (hasSubjects != hasDays || hasDays != hasTimes) {
            throw new ParseException(MESSAGE_LESSON_SLOT_INCOMPLETE);
        }

        List<LessonSlot> lessonSlots;
        if (hasSubjects) {
            if (rawSubjects.size() != rawDays.size()
                    || rawDays.size() != rawTimes.size()) {
                throw new ParseException(String.format(
                        MESSAGE_SUBJECT_DAY_TIME_MISMATCH,
                        rawSubjects.size(), rawDays.size(), rawTimes.size()));
            }
            lessonSlots = ParserUtil.parseLessonSlots(rawSubjects, rawDays, rawTimes);
        } else {
            lessonSlots = List.of();
        }

        EmergencyContact emergencyContact =
                ParserUtil.parseEmergencyContact(
                        argMultimap.getValue(PREFIX_EMERGENCY_CONTACT).get());
        PaymentStatus paymentStatus = argMultimap
                .getValue(PREFIX_PAYMENT_STATUS).isPresent()
                ? ParserUtil.parsePaymentStatus(
                        argMultimap.getValue(PREFIX_PAYMENT_STATUS).get())
                : new PaymentStatus("Due");
        Set<Tag> tagList = ParserUtil.parseTags(
                argMultimap.getAllValues(PREFIX_TAG));

        Remark remark = argMultimap.getValue(PREFIX_REMARK).isPresent()
                ? new Remark(argMultimap.getValue(PREFIX_REMARK).get().trim())
                : new Remark("");
        Person person = new Person(name, email, address, lessonSlots,
                emergencyContact, paymentStatus, remark, tagList);

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(
            ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(
                prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
