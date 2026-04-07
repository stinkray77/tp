package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ListAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListAttendanceCommand object.
 */
public class ListAttendanceCommandParser implements Parser<ListAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListAttendanceCommand
     * and returns a ListAttendanceCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ListAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SUBJECT);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAttendanceCommand.MESSAGE_USAGE), pe);
        }

        String subject = null;
        if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
            subject = ParserUtil.parseSubject(argMultimap.getValue(PREFIX_SUBJECT).get()).subjectName;
        }

        return new ListAttendanceCommand(index, subject);
    }
}
