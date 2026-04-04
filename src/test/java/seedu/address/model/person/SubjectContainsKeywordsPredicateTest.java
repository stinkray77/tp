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
        assertTrue(predicate.test(new PersonBuilder().withSubjects("Mathematics").build()));

        // Multiple keywords
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Math", "English"));
        assertTrue(predicate.test(new PersonBuilder().withSubjects("Mathematics", "English").build()));

        // Only one matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Chemistry", "English"));
        assertTrue(predicate.test(new PersonBuilder().withSubjects("Mathematics", "English").build()));

        // Mixed-case keywords
        predicate = new SubjectContainsKeywordsPredicate(Collections.singletonList("mAtH"));
        assertTrue(predicate.test(new PersonBuilder().withSubjects("Mathematics").build()));
    }

    @Test
    public void test_subjectDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        SubjectContainsKeywordsPredicate predicate =
                new SubjectContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withSubjects("Mathematics").build()));

        // Non-matching keyword
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Chemistry"));
        assertFalse(predicate.test(new PersonBuilder().withSubjects("Mathematics", "English").build()));

        // Keywords match name, email and address, but does not match subject
        predicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Alice", "alice@email.com", "Main Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withSubjects("Mathematics").build()));
    }
}
