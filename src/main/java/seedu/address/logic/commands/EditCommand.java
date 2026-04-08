package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PAYMENT_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Day;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing student in Tutor Central.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_SUBJECT + "SUBJECT]... "
            + "[" + PREFIX_DAY + "DAY]... "
            + "[" + PREFIX_TIME + "TIME]... "
            + "[" + PREFIX_EMERGENCY_CONTACT + "EMERGENCY_CONTACT] "
            + "[" + PREFIX_PAYMENT_STATUS + "PAYMENT_STATUS] "
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_EMERGENCY_CONTACT + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS =
            "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED =
            "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON =
            "This student already exists in Tutor Central.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index,
                       EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor =
                new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(
                personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson)
                && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(
                MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(
            Person personToEdit,
            EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName()
                .orElse(personToEdit.getName());
        Email updatedEmail = editPersonDescriptor.getEmail()
                .orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress()
                .orElse(personToEdit.getAddress());
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects()
                .orElse(personToEdit.getSubjects());
        Set<Day> updatedDays = editPersonDescriptor.getDays()
                .orElse(personToEdit.getDays());
        Set<Time> updatedTimes = editPersonDescriptor.getTimes()
                .orElse(personToEdit.getTimes());
        EmergencyContact updatedEmergencyContact =
                editPersonDescriptor.getEmergencyContact()
                        .orElse(personToEdit.getEmergencyContact());
        PaymentStatus updatedPaymentStatus =
                editPersonDescriptor.getPaymentStatus()
                        .orElse(personToEdit.getPaymentStatus());
        Remark updatedRemark = editPersonDescriptor.getRemark()
                .orElse(personToEdit.getRemark());
        Set<Tag> updatedTags = editPersonDescriptor.getTags()
                .orElse(personToEdit.getTags());

        return new Person(updatedName, updatedEmail, updatedAddress,
                updatedSubjects, updatedDays, updatedTimes,
                updatedEmergencyContact, updatedPaymentStatus,
                updatedRemark, updatedTags,
                personToEdit.getAttendanceRecords());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(
                        otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Email email;
        private Address address;
        private Set<Subject> subjects;
        private Set<Day> days;
        private Set<Time> times;
        private EmergencyContact emergencyContact;
        private PaymentStatus paymentStatus;
        private Remark remark;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} and {@code subjects} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setSubjects(toCopy.subjects);
            setDays(toCopy.days);
            setTimes(toCopy.times);
            setEmergencyContact(toCopy.emergencyContact);
            setPaymentStatus(toCopy.paymentStatus);
            setRemark(toCopy.remark);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, email, address,
                    subjects, days, times, emergencyContact,
                    paymentStatus, remark, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code subjects} to this object's {@code subjects}.
         * A defensive copy of {@code subjects} is used internally.
         */
        public void setSubjects(Set<Subject> subjects) {
            this.subjects = (subjects != null)
                    ? new HashSet<>(subjects) : null;
        }

        /**
         * Returns an unmodifiable subject set, which throws
         * {@code UnsupportedOperationException} if modification is attempted.
         * Returns {@code Optional#empty()} if {@code subjects} is null.
         */
        public Optional<Set<Subject>> getSubjects() {
            return (subjects != null)
                    ? Optional.of(Collections.unmodifiableSet(subjects))
                    : Optional.empty();
        }

        /**
         * Sets {@code days} to this object's {@code days}.
         * A defensive copy of {@code days} is used internally.
         */
        public void setDays(Set<Day> days) {
            this.days = (days != null)
                    ? new HashSet<>(days) : null;
        }

        /**
         * Returns an unmodifiable day set, which throws
         * {@code UnsupportedOperationException} if modification is attempted.
         * Returns {@code Optional#empty()} if {@code days} is null.
         */
        public Optional<Set<Day>> getDays() {
            return (days != null)
                    ? Optional.of(Collections.unmodifiableSet(days))
                    : Optional.empty();
        }

        /**
         * Sets {@code times} to this object's {@code times}.
         * A defensive copy of {@code times} is used internally.
         */
        public void setTimes(Set<Time> times) {
            this.times = (times != null)
                    ? new HashSet<>(times) : null;
        }

        /**
         * Returns an unmodifiable time set, which throws
         * {@code UnsupportedOperationException} if modification is attempted.
         * Returns {@code Optional#empty()} if {@code times} is null.
         */
        public Optional<Set<Time>> getTimes() {
            return (times != null)
                    ? Optional.of(Collections.unmodifiableSet(times))
                    : Optional.empty();
        }

        public void setEmergencyContact(
                EmergencyContact emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public Optional<EmergencyContact> getEmergencyContact() {
            return Optional.ofNullable(emergencyContact);
        }

        public void setPaymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Optional<PaymentStatus> getPaymentStatus() {
            return Optional.ofNullable(paymentStatus);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null)
                    ? Optional.of(Collections.unmodifiableSet(tags))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherDescriptor =
                    (EditPersonDescriptor) other;
            return Objects.equals(name, otherDescriptor.name)
                    && Objects.equals(email, otherDescriptor.email)
                    && Objects.equals(address, otherDescriptor.address)
                    && Objects.equals(subjects, otherDescriptor.subjects)
                    && Objects.equals(days, otherDescriptor.days)
                    && Objects.equals(times, otherDescriptor.times)
                    && Objects.equals(emergencyContact,
                            otherDescriptor.emergencyContact)
                    && Objects.equals(paymentStatus,
                            otherDescriptor.paymentStatus)
                    && Objects.equals(remark, otherDescriptor.remark)
                    && Objects.equals(tags, otherDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("email", email)
                    .add("address", address)
                    .add("subjects", subjects)
                    .add("days", days)
                    .add("times", times)
                    .add("emergencyContact", emergencyContact)
                    .add("paymentStatus", paymentStatus)
                    .add("remark", remark)
                    .add("tags", tags)
                    .toString();
        }
    }
}
