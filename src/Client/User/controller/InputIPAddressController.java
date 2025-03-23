package Client.User.controller;
/**
 * Controls input username view window
 */
import common.AnsiFormatter;
import exception.InvalidUsernameException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InputIPAddressController {
    private static final Logger logger = Logger.getLogger(InputIPAddressController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    @FXML
    private TextField inputField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button enterButton;
    public static String ipAddress;

    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> handleEnterButtonClick(event));

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });
    }

    public void handleEnterButtonClick(ActionEvent event) {
        ipAddress = inputField.getText().trim();

        if (ipAddress.isEmpty()) {
            handleException(new InvalidUsernameException("IP Address cannot be empty!"));
            return;
        }

        logger.info("\nInputIPController: Username entered: " + ipAddress);
        switchToChooseRole(event);
    }
    private void switchToChooseRole(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/choose_role.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Choose Client.User Role");
            stage.setResizable(false);
            stage.show();

            logger.info("\nInputIPAddressController: Switched to Choose Role.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Choose Role.", e);
        }
    }
    private void handleException(Exception e) {
        logger.severe("❌ ERROR: " + e.getMessage());
        Platform.runLater(() -> {
            inputField.clear();
            inputField.requestFocus();
            errorLabel.setText(e.getMessage());
        });
    }
}