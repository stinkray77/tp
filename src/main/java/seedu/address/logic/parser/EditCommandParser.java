package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_DAY_TIME_INCOMPLETE;
import static seedu.address.logic.Messages.MESSAGE_DAY_TIME_MISMATCH;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Day;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
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
                        PREFIX_PAYMENT_STATUS, PREFIX_TAG);

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
                PREFIX_PAYMENT_STATUS);

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
        parseSubjectsForEdit(argMultimap.getAllValues(PREFIX_SUBJECT))
                .ifPresent(editPersonDescriptor::setSubjects);
        Optional<Set<Day>> parsedDays =
                parseDaysForEdit(argMultimap.getAllValues(PREFIX_DAY));
        Optional<Set<Time>> parsedTimes =
                parseTimesForEdit(argMultimap.getAllValues(PREFIX_TIME));

        if (parsedDays.isPresent() != parsedTimes.isPresent()) {
            throw new ParseException(MESSAGE_DAY_TIME_INCOMPLETE);
        }
        if (parsedDays.isPresent() && parsedTimes.isPresent()
                && parsedDays.get().size() != parsedTimes.get().size()) {
            throw new ParseException(String.format(
                    MESSAGE_DAY_TIME_MISMATCH,
                    parsedDays.get().size(), parsedTimes.get().size()));
        }

        parsedDays.ifPresent(editPersonDescriptor::setDays);
        parsedTimes.ifPresent(editPersonDescriptor::setTimes);
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
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editPersonDescriptor::setTags);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> subjects} into a {@code Set<Subject>}
     * if {@code subjects} is non-empty.
     */
    private Optional<Set<Subject>> parseSubjectsForEdit(
            Collection<String> subjects) throws ParseException {
        assert subjects != null;

        if (subjects.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> subjectSet = subjects.size() == 1
                && subjects.contains("")
                ? Collections.emptySet() : subjects;
        return Optional.of(ParserUtil.parseSubjects(subjectSet));
    }

    /**
     * Parses {@code Collection<String> days} into a {@code Set<Day>}
     * if {@code days} is non-empty.
     */
    private Optional<Set<Day>> parseDaysForEdit(
            Collection<String> days) throws ParseException {
        assert days != null;

        if (days.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> daySet = days.size() == 1
                && days.contains("")
                ? Collections.emptySet() : days;
        return Optional.of(ParserUtil.parseDays(daySet));
    }

    /**
     * Parses {@code Collection<String> times} into a {@code Set<Time>}
     * if {@code times} is non-empty.
     */
    private Optional<Set<Time>> parseTimesForEdit(
            Collection<String> times) throws ParseException {
        assert times != null;

        if (times.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> timeSet = times.size() == 1
                && times.contains("")
                ? Collections.emptySet() : times;
        return Optional.of(ParserUtil.parseTimes(timeSet));
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
