package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        UserPrefs userPrefs = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPrefs.setAddressBookFilePath(null));
    }

    @Test
    public void equals() {
        UserPrefs userPrefs = new UserPrefs();

        // same object -> returns true
        assertTrue(userPrefs.equals(userPrefs));

        // null -> returns false
        assertFalse(userPrefs.equals(null));

        // different type -> returns false
        assertFalse(userPrefs.equals(5));

        // same values -> returns true
        assertTrue(userPrefs.equals(new UserPrefs()));

        // different guiSettings -> returns false
        UserPrefs otherPrefs = new UserPrefs();
        otherPrefs.setGuiSettings(new GuiSettings(500, 400, 0, 0));
        assertFalse(userPrefs.equals(otherPrefs));

        // different file path -> returns false
        UserPrefs diffPath = new UserPrefs();
        diffPath.setAddressBookFilePath(Paths.get("other", "path.json"));
        assertFalse(userPrefs.equals(diffPath));
    }

    @Test
    public void hashcode() {
        UserPrefs userPrefs = new UserPrefs();
        assertEquals(userPrefs.hashCode(), new UserPrefs().hashCode());

        UserPrefs otherPrefs = new UserPrefs();
        otherPrefs.setGuiSettings(new GuiSettings(500, 400, 0, 0));
        assertNotEquals(userPrefs.hashCode(), otherPrefs.hashCode());
    }

    @Test
    public void toStringMethod() {
        UserPrefs userPrefs = new UserPrefs();
        String str = userPrefs.toString();
        assertTrue(str.contains("Gui Settings"));
        assertTrue(str.contains("Local data file location"));
    }

}
