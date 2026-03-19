package seedu.address.ui;

import java.io.IOException;
import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private FlowPane subjects;

    @FXML
    private FlowPane days;

    @FXML
    private FlowPane times;

    // Add other fields as needed (e.g., student ID, class, etc.)

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
            dialogStage.setTitle("View Person");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setResizable(false);

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

        // Format tags nicely
        String tags = person.getTags().stream()
                .map(tag -> tag.tagName)
                .reduce((a, b) -> a + ", " + b)
                .orElse("None");
        tagsLabel.setText(tags);

        PaymentStatus hasPaid = person.getPaymentStatus();
        paymentLabel.setText(hasPaid.toString());

        // Populate subjects
        person.getSubjects().stream()
                .sorted(Comparator.comparing(s -> s.subjectName))
                .forEach(s -> subjects.getChildren().add(new Label(s.subjectName)));

        // Populate days
        person.getDays().stream()
                .sorted(Comparator.comparing(d -> d.dayName))
                .forEach(d -> days.getChildren().add(new Label(d.dayName)));

        // Populate times
        person.getTimes().stream()
                .sorted(Comparator.comparing(t -> t.timeValue))
                .forEach(t -> times.getChildren().add(new Label(t.timeValue)));
    }

    @FXML
    private void handleClose() {
        dialogStage.close();
    }
}
