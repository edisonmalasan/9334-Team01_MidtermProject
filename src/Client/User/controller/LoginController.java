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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    private static String playerUsername;
    private static String playerPassword;

    @FXML
    private TextField usernameField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Hyperlink registerLink;

    public LoginController() {
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

        registerLink.setOnAction(event -> switchToRegister(event));


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

            logger.info("\nLoginController: Switched to Main Menu.");
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

    private void switchToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Hyperlink) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Register - Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("\nLoginController: Switched to Register Page.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Register Page.", e);
        }
    }

}