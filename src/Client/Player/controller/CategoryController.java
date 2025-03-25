package Client.Player.controller;

import Client.connection.ClientConnection;
import common.Log.LogManager;
import common.Response;
import common.model.QuestionModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import App.App;
import java.io.IOException;
import java.util.List;

public class CategoryController {
    @FXML
    public Button returnButton;

    @FXML
    private Button algebraButton;
    @FXML
    private Button anglesButton;
    @FXML
    private Button geometryButton;
    @FXML
    private Button logicButton;
    @FXML
    private Button probabilityButton;
    @FXML
    private Button trigoButton;

    private ClientConnection clientConnection;
    private static String selectedCategory;
    public static boolean isEndlessMode = false;
    private final LogManager logManager = LogManager.getInstance();

    public CategoryController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (Exception e) {
            logManager.appendLog("❌ Failed to initialize ClientConnection: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        algebraButton.setOnAction(event -> requestQuestionFromServer("ALGEBRA", event));
        anglesButton.setOnAction(event -> requestQuestionFromServer("ARITHMETIC", event));
        geometryButton.setOnAction(event -> requestQuestionFromServer("GEOMETRY", event));
        logicButton.setOnAction(event -> requestQuestionFromServer("LOGIC", event));
        probabilityButton.setOnAction(event -> requestQuestionFromServer("PROBABILITY", event));
        trigoButton.setOnAction(event -> requestQuestionFromServer("TRIGONOMETRY", event));
        returnButton.setOnAction(event -> switchToMainMenu());
    }

    private void requestQuestionFromServer(String category, ActionEvent event) {
        selectedCategory = category;
        String playerName = LoginController.getCurrentUser() != null ?
                LoginController.getCurrentUser().getUsername() : "Unknown Player";

        logManager.appendLog(playerName + " requested " + category + " questions | IP: " + App.fetchIPAddress);

        new Thread(() -> {
            try {
                Response response = ClientConnection.bombGameServer.getQuestionsPerCategory(category);

                if (response.isSuccess() && response.getData() instanceof List) {
                    List<QuestionModel> questions = (List<QuestionModel>) response.getData();

                    String logMessage = playerName + " successfully received " + questions.size() +
                            " " + category + " questions | IP: " + App.fetchIPAddress;
                    logManager.appendLog(logMessage);
                    ClientConnection.bombGameServer.logMessage(logMessage);

                    updateUI(() -> switchToGameplay(category, questions, event));
                } else {
                    logManager.appendLog("No questions found for category: " + category);
                }
            } catch (IOException e) {
                logManager.appendLog("Failed to fetch " + category + " questions: " + e.getMessage());
            }
        }).start();
    }

    private void switchToGameplay(String category, List<QuestionModel> questions, ActionEvent event) {
        String gameMode = isEndlessMode ? "/views/client/endless_game.fxml" : "/views/client/classic_game.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(gameMode));
            Parent root = loader.load();

            GameController gameController = loader.getController();
            gameController.setQuestions(category, questions, isEndlessMode);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            logManager.appendLog("Failed to fetch " + category + " questions: " + e.getMessage());
        }
    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            logManager.appendLog("Failed to fetch main menu screen" + e.getMessage());
        }
    }

    private void updateUI(Runnable action) {
        Platform.runLater(action);
    }
}