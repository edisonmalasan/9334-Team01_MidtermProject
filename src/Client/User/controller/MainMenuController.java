package Client.User.controller;
/**
 * Controls main menu view window
 */
import common.Log.AnsiFormatter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainMenuController {
    private static final Logger logger = Logger.getLogger(MainMenuController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button quitButton;

    @FXML
    public void initialize() {
        playButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Play button clicked.");
            switchToModeMenu(actionEvent);
        });

        leaderboardButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Leaderboard button clicked.");
            switchToLeaderboard(actionEvent);
        });

        quitButton.setOnAction(actionEvent -> {
            logger.info("\nMainMenuController: Exiting application.");
            System.exit(0);
        });
    }

    private void switchToModeMenu(ActionEvent event) {
        switchScene(event, "/views/client/mode_menu.fxml", "Bomb Defusing Game");
    }

    private void switchToLeaderboard(ActionEvent event) {
        switchScene(event, "/views/client/leaderboard.fxml", "Leaderboard");
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