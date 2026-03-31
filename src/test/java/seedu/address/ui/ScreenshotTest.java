package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.Window;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
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
                + "s/Mathematics d/Monday ti/1400 ec/91234567 ps/Due");
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

        runCommand(robot, "list");
        runCommand(robot, "view 1");
        WaitForAsyncUtils.waitForFxEvents();
        takeScreenshot("view-result");

        runCommand(robot, "delete 8");
        takeScreenshot("delete-result");

        runCommand(robot, "help");
        WaitForAsyncUtils.waitForFxEvents();
        takeScreenshot("help-result");
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
     * Issue #96: mark with a negative index should produce a clear error message,
     * not a crash or a misleading message.
     * Expected: "Invalid command format" shown in result display.
     * Result: PASS — MarkCommandParser rejects non-positive indices correctly.
     */
    @Test
    void issue96_markNegativeIndexShowsError(FxRobot robot) {
        runCommand(robot, "mark -1 ps/Paid");
        String result = getResultText(robot);
        assertTrue(result.contains("Invalid command format"),
                "Issue #96: Expected 'Invalid command format' for mark -1, but got: " + result);
    }

    /**
     * Issue #97: The UG documents prefix-based find (find n/NAME, find ps/STATUS, etc.)
     * but FindCommandParser only does plain name-keyword matching — no prefix support.
     * CONFIRMED BUG: find ps/Due treats "ps/Due" as a literal name keyword (0 results).
     * DEEPER BUG: find n/Alice also returns 0 because "n/Alice" is not a word in any name.
     * FindCommandParser must be updated to use ArgumentTokenizer for prefix-based filtering.
     * Expected (correct): find ps/Due returns 3 (Benson, Daniel, Fiona).
     * Actual: 0.
     */
    @Test
    void issue97_findPrefixSyntaxNotImplemented(FxRobot robot) {
        runCommand(robot, "list");
        assertEquals(TYPICAL_TOTAL, logic.getFilteredPersonList().size(),
                "Setup: expected all typical persons listed");

        // Test 1: find ps/Due — UG says this finds students with Due status
        runCommand(robot, "find ps/Due");
        int foundByPayment = logic.getFilteredPersonList().size();
        assertEquals(0, foundByPayment,
                "Issue #97 (BUG CONFIRMED): find ps/Due treats 'ps/Due' as a name keyword. "
                + "Returns " + foundByPayment + " instead of " + TYPICAL_DUE_COUNT
                + ". FindCommandParser has no prefix-based filtering support.");

        // Test 2: find n/Alice — UG says this finds students named Alice
        runCommand(robot, "list");
        runCommand(robot, "find n/Alice");
        int foundByNamePrefix = logic.getFilteredPersonList().size();
        assertEquals(0, foundByNamePrefix,
                "Issue #97 (BUG CONFIRMED): find n/Alice treats 'n/Alice' as a literal name keyword. "
                + "Returns " + foundByNamePrefix + " instead of 1. "
                + "Use 'find Alice' (no prefix) for name search to work.");
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
        robot.clickOn(".text-field");
        robot.write(command);
        robot.push(KeyCode.ENTER);
        WaitForAsyncUtils.waitForFxEvents();
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
        AtomicReference<WritableImage> imageRef = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            imageRef.set(primaryStage.getScene().snapshot(null));
            latch.countDown();
        });
        latch.await();
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageRef.get(), null);
        Path outputPath = DOCS_IMAGES.resolve(name + ".png");
        ImageIO.write(bufferedImage, "PNG", outputPath.toFile());
        System.out.println("Saved: " + outputPath.toAbsolutePath());
    }
}
