package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ATTENDANCE_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.AttendanceStatus;

/**
 * Parses input arguments and creates a new MarkAttendanceCommand object.
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkAttendanceCommand
     * and returns a MarkAttendanceCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_SUBJECT, PREFIX_DAY, PREFIX_TIME, PREFIX_ATTENDANCE_STATUS);

        if (!arePrefixesPresent(argMultimap, PREFIX_SUBJECT, PREFIX_DAY, PREFIX_TIME, PREFIX_ATTENDANCE_STATUS)
                || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SUBJECT, PREFIX_DAY, PREFIX_TIME, PREFIX_ATTENDANCE_STATUS);

        Index[] indices;
        try {
            String preamble = argMultimap.getPreamble();
            if (preamble.contains(",")) {
                // Multiple indices separated by commas
                String[] indexStrings = preamble.split(",");
                indices = new Index[indexStrings.length];
                for (int i = 0; i < indexStrings.length; i++) {
                    indices[i] = ParserUtil.parseIndex(indexStrings[i].trim());
                }
            } else {
                // Single index
                indices = new Index[]{ParserUtil.parseIndex(preamble)};
            }
        } catch (ParseException pe) {
            throw new ParseException(
                    "Invalid index: " + pe.getMessage() + "\n" + MarkAttendanceCommand.MESSAGE_USAGE, pe);
        }

        String subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get()).subjectName;
        String day = ParserUtil.parseDay(argMultimap.getValue(PREFIX_DAY).get()).dayName;
        String time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get()).timeValue;
        AttendanceStatus status = ParserUtil.parseAttendanceStatus(
                argMultimap.getValue(PREFIX_ATTENDANCE_STATUS).get());

        return new MarkAttendanceCommand(indices, subject, day, time, status);
    }

    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
