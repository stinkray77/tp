package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.logging.Level;

import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void toStringMethod() {
        Config config = new Config();
        String expected = Config.class.getCanonicalName() + "{logLevel=" + config.getLogLevel()
                + ", userPrefsFilePath=" + config.getUserPrefsFilePath() + "}";
        assertEquals(expected, config.toString());
    }

    @Test
    public void equalsMethod() {
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);

        // same object -> returns true
        assertTrue(defaultConfig.equals(defaultConfig));

        // null -> returns false
        assertFalse(defaultConfig.equals(null));

        // different type -> returns false
        assertFalse(defaultConfig.equals("string"));

        // same default values -> returns true
        assertTrue(defaultConfig.equals(new Config()));

        // different logLevel -> returns false
        Config diffLevel = new Config();
        diffLevel.setLogLevel(Level.WARNING);
        assertFalse(defaultConfig.equals(diffLevel));

        // different userPrefsFilePath -> returns false
        Config diffPath = new Config();
        diffPath.setUserPrefsFilePath(Paths.get("other.json"));
        assertFalse(defaultConfig.equals(diffPath));
    }

    @Test
    public void hashcode() {
        Config config = new Config();
        assertEquals(config.hashCode(), new Config().hashCode());

        Config diffLevel = new Config();
        diffLevel.setLogLevel(Level.WARNING);
        assertNotEquals(config.hashCode(), diffLevel.hashCode());
    }
}
