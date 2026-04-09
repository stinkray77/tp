package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Day;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_returnsExpectedCount() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(5, persons.length);
    }

    @Test
    public void getSamplePersons_allPersonsNotNull() {
        for (Person person : SampleDataUtil.getSamplePersons()) {
            assertNotNull(person);
        }
    }

    @Test
    public void getSamplePersons_firstPersonIsAlexTan() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(new Name("Alex Tan"), persons[0].getName());
    }

    @Test
    public void getSampleAddressBook_containsAllSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertEquals(5, addressBook.getPersonList().size());
    }

    @Test
    public void getTagSet_singleTag_returnsCorrectSet() {
        assertTrue(SampleDataUtil.getTagSet("secondary").contains(new Tag("secondary")));
    }

    @Test
    public void getSubjectSet_singleSubject_returnsCorrectSet() {
        assertTrue(SampleDataUtil.getSubjectSet("Mathematics").contains(new Subject("Mathematics")));
    }

    @Test
    public void getDaySet_singleDay_returnsCorrectSet() {
        assertTrue(SampleDataUtil.getDaySet("Monday").contains(new Day("Monday")));
    }

    @Test
    public void getTimeSet_singleTime_returnsCorrectSet() {
        assertTrue(SampleDataUtil.getTimeSet("1400").contains(new Time("1400")));
    }

    @Test
    public void getLessonSlotList_validTriplets_returnsCorrectList() {
        List<LessonSlot> slots =
                SampleDataUtil.getLessonSlotList("Mathematics", "Monday", "1400");
        assertEquals(1, slots.size());
        assertEquals(new Subject("Mathematics"), slots.get(0).getSubject());
        assertEquals(new Day("Monday"), slots.get(0).getDay());
        assertEquals(new Time("1400"), slots.get(0).getTime());
    }

    @Test
    public void getLessonSlotList_emptyArgs_returnsEmptyList() {
        List<LessonSlot> slots = SampleDataUtil.getLessonSlotList();
        assertTrue(slots.isEmpty());
    }

    @Test
    public void getLessonSlotList_invalidCount_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()
                -> SampleDataUtil.getLessonSlotList("Mathematics", "Monday"));
    }
}
