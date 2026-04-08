package seedu.address.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Tan"), new Email("alex@example.com"),
                new Address("Blk 30 Clementi Ave 2, #05-01"),
                getLessonSlotList("Mathematics", "Monday", "1400",
                        "Physics", "Wednesday", "1600"),
                new EmergencyContact("91234567"),
                new PaymentStatus("Paid"),
                new Remark(""),
                getTagSet("secondary")),
            new Person(new Name("Priya Sharma"), new Email("priya@example.com"),
                new Address("25 Bukit Timah Road"),
                getLessonSlotList("English", "Tuesday", "1000",
                        "History", "Thursday", "1500"),
                new EmergencyContact("82345678"),
                new PaymentStatus("Due"),
                new Remark(""),
                getTagSet("primary")),
            new Person(new Name("James Lee"), new Email("james@example.com"),
                new Address("Blk 123 Bishan St 12, #08-15"),
                getLessonSlotList("Chemistry", "Friday", "0900"),
                new EmergencyContact("93456789"),
                new PaymentStatus("Overdue"),
                new Remark(""),
                getTagSet("jc")),
            new Person(new Name("Sarah Chen"), new Email("sarah@example.com"),
                new Address("10 Pasir Ris Drive 4"),
                getLessonSlotList("Mathematics", "Monday", "1600",
                        "English", "Saturday", "1000"),
                new EmergencyContact("84567890"),
                new PaymentStatus("Paid"),
                new Remark(""),
                getTagSet("secondary", "priority")),
            new Person(new Name("Ravi Kumar"), new Email("ravi@example.com"),
                new Address("Blk 456 Tampines St 42, #12-03"),
                getLessonSlotList("Biology", "Wednesday", "1100"),
                new EmergencyContact("95678901"),
                new PaymentStatus("Due"),
                new Remark(""),
                getTagSet("jc"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a subject set containing the list of strings given.
     */
    public static Set<Subject> getSubjectSet(String... strings) {
        return Arrays.stream(strings)
                .map(Subject::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a day set containing the list of strings given.
     */
    public static Set<Day> getDaySet(String... strings) {
        return Arrays.stream(strings)
                .map(Day::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a time set containing the list of strings given.
     */
    public static Set<Time> getTimeSet(String... strings) {
        return Arrays.stream(strings)
                .map(Time::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns a lesson slot list from groups of 3 strings: subject, day, time.
     * Example: getLessonSlotList("Mathematics", "Monday", "1400", "English", "Tuesday", "0900")
     */
    public static List<LessonSlot> getLessonSlotList(String... triplets) {
        if (triplets.length % 3 != 0) {
            throw new IllegalArgumentException("Arguments must be in groups of 3: subject, day, time");
        }
        List<LessonSlot> list = new ArrayList<>();
        for (int i = 0; i < triplets.length; i += 3) {
            list.add(new LessonSlot(
                    new Subject(triplets[i]),
                    new Day(triplets[i + 1]),
                    new Time(triplets[i + 2])));
        }
        return list;
    }

}
