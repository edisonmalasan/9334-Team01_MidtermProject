package Client.User.controller;
/**
 * Controls score view window
 */
import common.Log.AnsiFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreController {

    @FXML
    private Label scoreLabel;
    @FXML
    private Button backToMenuButton;

    private int finalScore;
    private static final Logger logger = Logger.getLogger(ScoreController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }
    public void setScore(int score) {
        this.finalScore = score;
        scoreLabel.setText(String.valueOf(finalScore));
    }

    @FXML
    public void initialize() {
        backToMenuButton.setOnAction(event -> switchToMainMenu());

    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("ScoreController: Successfully switched to the Main Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ScoreController: Failed to load Main Menu", e);
        }
    }
}
