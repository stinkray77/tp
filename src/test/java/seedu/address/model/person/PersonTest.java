package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_STATUS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, ()
                -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        assertTrue(ALICE.isSamePerson(ALICE));

        assertFalse(ALICE.isSamePerson(null));

        Person editedAlice = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        Person differentName = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(differentName));

        Person editedBob = new PersonBuilder(BOB)
                .withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB)
                .withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        assertTrue(ALICE.equals(ALICE));

        assertFalse(ALICE.equals(null));

        assertFalse(ALICE.equals(5));

        assertFalse(ALICE.equals(BOB));

        Person editedAlice = new PersonBuilder(ALICE)
                .withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE)
                .withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE)
                .withPaymentStatus(VALID_PAYMENT_STATUS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        editedAlice = new PersonBuilder(ALICE)
                .withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void getSubjects_derivedFromLessonSlots() {
        Person person = new PersonBuilder()
                .withLessonSlots("Mathematics", "Monday", "1400").build();
        assertEquals(1, person.getSubjects().size());
        assertTrue(person.getSubjects().stream()
                .anyMatch(s -> s.subjectName.equals("Mathematics")));
    }

    @Test
    public void getDays_derivedFromLessonSlots() {
        Person person = new PersonBuilder()
                .withLessonSlots("Mathematics", "Monday", "1400").build();
        assertEquals(1, person.getDays().size());
        assertTrue(person.getDays().stream()
                .anyMatch(d -> d.dayName.equals("Monday")));
    }

    @Test
    public void getTimes_derivedFromLessonSlots() {
        Person person = new PersonBuilder()
                .withLessonSlots("Mathematics", "Monday", "1400").build();
        assertEquals(1, person.getTimes().size());
        assertTrue(person.getTimes().stream()
                .anyMatch(t -> t.timeValue.equals("1400")));
    }

    @Test
    public void hasSameEmail() {
        // Same person
        assertTrue(ALICE.hasSameEmail(ALICE));

        // Null comparison
        assertFalse(ALICE.hasSameEmail(null));

        // Same email, different person
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.hasSameEmail(aliceCopy));

        // Different email, same person name
        Person editedAlice = new PersonBuilder(ALICE)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.hasSameEmail(editedAlice));

        // Different email, different person
        assertFalse(ALICE.hasSameEmail(BOB));

        // Case sensitivity test - email comparison should be case-sensitive
        Person aliceWithDifferentCaseEmail = new PersonBuilder(ALICE)
                .withEmail(ALICE.getEmail().value.toUpperCase()).build();
        assertFalse(ALICE.hasSameEmail(aliceWithDifferentCaseEmail));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{name=" + ALICE.getName()
                + ", email=" + ALICE.getEmail()
                + ", address=" + ALICE.getAddress()
                + ", lessonSlots=" + ALICE.getLessonSlots()
                + ", emergencyContact=" + ALICE.getEmergencyContact()
                + ", paymentStatus=" + ALICE.getPaymentStatus()
                + ", remark=" + ALICE.getRemark()
                + ", tags=" + ALICE.getTags()
                + ", attendanceRecords=" + ALICE.getAttendanceRecords() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
