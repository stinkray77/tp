package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class TagContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        TagContainsKeywordsPredicate firstPredicate =
                new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        TagContainsKeywordsPredicate secondPredicate =
                new TagContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        TagContainsKeywordsPredicate firstPredicateCopy =
                new TagContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_tagContainsKeywords_returnsTrue() {
        // One keyword
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.singletonList("priority"));
        assertTrue(predicate.test(new PersonBuilder().withTags("priority").build()));

        // Multiple keywords
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("priority", "urgent"));
        assertTrue(predicate.test(new PersonBuilder().withTags("priority", "urgent").build()));

        // Only one matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("normal", "urgent"));
        assertTrue(predicate.test(new PersonBuilder().withTags("priority", "urgent").build()));

        // Mixed-case keywords
        predicate = new TagContainsKeywordsPredicate(Collections.singletonList("PrIoRiTy"));
        assertTrue(predicate.test(new PersonBuilder().withTags("priority").build()));
    }

    @Test
    public void test_tagDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        TagContainsKeywordsPredicate predicate =
                new TagContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withTags("priority").build()));

        // Non-matching keyword
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("normal"));
        assertFalse(predicate.test(new PersonBuilder().withTags("priority", "urgent").build()));

        // Keywords match name, email and address, but does not match tag
        predicate = new TagContainsKeywordsPredicate(Arrays.asList("Alice", "alice@email.com", "Main Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@email.com")
                .withAddress("Main Street").withTags("priority").build()));
    }
}
