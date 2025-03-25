package Client.Player.controller;

import Client.common.connection.ClientConnection;
import common.Log.LogManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import App.App;

import java.io.IOException;

public class ScoreController {
    @FXML
    private Label scoreLabel;
    @FXML
    private Button backToMenuButton;

    private int finalScore;
    private final LogManager logManager = LogManager.getInstance();

    public void setScore(int score) {
        this.finalScore = score;
        scoreLabel.setText(String.valueOf(finalScore));

        logScore();
    }

    private void logScore() {
        try {
            String playerName = LoginController.getCurrentUser() != null ?
                    LoginController.getCurrentUser().getUsername() : "Unknown Player";
            String gameMode = CategoryController.isEndlessMode ? "Endless" : "Classic";

            String scoreMessage = String.format(
                    "Score Report - Player: %s | Mode: %s | Score: %d | IP: %s",
                    playerName,
                    gameMode,
                    finalScore,
                    App.iPAddress
            );

            // send to both local log and server log
            logManager.appendLog(scoreMessage);
            ClientConnection.bombGameServer.logMessage(scoreMessage);

        } catch (Exception e) {
            logManager.appendLog("Failed to log score: " + e.getMessage());
        }
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

            logManager.appendLog("Returned to main menu from score screen");
        } catch (IOException e) {
            logManager.appendLog("Failed to load main menu: " + e.getMessage());
        }
    }
}