package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_EMAIL,
                        PREFIX_ADDRESS, PREFIX_SUBJECT, PREFIX_DAY,
                        PREFIX_TIME, PREFIX_EMERGENCY_CONTACT,
                        PREFIX_PAYMENT_STATUS, PREFIX_REMARK, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT,
                    EditCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_EMAIL,
                PREFIX_ADDRESS, PREFIX_EMERGENCY_CONTACT,
                PREFIX_PAYMENT_STATUS, PREFIX_REMARK);

        EditPersonDescriptor editPersonDescriptor =
                new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(
                    argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(
                    argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editPersonDescriptor.setAddress(ParserUtil.parseAddress(
                    argMultimap.getValue(PREFIX_ADDRESS).get()));
        }

        // Handle lesson slots (s/, d/, ti/ must all be present together)
        List<String> rawSubjects = argMultimap.getAllValues(PREFIX_SUBJECT);
        List<String> rawDays = argMultimap.getAllValues(PREFIX_DAY);
        List<String> rawTimes = argMultimap.getAllValues(PREFIX_TIME);

        boolean hasSubjects = !rawSubjects.isEmpty();
        boolean hasDays = !rawDays.isEmpty();
        boolean hasTimes = !rawTimes.isEmpty();

        if (hasSubjects || hasDays || hasTimes) {
            // Check if clearing all three (all empty values)
            boolean clearingSubjects = hasSubjects && rawSubjects.size() == 1
                    && rawSubjects.get(0).isEmpty();
            boolean clearingDays = hasDays && rawDays.size() == 1
                    && rawDays.get(0).isEmpty();
            boolean clearingTimes = hasTimes && rawTimes.size() == 1
                    && rawTimes.get(0).isEmpty();

            if (clearingSubjects && clearingDays && clearingTimes) {
                // Clear all lesson slots
                editPersonDescriptor.setLessonSlots(List.of());
            } else if (hasSubjects != hasDays || hasDays != hasTimes) {
                throw new ParseException(MESSAGE_LESSON_SLOT_INCOMPLETE);
            } else {
                if (rawSubjects.size() != rawDays.size()
                        || rawDays.size() != rawTimes.size()) {
                    throw new ParseException(String.format(
                            MESSAGE_SUBJECT_DAY_TIME_MISMATCH,
                            rawSubjects.size(), rawDays.size(), rawTimes.size()));
                }
                List<LessonSlot> lessonSlots = ParserUtil.parseLessonSlots(
                        rawSubjects, rawDays, rawTimes);
                editPersonDescriptor.setLessonSlots(lessonSlots);
            }
        }

        if (argMultimap.getValue(PREFIX_EMERGENCY_CONTACT).isPresent()) {
            editPersonDescriptor.setEmergencyContact(
                    ParserUtil.parseEmergencyContact(
                            argMultimap.getValue(
                                    PREFIX_EMERGENCY_CONTACT).get()));
        }
        if (argMultimap.getValue(PREFIX_PAYMENT_STATUS).isPresent()) {
            editPersonDescriptor.setPaymentStatus(
                    ParserUtil.parsePaymentStatus(
                            argMultimap.getValue(
                                    PREFIX_PAYMENT_STATUS).get()));
        }
        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            editPersonDescriptor.setRemark(
                    new Remark(argMultimap.getValue(PREFIX_REMARK).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(
            Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1
                && tags.contains("")
                ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
