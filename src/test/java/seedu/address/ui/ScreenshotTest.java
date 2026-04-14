package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.imageio.ImageIO;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.AttendanceStatus;
import seedu.address.model.person.PaymentStatus;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;

/**
 * Generates screenshots of each major command for the User Guide,
 * and verifies behaviour for alpha-bug issues #96–#100.
 * Run with: ./gradlew screenshotTest
 * Screenshots are saved to docs/images/.
 */
@Tag("screenshot")
@ExtendWith(ApplicationExtension.class)
public class ScreenshotTest {

    private static final Path DOCS_IMAGES = Paths.get("docs", "images");

    // TypicalPersons payment statuses: Paid=Alice,Carl,Elle(3) | Due=Benson,Daniel,Fiona(3) | Overdue=George(1)
    private static final int TYPICAL_TOTAL = 7;
    private static final int TYPICAL_PAID_COUNT = 3;
    private static final int TYPICAL_DUE_COUNT = 3;

    private MainWindow mainWindow;
    private Stage primaryStage;
    private Logic logic;

    /**
     * Initialises the application with a typical address book.
     */
    @Start
    void start(Stage stage) throws Exception {
        primaryStage = stage;

        Path tempDir = Files.createTempDirectory("tutorcentral-screenshot");
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(tempDir.resolve("data.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(tempDir.resolve("prefs.json"));
        Storage storage = new StorageManager(addressBookStorage, userPrefsStorage);

        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        logic = new LogicManager(model, storage);

        mainWindow = new MainWindow(stage, logic);
        mainWindow.show();
        mainWindow.fillInnerParts();
    }

    // -------------------------------------------------------------------------
    // Screenshot generation tests
    // -------------------------------------------------------------------------

    /**
     * Generates screenshots of all major commands documented in the User Guide.
     */
    @Test
    void generateAllScreenshots(FxRobot robot) throws IOException, InterruptedException {
        Files.createDirectories(DOCS_IMAGES);

        takeScreenshot("startup");

        runCommand(robot, "list");
        takeScreenshot("list-result");

        runCommand(robot,
                "add n/John Tan e/johntan@example.com a/21 Lower Kent Ridge Rd "
                + "s/Mathematics d/Monday ti/1400 ec/91234567 ps/Due "
                + "r/Needs extra help");
        takeScreenshot("add-result");

        runCommand(robot, "list");
        takeScreenshot("list-after-add");

        runCommand(robot, "find John");
        takeScreenshot("find-name-result");

        runCommand(robot, "find ps/Due");
        takeScreenshot("find-payment-result");

        runCommand(robot, "list");
        runCommand(robot, "edit 1 ps/Paid");
        takeScreenshot("edit-result");

        runCommand(robot, "list");
        runCommand(robot, "mark 1 ps/Overdue");
        takeScreenshot("mark-result");

        runCommand(robot, "list");
        runCommand(robot, "remark 1 r/Needs extra help with algebra.");
        takeScreenshot("remark-result");

        // markattendance — Alice has Mathematics on Monday at 1400
        runCommand(robot, "list");
        runCommand(robot, "markattendance 1 s/Mathematics d/Monday ti/1400 st/Present");
        takeScreenshot("markattendance-result");

        // markattendance error — student does not have this lesson slot
        runCommand(robot, "markattendance 3 s/Mathematics d/Tuesday ti/0900 st/Present");
        takeScreenshot("markattendance-error");

        // listattendance — show attendance records for Alice
        runCommand(robot, "listattendance 1");
        takeScreenshot("listattendance-result");

        // view — execute via logic directly to avoid showAndWait() blocking,
        // then manually open the dialog non-modally for the screenshot.
        runCommand(robot, "list");
        Person personToView = logic.getFilteredPersonList().get(0);
        showViewDialogAndScreenshot(personToView, "view-result");

        // delete — delete the added student (index 8)
        runCommand(robot, "list");
        runCommand(robot, "delete 8");
        takeScreenshot("delete-result");

        // help — main window shows "Opened help window." in the result display
        runCommand(robot, "help");
        WaitForAsyncUtils.waitForFxEvents();
        takeScreenshot("help-result");
        closeNonModalWindows();
    }

    /**
     * Generates a screenshot of the list state for the UG overview image.
     */
    @Test
    void generateUiOverviewScreenshot(FxRobot robot) throws IOException, InterruptedException {
        Files.createDirectories(DOCS_IMAGES);
        runCommand(robot, "list");
        takeScreenshot("Ui");
    }

    // -------------------------------------------------------------------------
    // Alpha-bug verification tests (#96 – #100)
    // -------------------------------------------------------------------------

    /**
     * Issue #96: mark with a negative index should produce a clear error message.
     * Fixed: MarkCommandParser now shows "Invalid index: ..." for non-positive indices.
     */
    @Test
    void issue96_markNegativeIndexShowsError(FxRobot robot) {
        runCommand(robot, "mark -1 ps/Paid");
        String result = getResultText(robot);
        assertTrue(result.contains("Invalid index"),
                "Issue #96: Expected 'Invalid index' for mark -1, but got: " + result);
    }

    /**
     * Issue #97: find ps/STATUS and find n/NAME should use prefix-based filtering.
     * Fixed: FindCommandParser now uses ArgumentTokenizer for prefix-based filtering.
     */
    @Test
    void issue97_findPrefixSyntaxNotImplemented(FxRobot robot) {
        runCommand(robot, "list");
        assertEquals(TYPICAL_TOTAL, logic.getFilteredPersonList().size(),
                "Setup: expected all typical persons listed");

        // Test 1: find ps/Due — should find students with Due payment status
        runCommand(robot, "find ps/Due");
        int foundByPayment = logic.getFilteredPersonList().size();
        assertEquals(TYPICAL_DUE_COUNT, foundByPayment,
                "Issue #97 (FIXED): find ps/Due should return " + TYPICAL_DUE_COUNT
                + " but got " + foundByPayment);

        // Test 2: find n/Alice — should find student named Alice
        runCommand(robot, "list");
        runCommand(robot, "find n/Alice");
        int foundByNamePrefix = logic.getFilteredPersonList().size();
        assertEquals(1, foundByNamePrefix,
                "Issue #97 (FIXED): find n/Alice should return 1 but got " + foundByNamePrefix);
    }

    /**
     * Issue #98: add command with mismatched day/time counts should show a specific error
     * mentioning the exact counts (e.g. "Got 2 day(s) and 1 time(s)").
     * Result: PASS — Messages.MESSAGE_DAY_TIME_MISMATCH includes the counts.
     */
    @Test
    void issue98_mismatchedDayTimePairErrorIsSpecific(FxRobot robot) {
        runCommand(robot,
                "add n/Test e/t@t.com a/addr s/Math d/Monday d/Tuesday ti/1400 ec/91234567 ps/Paid");
        String result = getResultText(robot);
        assertTrue(result.contains("2") && result.contains("1"),
                "Issue #98: Error message should mention the mismatch counts (2 days, 1 time). Got: " + result);
    }

    /**
     * Issue #99: opening the help window and verifying a second window is shown.
     * Also verifies the main window stays open and the help window is a distinct Stage.
     * (Exit behaviour is not tested here as it hides the shared Stage used by all tests.)
     * Result: PASS — help command opens a second window.
     */
    @Test
    void issue99_helpWindowOpensAsSecondWindow(FxRobot robot) throws InterruptedException {
        runCommand(robot, "help");
        WaitForAsyncUtils.waitForFxEvents();

        int showingWindows = countShowingWindows();
        assertTrue(showingWindows >= 2,
                "Issue #99: Expected at least 2 windows after help (main + help). Got: " + showingWindows);
    }

    /**
     * Issue #100: deleting a student from a filtered list should reset the view to show
     * all remaining students. CONFIRMED BUG — DeleteCommand does not call
     * model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS), so the filter stays active.
     * Expected (correct): 6 students shown after deleting Alice from a filtered view.
     * Actual: 0 students shown (filter still active, Alice gone so no match).
     */
    @Test
    void issue100_listDoesNotResetAfterDeleteOnFilteredView(FxRobot robot) {
        runCommand(robot, "find Alice");
        assertEquals(1, logic.getFilteredPersonList().size(),
                "Setup: find Alice should show exactly 1 result (Alice Pauline)");

        runCommand(robot, "delete 1");
        int shown = logic.getFilteredPersonList().size();

        // Document the actual (broken) behaviour:
        // After deleting on a filtered list, the filter stays active.
        // Alice is gone, so 0 people match the "Alice" name filter.
        // The correct behaviour would reset to show all remaining 6 students.
        assertEquals(0, shown,
                "Issue #100 (BUG CONFIRMED): After deleting on a filtered list, list shows "
                + shown + " persons. Expected reset to " + (TYPICAL_TOTAL - 1)
                + " but DeleteCommand does not call updateFilteredPersonList.");
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void runCommand(FxRobot robot, String command) {
        clearAndType(robot, command);
        robot.push(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
    }

    /**
     * Clears the command text field and types a new command into it.
     */
    private void clearAndType(FxRobot robot, String command) {
        robot.clickOn(".text-field");
        // Select all existing text and delete it before typing new command
        robot.push(KeyCode.SHORTCUT, KeyCode.A);
        robot.push(KeyCode.BACK_SPACE);
        robot.write(command);
    }

    private String getResultText(FxRobot robot) {
        return robot.lookup("#resultDisplay").queryAs(TextArea.class).getText();
    }

    private int countShowingWindows() throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            count.set((int) Window.getWindows().stream()
                    .filter(Window::isShowing).count());
            latch.countDown();
        });
        latch.await();
        return count.get();
    }

    private void takeScreenshot(String name) throws IOException, InterruptedException {
        WaitForAsyncUtils.waitForFxEvents();
        takeScreenshotOfStage(primaryStage, name);
    }

    /**
     * Takes a screenshot of a popup/dialog window (the topmost non-primary showing window).
     * Falls back to the primary stage if no popup is found.
     */
    private void takeScreenshotOfPopup(String name) throws IOException, InterruptedException {
        AtomicReference<Stage> popupRef = new AtomicReference<>();
        CountDownLatch findLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            for (Window w : Window.getWindows()) {
                if (w instanceof Stage && w != primaryStage && w.isShowing()) {
                    popupRef.set((Stage) w);
                }
            }
            findLatch.countDown();
        });
        findLatch.await();

        Stage target = popupRef.get() != null ? popupRef.get() : primaryStage;
        takeScreenshotOfStage(target, name);
    }

    private void takeScreenshotOfStage(Stage stage, String name) throws IOException, InterruptedException {
        AtomicReference<WritableImage> imageRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            imageRef.set(stage.getScene().snapshot(null));
            latch.countDown();
        });
        latch.await();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageRef.get(), null);
        Path outputPath = DOCS_IMAGES.resolve(name + ".png");
        ImageIO.write(bufferedImage, "PNG", outputPath.toFile());
        System.out.println("Saved: " + outputPath.toAbsolutePath());
    }

    /**
     * Opens the PersonViewDialog non-modally (using show() instead of showAndWait()),
     * takes a screenshot of the dialog, then closes it. This avoids the blocking
     * behaviour of showAndWait() which hangs TestFX.
     */
    private void showViewDialogAndScreenshot(Person person, String name)
            throws IOException, InterruptedException {
        AtomicReference<Stage> dialogRef = new AtomicReference<>();
        CountDownLatch openLatch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(PersonViewDialog.class.getResource("/view/PersonViewDialog.fxml"));
                VBox page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Student Details");
                dialogStage.setResizable(true);
                dialogStage.setScene(new Scene(page));

                // Populate the dialog fields using the FXML controller's IDs
                Label nameLabel = (Label) page.lookup("#nameLabel");
                Label emergencyContactLabel = (Label) page.lookup("#emergencyContactLabel");
                Label emailLabel = (Label) page.lookup("#emailLabel");
                Label addressLabel = (Label) page.lookup("#addressLabel");
                Label tagsLabel = (Label) page.lookup("#tagsLabel");
                Label paymentLabel = (Label) page.lookup("#paymentLabel");
                Label remarkLabel = (Label) page.lookup("#remarkLabel");
                FlowPane lessonSlots = (FlowPane) page.lookup("#lessonSlots");
                VBox attendanceRecordsBox = (VBox) page.lookup("#attendanceRecordsBox");

                nameLabel.setText(person.getName().fullName);
                emergencyContactLabel.setText(person.getEmergencyContact().value);
                emailLabel.setText(person.getEmail().value);
                addressLabel.setText(person.getAddress().value);
                String remarkText = person.getRemark().value;
                remarkLabel.setText(remarkText.isEmpty() ? "None" : remarkText);

                String tags = person.getTags().stream()
                        .map(tag -> tag.tagName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("None");
                tagsLabel.setText(tags);

                PaymentStatus hasPaid = person.getPaymentStatus();
                paymentLabel.setText(hasPaid.toString());

                person.getLessonSlots().stream()
                        .sorted(Comparator.comparing(ls -> ls.toString()))
                        .forEach(ls -> lessonSlots.getChildren().add(new Label(ls.toString())));

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
                                                    "  " + lessonEntry.getKey() + ": "
                                                            + lessonEntry.getValue().value);
                                            attendanceRecordsBox.getChildren().add(lessonLabel);
                                        });
                            });
                }

                dialogStage.show(); // non-blocking show
                dialogRef.set(dialogStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            openLatch.countDown();
        });
        openLatch.await();

        WaitForAsyncUtils.waitForFxEvents();
        Thread.sleep(500); // allow layout to settle
        takeScreenshotOfStage(dialogRef.get(), name);

        // Close the dialog
        CountDownLatch closeLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            dialogRef.get().close();
            closeLatch.countDown();
        });
        closeLatch.await();
    }

    /**
     * Closes all non-primary secondary windows (e.g. HelpWindow).
     */
    private void closeNonModalWindows() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            for (Window w : Window.getWindows()) {
                if (w instanceof Stage && w != primaryStage && w.isShowing()) {
                    Stage s = (Stage) w;
                    s.close();
                }
            }
            latch.countDown();
        });
        latch.await();
    }
}
