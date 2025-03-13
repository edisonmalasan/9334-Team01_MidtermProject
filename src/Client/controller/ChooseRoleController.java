package Client.controller;

import common.AnsiFormatter;
import exception.ConnectionException;
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

public class ChooseRoleController {
    private static final Logger logger = Logger.getLogger(ChooseRoleController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    private Button playerButton;

    @FXML
    private Button adminButton;

    @FXML
    public void initialize() {
        playerButton.setOnAction(actionEvent -> {
            logger.info("\nChooseRoleController: Player button clicked.");
            switchToInputUsername(actionEvent);
        });

        adminButton.setOnAction(actionEvent -> {
            logger.info("\nChooseRoleController: Admin button clicked.");
            switchToAdminLogin(actionEvent);
        });
    }

    private void switchToInputUsername(ActionEvent event) {
        switchScene(event, "/views/input_username.fxml", "Input Player Username");
    }

    private void switchToAdminLogin(ActionEvent event) {
        switchScene(event, "/views/admin_login.fxml", "Administrator View");
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
