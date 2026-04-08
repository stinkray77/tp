package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PAYMENT_STATUS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        EditPersonDescriptor descriptorWithSameValues =
                new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        assertTrue(DESC_AMY.equals(DESC_AMY));

        assertFalse(DESC_AMY.equals(null));

        assertFalse(DESC_AMY.equals(5));

        assertFalse(DESC_AMY.equals(DESC_BOB));

        EditPersonDescriptor editedAmy =
                new EditPersonDescriptorBuilder(DESC_AMY)
                        .withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB)
                .build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withPaymentStatus(VALID_PAYMENT_STATUS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY)
                .withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor =
                new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName()
                + "{name="
                + editPersonDescriptor.getName().orElse(null)
                + ", email="
                + editPersonDescriptor.getEmail().orElse(null)
                + ", address="
                + editPersonDescriptor.getAddress().orElse(null)
                + ", subjects="
                + editPersonDescriptor.getSubjects().orElse(null)
                + ", days="
                + editPersonDescriptor.getDays().orElse(null)
                + ", times="
                + editPersonDescriptor.getTimes().orElse(null)
                + ", emergencyContact="
                + editPersonDescriptor.getEmergencyContact().orElse(null)
                + ", paymentStatus="
                + editPersonDescriptor.getPaymentStatus().orElse(null)
                + ", remark="
                + editPersonDescriptor.getRemark().orElse(null)
                + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }
}
