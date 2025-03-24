package Client.User.controller;

import common.model.PlayerModel;
import common.LogManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());
    private final LogManager logManager = LogManager.getInstance();
    private static final String USERS_JSON_PATH = "data/players.json";

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
            JSONArray usersArray = readOrCreateUserArray();
            if (usernameExists(usersArray, username)) {
                showFieldError(usernameErrorLabel, "Username already taken!");
                usernameField.requestFocus();
                return;
            }

            addNewUserToJson(usersArray, username, password);
            showSuccessAndRedirect(event);

        } catch (Exception e) {
            showGeneralError("Registration failed: " + e.getMessage());
            logger.log(Level.SEVERE, "Registration error", e);
        }
    }

    private boolean usernameExists(JSONArray usersArray, String username) {
        for (int i = 0; i < usersArray.length(); i++) {
            if (usersArray.getJSONObject(i).getString("username").equalsIgnoreCase(username)) {
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
        errorLabel.setText(message);
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(true);
    }

    private void clearFieldError(Label errorLabel) {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    private void clearAllErrors() {
        clearFieldError(usernameErrorLabel);
        clearFieldError(passwordErrorLabel);
        clearFieldError(confirmPasswordErrorLabel);
        clearFieldError(generalErrorLabel);
    }

    private void showGeneralError(String message) {
        generalErrorLabel.setText(message);
        generalErrorLabel.setTextFill(Color.RED);
        generalErrorLabel.setVisible(true);
    }

    private void validateRegistration(String username, String password, String confirmPassword) {
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        if (!username.matches("^[a-zA-Z0-9]}$")) {
            throw new IllegalArgumentException("Username must be alphanumeric characters");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }
    }

    private JSONArray readOrCreateUserArray() throws IOException {
        File file = new File(USERS_JSON_PATH);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("[]");
            }
            return new JSONArray();
        }

        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        }
    }

    private void checkUsernameAvailability(JSONArray usersArray, String username) {
        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject user = usersArray.getJSONObject(i);
            if (user.getString("username").equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Username already taken!");
            }
        }
    }

    private void addNewUserToJson(JSONArray usersArray, String username, String password) throws IOException {
        JSONObject newUser = new JSONObject();
        newUser.put("username", username);
        newUser.put("password", password);
        newUser.put("role", "PLAYER"); //default role
        newUser.put("classicScore", 0); //default scores
        newUser.put("endlessScore", 0);

        usersArray.put(newUser);

        try (FileWriter writer = new FileWriter(USERS_JSON_PATH)) {
            writer.write(usersArray.toString(4));
        }
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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/views/client/login.fxml")));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Bomb Defusing Game");
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to switch to login", e);
        }
    }

    private void handleRegistrationError(Exception e) {
        Platform.runLater(() -> {
            errorLabel.setText(e.getMessage());
            passwordField.clear();
            confirmPasswordField.clear();
            usernameField.requestFocus();
        });
        logger.warning("Registration error: " + e.getMessage());
    }
}