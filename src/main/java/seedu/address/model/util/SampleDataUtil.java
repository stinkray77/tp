package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getSubjectSet("Mathematics"), getDaySet("Monday"), getTimeSet("1400"),
                new EmergencyContact("87438807"), new PaymentStatus("Paid"), getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getSubjectSet("English", "Science"), getDaySet("Wednesday"), getTimeSet("0900"),
                new EmergencyContact("99272758"), new PaymentStatus("Due"), getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getSubjectSet("Physics"), getDaySet("Friday"), getTimeSet("1500"),
                new EmergencyContact("93210283"), new PaymentStatus("Paid"), getTagSet("neighbours")),
            new Person(new Name("David Li"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getSubjectSet("Chemistry"), getDaySet("Tuesday"), getTimeSet("1000"),
                new EmergencyContact("91031282"), new PaymentStatus("Overdue"), getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getSubjectSet("Mathematics", "English"), getDaySet("Thursday"), getTimeSet("1100"),
                new EmergencyContact("92492021"), new PaymentStatus("Paid"), getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getSubjectSet("Biology"), getDaySet("Monday"), getTimeSet("1600"),
                new EmergencyContact("92624417"), new PaymentStatus("Due"), getTagSet("colleagues"))
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

}
