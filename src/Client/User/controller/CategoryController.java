package Client.User.controller;
/**
 * Controls category view
 */
import App.App;
import common.AnsiFormatter;
import Client.User.connection.ClientConnection;
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
import utility.BombGameServer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public BombGameServer bombGameServer = App.bombGameServer;

    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public CategoryController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Failed to initialize ClientConnection.", e);
        }
    }

    @FXML
    public void initialize() {
        algebraButton.setOnAction(event -> requestQuestionFromServer("ALGEBRA", event));
        anglesButton.setOnAction(event -> requestQuestionFromServer("ARITHMETIC", event)); // ANGLES
        geometryButton.setOnAction(event -> requestQuestionFromServer("GEOMETRY", event));
        logicButton.setOnAction(event -> requestQuestionFromServer("LOGIC", event));
        probabilityButton.setOnAction(event -> requestQuestionFromServer("PROBABILITY", event));
        trigoButton.setOnAction(event -> requestQuestionFromServer("TRIGONOMETRY", event));
        returnButton.setOnAction(event -> switchToMainMenu());
    }

    private void requestQuestionFromServer(String category, ActionEvent event) {
        selectedCategory = category;
        logger.info("\nCategoryController: Requesting questions for category: " + category);

        new Thread(() -> {
            try {
                Response response = bombGameServer.getQuestionsPerCategory(category);

                logger.info("\nCategoryController: DEBUG: Received response from server: " + response);

                if (response.isSuccess() && response.getData() instanceof List) {
                    List<QuestionModel> questions = (List<QuestionModel>) response.getData();

                    logger.info("CategoryController: Received " + questions.size() + " questions for category: " + category);

                    // log formatted questions for debug purpose
                    for (int i = 0; i < questions.size(); i++) {
                        QuestionModel q = questions.get(i);
                        logger.info("\nCategoryController: \n📌 Question " + (i + 1) + ":\n"
                                + "   🏷 Category: " + q.getCategory() + "\n"
                                + "   ❓ Question: " + q.getQuestionText() + "\n"
                                + "   🔢 Choices: " + q.getChoices() + "\n"
                                + "   ✅ Correct Answer: " + q.getCorrectAnswer() + "\n"
                                + "   ⭐ Score: " + q.getScore() + "\n"
                                + "--------------------------------------");
                    }

                    updateUI(() -> switchToGameplay(category, questions, event));
                } else {
                    logger.warning("CategoryController: No questions found for category: " + category);
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "CategoryController: Failed to fetch questions from server.", e);
            }
        }).start();
    }

    private void switchToGameplay(String category, List<QuestionModel> questions, ActionEvent event) {
        String gameMode = (isEndlessMode) ? "/views/client/endless_game.fxml" : "/views/client/classic_game.fxml";
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

            logger.info("Switched to " + ((isEndlessMode) ? "Endless Mode" : "Classic Mode") + " gameplay.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load game mode.", e);
        }
    }

    private void switchToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("ScoreController: Successfully switched to the Main Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "ScoreController: Failed to load Main Menu", e);
        }
    }

    private void updateUI(Runnable action) {
        Platform.runLater(action);
    }
}
