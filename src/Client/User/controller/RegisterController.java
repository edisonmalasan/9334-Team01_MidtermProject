package Client.User.controller;

import Client.connection.ClientConnection;
import common.Response;
import common.model.PlayerModel;
import common.Log.LogManager;
import common.Log.AnsiFormatter;
import exception.ConnectionException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import App.App;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private final LogManager logManager = LogManager.getInstance();
    protected ClientConnection clientConnection;


    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink loginLink;
    @FXML private Button registerButton;
    @FXML private Label usernameErrorLabel;
    @FXML private Label passwordErrorLabel;
    @FXML private Label confirmPasswordErrorLabel;
    @FXML private Label generalErrorLabel;

    public RegisterController() {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
            logManager.appendLog("Registration error: Failed to initialize connection");
        }
    }

    @FXML
    public void initialize() {
        registerButton.setOnAction(this::handleRegister);
        loginLink.setOnAction(this::switchToLogin);

        // Clear error when typing
        usernameField.textProperty().addListener((o, oldVal, newVal) -> validateUsername());
        passwordField.textProperty().addListener((o, oldVal, newVal) -> validatePassword());
        confirmPasswordField.textProperty().addListener((o, oldVal, newVal) -> validatePasswordMatch());
    }

    private void handleRegister(ActionEvent event) {
        clearAllErrors();

        if (!validateAllFields()) {
            return;
        }

        String username = usernameField.getText().trim().toUpperCase();
        String password = passwordField.getText().trim();

        try {
            Response response = ClientConnection.bombGameServer.getPlayerList();
            if (usernameExists((List<PlayerModel>) response.getData(), username)) {
                showFieldError(usernameErrorLabel, "Username already taken!");
                usernameField.requestFocus();
                logManager.appendLog("Registration failed: Username " + username + " already exists");
                return;
            }

            // register new player
            ClientConnection.bombGameServer.register(username, password);

            // log registration to server view
            String logMessage = "New Player Registered: " + username + " | IP: " + App.fetchIPAddress;
            ClientConnection.bombGameServer.logMessage(logMessage);
            logManager.appendLog(logMessage);

            showSuccessAndRedirect(event);

        } catch (Exception e) {
            showGeneralError("Registration failed: " + e.getMessage());
            logManager.appendLog("Registration error for " + username + ": " + e.getMessage());
        }
    }

    private boolean usernameExists(List<PlayerModel> playerList, String username) {
        for (PlayerModel player : playerList) {
            if (player.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        if (!validateUsername()) {
            isValid = false;
        }

        if (!validatePassword()) {
            isValid = false;
        }

        if (!validatePasswordMatch()) {
            isValid = false;
        }

        return isValid;
    }

    private boolean validateUsername() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showFieldError(usernameErrorLabel, "Username is required");
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9]{3,20}$")) {
            showFieldError(usernameErrorLabel, "3-20 alphanumeric characters");
            return false;
        }

        clearFieldError(usernameErrorLabel);
        return true;
    }

    private boolean validatePassword() {
        String password = passwordField.getText().trim();
        if (password.isEmpty()) {
            showFieldError(passwordErrorLabel, "Password is required");
            return false;
        }

        if (password.length() < 6) {
            showFieldError(passwordErrorLabel, "Minimum 6 characters");
            return false;
        }

        clearFieldError(passwordErrorLabel);
        return true;
    }

    private boolean validatePasswordMatch() {
        String password = passwordField.getText().trim();
        String confirm = confirmPasswordField.getText().trim();

        if (!password.equals(confirm)) {
            showFieldError(confirmPasswordErrorLabel, "Passwords don't match");
            return false;
        }

        clearFieldError(confirmPasswordErrorLabel);
        return true;
    }

    private void showFieldError(Label errorLabel, String message) {
        Platform.runLater(() -> {
            errorLabel.setText(message);
            errorLabel.setTextFill(Color.RED);
            errorLabel.setVisible(true);
        });
    }

    private void clearFieldError(Label errorLabel) {
        Platform.runLater(() -> {
            errorLabel.setText("");
            errorLabel.setVisible(false);
        });
    }

    private void clearAllErrors() {
        clearFieldError(usernameErrorLabel);
        clearFieldError(passwordErrorLabel);
        clearFieldError(confirmPasswordErrorLabel);
        clearFieldError(generalErrorLabel);
    }

    private void showGeneralError(String message) {
        Platform.runLater(() -> {
            generalErrorLabel.setText(message);
            generalErrorLabel.setTextFill(Color.RED);
            generalErrorLabel.setVisible(true);
        });
    }

    private void showSuccessAndRedirect(ActionEvent event) {
        Platform.runLater(() -> {
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Registration Successful");
            success.setHeaderText(null);
            success.setContentText("Account created successfully!");
            success.showAndWait();
            switchToLogin(event);
        });
    }

    private void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Bomb Defusing Game");
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load login fxml" + e.getMessage());
        }
    }
}