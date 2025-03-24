package Client.User.controller;
/**
 * Controls input username view window
 */
import common.model.PlayerModel;
import common.LogManager;
import common.AnsiFormatter;
import exception.InvalidCredentialsException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    private final LogManager logManager = LogManager.getInstance();

    private static final String USERS_JSON_PATH = "data/players.json";

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    protected static PlayerModel currentUser;

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
        loginButton.setOnAction(this::handleLoginButtonClick);

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) errorLabel.setText("");
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) errorLabel.setText("");
        });

        if (registerLink != null) {
            registerLink.setOnAction(this::switchToRegister);
        }
    }

    protected void handleLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            handleException(new InvalidCredentialsException("Username or Password cannot be empty!"));
            return;
        }

        try {
            PlayerModel authenticatedUser = validateUser(username, password);
            if (authenticatedUser != null) {
                currentUser = authenticatedUser;
                logManager.appendLog("User logged in: " + currentUser.getUsername());

                if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                    logManager.appendLog("Admin " + currentUser.getUsername() + " logged in");
                    switchToAdminDashboard(event);
                } else {
                    logger.info("Player " + currentUser.getUsername() +
                            " logged in with scores - Classic: " +
                            currentUser.getClassicScore() +
                            ", Endless: " +
                            currentUser.getEndlessScore());
                    switchToMainMenu(event);
                }
            } else {
                handleException(new InvalidCredentialsException("Invalid username or password!"));
            }
        } catch (IOException e) {
            handleException(new InvalidCredentialsException("Login failed. Please try again later."));
            logger.log(Level.SEVERE, "Error reading user data", e);
        }
    }

    protected PlayerModel validateUser(String username, String password) throws IOException {
        try (FileReader reader = new FileReader(USERS_JSON_PATH)) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray playersArray = new JSONArray(tokener);

            for (int i = 0; i < playersArray.length(); i++) {
                JSONObject playerJson = playersArray.getJSONObject(i);
                PlayerModel player = new PlayerModel(
                        playerJson.getString("username"),
                        playerJson.getString("password"),
                        playerJson.getString("role"),
                        playerJson.getInt("classicScore"),
                        playerJson.getInt("endlessScore")
                );

                if (player.getUsername().equalsIgnoreCase(username) &&
                        player.getPassword().equals(password)) {
                    return player;
                }
            }
            return null;
        }
    }

    private void handleException(Exception e) {
        logManager.appendLog("ERROR: " + e.getMessage());
        Platform.runLater(() -> {
            usernameField.clear();
            usernameField.requestFocus();
            errorLabel.setText(e.getMessage());
        });
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // changed to Node instead of Button to check the type dynamically
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();

            logger.info("\nLoginController: Switched to " + title);

            //clear data
            usernameField.clear();
            passwordField.clear();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load " + title, e);
        }
    }

    private void switchToRegister(ActionEvent event) {
        switchScene(event, "/views/client/register.fxml", "Register - Bomb Defusing Game");
    }

    private void switchToMainMenu(ActionEvent event) {
        switchScene(event, "/views/client/main_menu.fxml", "Bomb Defusing Game");
    }

    private void switchToAdminDashboard(ActionEvent event) {
        switchScene(event, "/views/admin/admin_db.fxml", "Admin Dashboard");
    }

    public static PlayerModel getCurrentUser() {
        return currentUser;
    }

    //    private void switchToMainMenu(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/main_menu.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.setTitle("Bomb Defusing Game");
//            stage.setResizable(false);
//            stage.show();
//
//            logger.info("\nLoginController: Switched to Main Menu.");
//        } catch (IOException e) {
//            logger.log(Level.SEVERE, "Failed to load Main Menu.", e);
//        }
//    }
}