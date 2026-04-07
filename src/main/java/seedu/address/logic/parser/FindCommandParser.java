package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Arrays;
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

        // Check if any prefixes are present
        boolean hasPrefixes = argMultimap.getValue(PREFIX_NAME).isPresent()
                || argMultimap.getValue(PREFIX_SUBJECT).isPresent()
                || argMultimap.getValue(PREFIX_DAY).isPresent()
                || !argMultimap.getAllValues(PREFIX_PAYMENT_STATUS).isEmpty()
                || argMultimap.getValue(PREFIX_TAG).isPresent();

        if (!hasPrefixes) {
            // Backward compatible: treat as name search
            String[] nameKeywords = argMultimap.getPreamble().split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
        } else {
            // Prefix-based search: combine predicates with AND logic
            Predicate<Person> combinedPredicate = person -> true;
            int predicateCount = 0;

            if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                String nameArgs = argMultimap.getValue(PREFIX_NAME).get();
                String[] nameKeywords = nameArgs.split("\\s+");
                combinedPredicate = combinedPredicate.and(
                    new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords))
                );
                predicateCount++;
            }

            if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
                String subjectArgs = argMultimap.getValue(PREFIX_SUBJECT).get();
                String[] subjectKeywords = subjectArgs.split("\\s+");
                combinedPredicate = combinedPredicate.and(
                    new SubjectContainsKeywordsPredicate(Arrays.asList(subjectKeywords))
                );
                predicateCount++;
            }

            if (argMultimap.getValue(PREFIX_DAY).isPresent()) {
                String dayArgs = argMultimap.getValue(PREFIX_DAY).get();
                String[] dayKeywords = dayArgs.split("\\s+");
                combinedPredicate = combinedPredicate.and(new DayMatchesPredicate(Arrays.asList(dayKeywords)));
                predicateCount++;
            }

            java.util.List<String> paymentStatuses = argMultimap.getAllValues(PREFIX_PAYMENT_STATUS);
            if (!paymentStatuses.isEmpty()) {
                Predicate<Person> paymentPredicate = paymentStatuses.size() == 1
                        ? new PaymentStatusMatchesPredicate(paymentStatuses.get(0))
                        : paymentStatuses.stream()
                                .map(PaymentStatusMatchesPredicate::new)
                                .map(p -> (Predicate<Person>) p)
                                .reduce(person -> false, Predicate::or);
                combinedPredicate = combinedPredicate.and(paymentPredicate);
                predicateCount++;
            }

            if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
                String tagArgs = argMultimap.getValue(PREFIX_TAG).get();
                String[] tagKeywords = tagArgs.split("\\s+");
                combinedPredicate = combinedPredicate.and(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
                predicateCount++;
            }

            // If only one predicate was created, return it directly instead of a combined predicate
            if (predicateCount == 1) {
                // Find and return the single predicate
                if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
                    String nameArgs = argMultimap.getValue(PREFIX_NAME).get();
                    String[] nameKeywords = nameArgs.split("\\s+");
                    return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
                } else if (argMultimap.getValue(PREFIX_SUBJECT).isPresent()) {
                    String subjectArgs = argMultimap.getValue(PREFIX_SUBJECT).get();
                    String[] subjectKeywords = subjectArgs.split("\\s+");
                    return new FindCommand(new SubjectContainsKeywordsPredicate(Arrays.asList(subjectKeywords)));
                } else if (argMultimap.getValue(PREFIX_DAY).isPresent()) {
                    String dayArgs = argMultimap.getValue(PREFIX_DAY).get();
                    String[] dayKeywords = dayArgs.split("\\s+");
                    return new FindCommand(new DayMatchesPredicate(Arrays.asList(dayKeywords)));
                } else if (!argMultimap.getAllValues(PREFIX_PAYMENT_STATUS).isEmpty()) {
                    java.util.List<String> statuses = argMultimap.getAllValues(PREFIX_PAYMENT_STATUS);
                    Predicate<Person> paymentPredicate = statuses.size() == 1
                            ? new PaymentStatusMatchesPredicate(statuses.get(0))
                            : statuses.stream()
                                    .map(PaymentStatusMatchesPredicate::new)
                                    .map(p -> (Predicate<Person>) p)
                                    .reduce(person -> false, Predicate::or);
                    return new FindCommand(paymentPredicate);
                } else if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
                    String tagArgs = argMultimap.getValue(PREFIX_TAG).get();
                    String[] tagKeywords = tagArgs.split("\\s+");
                    return new FindCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
                }
            }

            return new FindCommand(combinedPredicate);
        }
    }

}
