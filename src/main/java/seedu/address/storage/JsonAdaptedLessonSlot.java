package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Day;
import seedu.address.model.person.LessonSlot;
import seedu.address.model.person.Subject;
import seedu.address.model.person.Time;

/**
 * Jackson-friendly version of {@link LessonSlot}.
 */
class JsonAdaptedLessonSlot {

    private final String subject;
    private final String day;
    private final String time;

    /**
     * Constructs a {@code JsonAdaptedLessonSlot} with the given details.
     */
    @JsonCreator
    public JsonAdaptedLessonSlot(@JsonProperty("subject") String subject,
                                  @JsonProperty("day") String day,
                                  @JsonProperty("time") String time) {
        this.subject = subject;
        this.day = day;
        this.time = time;
    }

    /**
     * Converts a given {@code LessonSlot} into this class for Jackson use.
     */
    public JsonAdaptedLessonSlot(LessonSlot source) {
        subject = source.getSubject().subjectName;
        day = source.getDay().dayName;
        time = source.getTime().timeValue;
    }

    /**
     * Converts this Jackson-friendly adapted lesson slot object into the model's {@code LessonSlot} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public LessonSlot toModelType() throws IllegalValueException {
        if (!Subject.isValidSubject(subject)) {
            throw new IllegalValueException(Subject.MESSAGE_CONSTRAINTS);
        }
        if (!Day.isValidDay(day)) {
            throw new IllegalValueException(Day.MESSAGE_CONSTRAINTS);
        }
        if (!Time.isValidTime(time)) {
            throw new IllegalValueException(Time.MESSAGE_CONSTRAINTS);
        }
        return new LessonSlot(new Subject(subject), new Day(day), new Time(time));
    }
}
