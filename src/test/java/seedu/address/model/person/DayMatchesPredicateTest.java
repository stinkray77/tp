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
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Multiple keywords
        predicate = new DayMatchesPredicate(Arrays.asList("Monday", "Tuesday"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Only one matching keyword
        predicate = new DayMatchesPredicate(Arrays.asList("Wednesday", "Tuesday"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Mixed-case keywords
        predicate = new DayMatchesPredicate(Collections.singletonList("mOnDaY"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));
    }

    @Test
    public void test_dayDoesNotMatch_returnsFalse() {
        // Zero keywords
        DayMatchesPredicate predicate = new DayMatchesPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withLessonSlots("Mathematics", "Monday", "1400").build()));

        // Non-matching keyword
        predicate = new DayMatchesPredicate(Arrays.asList("Wednesday"));
        assertFalse(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Keywords match name, email and address, but does not match day
        predicate = new DayMatchesPredicate(Arrays.asList("Alice", "alice@email.com", "Main Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withLessonSlots("Mathematics", "Monday", "1400").build()));
    }
}
