package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("hello world")); // spaces not allowed
        assertFalse(Tag.isValidTagName("tag-name")); // hyphens not allowed

        // valid tag names
        assertTrue(Tag.isValidTagName("friend"));
        assertTrue(Tag.isValidTagName("123"));
        assertTrue(Tag.isValidTagName("friend123"));
    }

    @Test
    public void equals() {
        Tag tag = new Tag("friend");

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different type -> returns false
        assertFalse(tag.equals("friend"));

        // same tag name -> returns true
        assertTrue(tag.equals(new Tag("friend")));

        // different tag name -> returns false
        assertFalse(tag.equals(new Tag("enemy")));
    }

    @Test
    public void hashcode() {
        Tag tag = new Tag("friend");
        assertEquals(tag.hashCode(), new Tag("friend").hashCode());
        assertNotEquals(tag.hashCode(), new Tag("enemy").hashCode());
    }

    @Test
    public void toStringMethod() {
        Tag tag = new Tag("friend");
        assertEquals("[friend]", tag.toString());
    }

}
