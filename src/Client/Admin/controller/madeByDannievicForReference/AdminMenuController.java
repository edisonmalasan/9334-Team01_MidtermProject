package Client.Admin.controller.madeByDannievicForReference;

import Client.User.controller.MainMenuController;
import common.AnsiFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminMenuController {
    private static final Logger logger = Logger.getLogger(MainMenuController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }
    public Button viewQuestionsButton;
    public Button viewLeaderboardsButton;
    public Button quitButton;

    public void initialize() {
        viewQuestionsButton.setOnAction(actionEvent -> {
            logger.info("\nAdminMenuController: View Questions button clicked.");
            switchToQuestions(actionEvent);
        });

        viewLeaderboardsButton.setOnAction(actionEvent -> {
            logger.info("\nAdminMenuController: View Leaderboards button clicked.");
            switchToAdminLeaderboard(actionEvent);
        });

        quitButton.setOnAction(actionEvent -> {
            logger.info("\nAdminMenuController: Exiting application.");
            System.exit(0);
        });
    }

    private void switchToQuestions(ActionEvent event) {
        switchScene(event, "/views/admin_questions.fxml", "Questions List");
    }

    private void switchToAdminLeaderboard(ActionEvent event) {
        switchScene(event, "/views/admin_leaderboard.fxml", "Leaderboards");
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();

            logger.info("Switched to " + title);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load " + title, e);
        }
    }
}
