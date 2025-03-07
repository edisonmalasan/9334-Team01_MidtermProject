package Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Manipulates mode view
 */
public class ModeController {
    private static final Logger logger = Logger.getLogger(ModeController.class.getName());

    @FXML
    public Button returnButton;

    @FXML
    private Button classicButton;

    @FXML
    private Button endlessButton;

    @FXML
    public void initialize() {
        classicButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Classic button clicked.");
            switchToCategory(actionEvent, false);
        });

        endlessButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Endless button clicked.");
            switchToCategory(actionEvent, true);
        });

        returnButton.setOnAction(actionEvent -> {
            logger.info("\nModeController: Return button clicked.");
            switchToMainMenu(actionEvent);
        });
    }

    private void switchToCategory(ActionEvent event, boolean isEndless) {
        CategoryController.isEndlessMode = isEndless;
        switchScene(event, "/views/category_menu.fxml", "Bomb Defusing Game");
    }

    private void switchToMainMenu(ActionEvent event) {
        switchScene(event, "/views/main_menu.fxml", "Bomb Defusing Game");
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
            logger.severe("Failed to load " + title + ": " + e.getMessage());
        }
    }
}