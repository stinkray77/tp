package seedu.address.ui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;

/**
 * A dialog to show the details of a person.
 */
public class PersonViewDialog {

    @FXML
    private Label nameLabel;

    @FXML
    private Label emergencyContactLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label tagsLabel;

    @FXML
    private Label paymentLabel;

    @FXML
    private Label remarkLabel;

    @FXML
    private FlowPane lessonSlots;

    @FXML
    private VBox attendanceRecordsBox;

    private Stage dialogStage;

    /**
     * Opens a modal dialog to display person details
     */
    public static void show(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PersonViewDialog.class.getResource("/view/PersonViewDialog.fxml"));
            VBox page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Student Details");
            dialogStage.initStyle(StageStyle.DECORATED);
            dialogStage.initModality(Modality.NONE);
            dialogStage.setResizable(true);

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PersonViewDialog controller = loader.getController();
            controller.dialogStage = dialogStage;
            controller.setPerson(person);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPerson(Person person) {
        nameLabel.setText(person.getName().fullName);
        emergencyContactLabel.setText(person.getEmergencyContact().value);
        emailLabel.setText(person.getEmail().value);
        addressLabel.setText(person.getAddress().value);
        String remarkText = person.getRemark().value;
        remarkLabel.setText(remarkText.isEmpty() ? "None" : remarkText);

        // Format tags nicely
        String tags = person.getTags().stream()
                .map(tag -> tag.tagName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");
        tagsLabel.setText(tags);

        PaymentStatus hasPaid = person.getPaymentStatus();
        paymentLabel.setText(hasPaid.toString());

        // Populate lesson slots
        person.getLessonSlots().stream()
                .sorted(Comparator.comparing(ls -> ls.toString()))
                .forEach(ls -> lessonSlots.getChildren().add(new Label(ls.toString())));

        // Populate attendance records
        Map<String, Map<String, AttendanceStatus>> records = person.getAttendanceRecords();
        if (records.isEmpty()) {
            attendanceRecordsBox.getChildren().add(new Label("None"));
        } else {
            records.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(subjectEntry -> {
                        Label subjectLabel = new Label(subjectEntry.getKey());
                        subjectLabel.setFont(Font.font("System Bold", 12));
                        attendanceRecordsBox.getChildren().add(subjectLabel);
                        subjectEntry.getValue().entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .forEach(lessonEntry -> {
                                    Label lessonLabel = new Label(
                                            "  " + lessonEntry.getKey() + ": " + lessonEntry.getValue().value);
                                    attendanceRecordsBox.getChildren().add(lessonLabel);
                                });
                    });
        }
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
