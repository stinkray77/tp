package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SubjectTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Subject(null));
    }

    @Test
    public void constructor_invalidSubject_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Subject(""));
        assertThrows(IllegalArgumentException.class, () -> new Subject(" "));
        assertThrows(IllegalArgumentException.class, () -> new Subject("Math@"));
    }

    @Test
    public void isValidSubject() {
        assertFalse(Subject.isValidSubject(""));
        assertFalse(Subject.isValidSubject(" "));
        assertFalse(Subject.isValidSubject("Math!"));
        assertFalse(Subject.isValidSubject("@Science"));

        // special characters currently not accepted (planned enhancement)
        assertFalse(Subject.isValidSubject("A-Math")); // hyphen
        assertFalse(Subject.isValidSubject("Mother Tongue (Chinese)")); // parentheses

        assertTrue(Subject.isValidSubject("Mathematics"));
        assertTrue(Subject.isValidSubject("English Literature"));
        assertTrue(Subject.isValidSubject("H2 Physics"));
        assertTrue(Subject.isValidSubject("3"));
    }

    @Test
    public void toStringMethod() {
        Subject subject = new Subject("Mathematics");
        assertEquals("Mathematics", subject.toString());
    }

    @Test
    public void equals() {
        Subject subject = new Subject("Mathematics");

        assertTrue(subject.equals(subject));
        assertTrue(subject.equals(new Subject("Mathematics")));

        assertFalse(subject.equals(null));
        assertFalse(subject.equals("Mathematics"));
        assertFalse(subject.equals(new Subject("English")));
    }

    @Test
    public void hashCodeMethod() {
        Subject subject = new Subject("Mathematics");
        assertEquals(subject.hashCode(), new Subject("Mathematics").hashCode());
    }
}
