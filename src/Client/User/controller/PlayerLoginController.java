package Client.User.controller;
/**
 * Controls input username view window
 */
import common.AnsiFormatter;
import Client.User.connection.ClientConnection;
import exception.ConnectionException;
import exception.InvalidUsernameException;
import exception.ServerNotRunningException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerLoginController {
    private static final Logger logger = Logger.getLogger(PlayerLoginController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private static String playerUsername;
    private static String playerPassword;
    public PasswordField passwordField;
    public Button registerButton;

    private ClientConnection clientConnection;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    public PlayerLoginController() {
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
        loginButton.setOnAction(event -> handleLoginButtonClick(event));

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            errorLabel.setText("");
        });
    }

    private void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim().toLowerCase();
        String password = playerPassword;

        if (username.isEmpty()) {
            handleException(new InvalidUsernameException("Username cannot be empty!"));
            return;
        }

        playerUsername = username;
        playerPassword = password;
        logger.info("\nPlayerLoginController: Username entered: " + playerUsername);
        switchToMainMenu(event);
    }

    public static String getPlayerUsername() {
        return playerUsername;
    }
    public static String getPlayerPassword() {
        return playerPassword;
    }

    private void switchToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/main_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("\nPlayerLoginController: Switched to Main Menu.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Main Menu.", e);
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