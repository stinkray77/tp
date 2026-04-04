package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class DayMatchesPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        DayMatchesPredicate firstPredicate = new DayMatchesPredicate(firstPredicateKeywordList);
        DayMatchesPredicate secondPredicate = new DayMatchesPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        DayMatchesPredicate firstPredicateCopy = new DayMatchesPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_dayMatches_returnsTrue() {
        // One keyword
        DayMatchesPredicate predicate = new DayMatchesPredicate(Collections.singletonList("Monday"));
        assertTrue(predicate.test(new PersonBuilder().withDays("Monday", "Tuesday").build()));

        // Multiple keywords
        predicate = new DayMatchesPredicate(Arrays.asList("Monday", "Tuesday"));
        assertTrue(predicate.test(new PersonBuilder().withDays("Monday", "Tuesday").build()));

        // Only one matching keyword
        predicate = new DayMatchesPredicate(Arrays.asList("Wednesday", "Tuesday"));
        assertTrue(predicate.test(new PersonBuilder().withDays("Monday", "Tuesday").build()));

        // Mixed-case keywords
        predicate = new DayMatchesPredicate(Collections.singletonList("mOnDaY"));
        assertTrue(predicate.test(new PersonBuilder().withDays("Monday", "Tuesday").build()));
    }

    @Test
    public void test_dayDoesNotMatch_returnsFalse() {
        // Zero keywords
        DayMatchesPredicate predicate = new DayMatchesPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withDays("Monday").build()));

        // Non-matching keyword
        predicate = new DayMatchesPredicate(Arrays.asList("Wednesday"));
        assertFalse(predicate.test(new PersonBuilder().withDays("Monday", "Tuesday").build()));

        // Keywords match name, email and address, but does not match day
        predicate = new DayMatchesPredicate(Arrays.asList("Alice", "alice@email.com", "Main Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withDays("Monday").build()));
    }
}
