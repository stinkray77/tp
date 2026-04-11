package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.DayMatchesPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PaymentStatusMatchesPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.SubjectContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NAME, PREFIX_SUBJECT, PREFIX_DAY,
                PREFIX_PAYMENT_STATUS, PREFIX_TAG);

        if (argMultimap.getPreamble().contains("/")) {
            throw new ParseException(
                    "Malformed prefix detected. Did you add a space before '/'? "
                    + FindCommand.MESSAGE_USAGE);
        }

        // Check if any prefixes are present
        boolean hasPrefixes = argMultimap.getValue(PREFIX_NAME).isPresent()
                || argMultimap.getValue(PREFIX_SUBJECT).isPresent()
                || argMultimap.getValue(PREFIX_DAY).isPresent()
                || argMultimap.getValue(PREFIX_PAYMENT_STATUS).isPresent()
                || argMultimap.getValue(PREFIX_TAG).isPresent();

        if (!hasPrefixes) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        } else {
            if (!argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            // Prefix-based search: combine predicates with AND logic
            Predicate<Person> combinedPredicate = person -> true;
            int predicateCount = 0;

            List<String> nameKeywords = getAllKeywords(argMultimap, PREFIX_NAME);
            if (!nameKeywords.isEmpty()) {
                combinedPredicate = combinedPredicate.and(
                    new NameContainsKeywordsPredicate(nameKeywords)
                );
                predicateCount++;
            }

            List<String> subjectKeywords = getAllKeywords(argMultimap, PREFIX_SUBJECT);
            if (!subjectKeywords.isEmpty()) {
                combinedPredicate = combinedPredicate.and(
                    new SubjectContainsKeywordsPredicate(subjectKeywords)
                );
                predicateCount++;
            }

            List<String> dayKeywords = getAllValidatedDays(argMultimap);
            if (!dayKeywords.isEmpty()) {
                combinedPredicate = combinedPredicate.and(new DayMatchesPredicate(dayKeywords));
                predicateCount++;
            }

            List<String> paymentStatuses = getAllValidatedPaymentStatuses(argMultimap);
            if (!paymentStatuses.isEmpty()) {
                combinedPredicate = combinedPredicate.and(new PaymentStatusMatchesPredicate(paymentStatuses));
                predicateCount++;
            }

            List<String> tagKeywords = getAllKeywords(argMultimap, PREFIX_TAG);
            if (!tagKeywords.isEmpty()) {
                combinedPredicate = combinedPredicate.and(new TagContainsKeywordsPredicate(tagKeywords));
                predicateCount++;
            }

            if (predicateCount == 0) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

            // If only one predicate was created, return it directly instead of a combined predicate
            if (predicateCount == 1) {
                // Find and return the single predicate
                if (!nameKeywords.isEmpty()) {
                    return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
                } else if (!subjectKeywords.isEmpty()) {
                    return new FindCommand(new SubjectContainsKeywordsPredicate(subjectKeywords));
                } else if (!dayKeywords.isEmpty()) {
                    return new FindCommand(new DayMatchesPredicate(dayKeywords));
                } else if (!paymentStatuses.isEmpty()) {
                    return new FindCommand(new PaymentStatusMatchesPredicate(paymentStatuses));
                } else if (!tagKeywords.isEmpty()) {
                    return new FindCommand(new TagContainsKeywordsPredicate(tagKeywords));
                }
            }

            return new FindCommand(combinedPredicate);
        }
    }

    private static List<String> getAllKeywords(ArgumentMultimap argMultimap, Prefix prefix) {
        List<String> keywords = new ArrayList<>();
        for (String value : argMultimap.getAllValues(prefix)) {
            Arrays.stream(value.split("\\s+"))
                    .filter(keyword -> !keyword.isBlank())
                    .forEach(keywords::add);
        }
        return keywords;
    }

    private static List<String> getAllValidatedDays(ArgumentMultimap argMultimap) throws ParseException {
        List<String> days = new ArrayList<>();
        for (String value : getAllKeywords(argMultimap, PREFIX_DAY)) {
            days.add(ParserUtil.parseDay(value).dayName);
        }
        return days;
    }

    private static List<String> getAllValidatedPaymentStatuses(ArgumentMultimap argMultimap) throws ParseException {
        List<String> paymentStatuses = new ArrayList<>();
        for (String value : getAllKeywords(argMultimap, PREFIX_PAYMENT_STATUS)) {
            paymentStatuses.add(ParserUtil.parsePaymentStatus(value).value);
        }
        return paymentStatuses;
    }

}
