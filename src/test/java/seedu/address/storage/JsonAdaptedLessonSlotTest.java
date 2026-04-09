package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Day;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;

public class JsonAdaptedLessonSlotTest {

    private static final String VALID_SUBJECT = "Mathematics";
    private static final String VALID_DAY = "Monday";
    private static final String VALID_TIME = "1400";

    private static final String INVALID_SUBJECT = "@Math";
    private static final String INVALID_DAY = "Someday";
    private static final String INVALID_TIME = "9999";

    @Test
    public void toModelType_validSlot_returnsLessonSlot() throws Exception {
        JsonAdaptedLessonSlot slot =
                new JsonAdaptedLessonSlot(VALID_SUBJECT, VALID_DAY, VALID_TIME);
        LessonSlot expected = new LessonSlot(
                new Subject(VALID_SUBJECT), new Day(VALID_DAY), new Time(VALID_TIME));
        assertEquals(expected, slot.toModelType());
    }

    @Test
    public void toModelType_invalidSubject_throwsIllegalValueException() {
        JsonAdaptedLessonSlot slot =
                new JsonAdaptedLessonSlot(INVALID_SUBJECT, VALID_DAY, VALID_TIME);
        assertThrows(IllegalValueException.class,
                Subject.MESSAGE_CONSTRAINTS, slot::toModelType);
    }

    @Test
    public void toModelType_invalidDay_throwsIllegalValueException() {
        JsonAdaptedLessonSlot slot =
                new JsonAdaptedLessonSlot(VALID_SUBJECT, INVALID_DAY, VALID_TIME);
        assertThrows(IllegalValueException.class,
                Day.MESSAGE_CONSTRAINTS, slot::toModelType);
    }

    @Test
    public void toModelType_invalidTime_throwsIllegalValueException() {
        JsonAdaptedLessonSlot slot =
                new JsonAdaptedLessonSlot(VALID_SUBJECT, VALID_DAY, INVALID_TIME);
        assertThrows(IllegalValueException.class,
                Time.MESSAGE_CONSTRAINTS, slot::toModelType);
    }

    @Test
    public void toModelType_multipleTriplets_constructedFromLessonSlot() throws Exception {
        LessonSlot mathSlot = new LessonSlot(
                new Subject("English"), new Day("Tuesday"), new Time("0900"));
        JsonAdaptedLessonSlot adapted = new JsonAdaptedLessonSlot(mathSlot);
        assertEquals(mathSlot, adapted.toModelType());
    }

    @Test
    public void constructor_fromLessonSlot_roundTrip() throws Exception {
        LessonSlot original = new LessonSlot(
                new Subject(VALID_SUBJECT), new Day(VALID_DAY), new Time(VALID_TIME));
        JsonAdaptedLessonSlot adapted = new JsonAdaptedLessonSlot(original);
        assertEquals(original, adapted.toModelType());
    }
}
