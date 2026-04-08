package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class SubjectContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        SubjectContainsKeywordsPredicate firstPredicate =
                new SubjectContainsKeywordsPredicate(firstPredicateKeywordList);
        SubjectContainsKeywordsPredicate secondPredicate =
                new SubjectContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        SubjectContainsKeywordsPredicate firstPredicateCopy =
                new SubjectContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_subjectContainsKeywords_returnsTrue() {
        // One keyword
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Collections.singletonList("Math"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots("Mathematics", "Monday", "1400").build()));

        // Multiple keywords
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Math", "English"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Only one matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Chemistry", "English"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Mixed-case keywords
        predicate = new SubjectContainsKeywordsPredicate(Collections.singletonList("mAtH"));
        assertTrue(predicate.test(new PersonBuilder().withLessonSlots("Mathematics", "Monday", "1400").build()));
    }

    @Test
    public void test_subjectDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withLessonSlots("Mathematics", "Monday", "1400").build()));

        // Non-matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Chemistry"));
        assertFalse(predicate.test(new PersonBuilder().withLessonSlots(
                "Mathematics", "Monday", "1400", "English", "Tuesday", "0900").build()));

        // Keywords match name, email and address, but does not match subject
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Alice", "alice@email.com", "Main Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withLessonSlots("Mathematics", "Monday", "1400").build()));
    }
}
