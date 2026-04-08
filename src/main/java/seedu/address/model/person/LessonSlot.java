package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a lesson slot that bundles a Subject, Day, and Time together.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class LessonSlot {

    private final Subject subject;
    private final Day day;
    private final Time time;

    /**
     * Every field must be present and not null.
     */
    public LessonSlot(Subject subject, Day day, Time time) {
        requireAllNonNull(subject, day, time);
        this.subject = subject;
        this.day = day;
        this.time = time;
    }

    public Subject getSubject() {
        return subject;
    }

    public Day getDay() {
        return day;
    }

    public Time getTime() {
        return time;
    }

    /**
     * Returns the attendance key for this lesson slot.
     * Format: "Day Time" (e.g., "Monday 1400")
     */
    public String getAttendanceKey() {
        return day.dayName + " " + time.timeValue;
    }

    @Override
    public String toString() {
        return subject.subjectName + ": " + day.dayName + " " + time.timeValue;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LessonSlot)) {
            return false;
        }

        LessonSlot otherSlot = (LessonSlot) other;
        return subject.equals(otherSlot.subject)
                && day.equals(otherSlot.day)
                && time.equals(otherSlot.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, day, time);
    }
}
