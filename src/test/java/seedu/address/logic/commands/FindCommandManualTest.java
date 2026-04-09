package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.SubjectContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class FindCommandManualTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void testManual() {
        // Test basic name-only search without prefixes
        NameContainsKeywordsPredicate namePredicate = new NameContainsKeywordsPredicate(Arrays.asList("Alex"));
        Person namePerson = new PersonBuilder().withName("Alex Tan").build();
        assertTrue(namePredicate.test(namePerson));
        System.out.println("Name test passed!");

        // Test subject search
        SubjectContainsKeywordsPredicate subjectPredicate = new SubjectContainsKeywordsPredicate(Arrays.asList("Math"));
        Person mathPerson = new PersonBuilder().withName("Alex Tan")
                .withLessonSlots("Mathematics", "Monday", "1400").build();
        boolean subjectResult = subjectPredicate.test(mathPerson);
        System.out.println("Subject test result: " + subjectResult);
        assertTrue(subjectResult);

        // Test tag search
        TagContainsKeywordsPredicate tagPredicate = new TagContainsKeywordsPredicate(Arrays.asList("secondary"));
        Person tagPerson = new PersonBuilder().withName("Alex Tan").withTags("secondary").build();
        boolean tagResult = tagPredicate.test(tagPerson);
        System.out.println("Tag test result: " + tagResult);
        assertTrue(tagResult);

        System.out.println("Manual test passed!");
    }
}
