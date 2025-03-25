package Client.Admin.controller.madeByDannievicForReference;
/**
 * Controls input username view window
 */
import Client.Player.controller.LoginController;
import common.Log.AnsiFormatter;
import Client.common.connection.ClientConnection;
import exception.ConnectionException;
import exception.InvalidCredentialsException;
import exception.ServerNotRunningException;
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

public class AdminLoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private static String adminPass;

    private ClientConnection clientConnection;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button enterButton;

    public AdminLoginController() {
        try {
            this.clientConnection = ClientConnection.getInstance();

            if (this.clientConnection == null) {
                logger.severe("❌ ERROR: ClientConnection is NULL! Server may not be running.");
            } else {
                logger.info("✅ Client is set up properly.");
            }

        } catch (ConnectionException e) {
            handleException(new ServerNotRunningException("⚠ Server is not running."));
        }
    }

    @FXML
    public void initialize() {
        enterButton.setOnAction(event -> handleEnterButtonClick(event));

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });
    }

    private void handleEnterButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim().toLowerCase();

        if (username.isEmpty()) {
            handleException(new InvalidCredentialsException("Username cannot be empty!"));
            return;
        }
        if (!username.equals("admin")){
            handleException(new InvalidCredentialsException("Incorrect!"));
            return;
        }
        adminPass = username;
        logger.info("\nAdminLoginController: Username entered: " + adminPass);

        switchToAdminMenu(event);
    }

    private void switchToAdminMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Redundant/admin_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("\nAdminLoginController: Switched to Client.Admin Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load ADmin Menu.", e);
        }
    }

    private void handleException(Exception e) {
        logger.severe("❌ ERROR: " + e.getMessage());
        Platform.runLater(() -> {
            usernameField.clear();
            usernameField.requestFocus();
            errorLabel.setText(e.getMessage());
        });
    }
}