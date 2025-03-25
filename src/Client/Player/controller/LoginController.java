package Client.Player.controller;
/**
 * Controls input username view window
 */
import Client.CallbackImpl;
import Client.connection.ClientConnection;
import common.model.PlayerModel;
import exception.ConnectionException;
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
import utility.Callback;
import App.App;
import java.io.IOException;

public class LoginController {

    protected ClientConnection clientConnection;


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
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
           System.out.println("\nGameController: Error initializing ClientConnection." + e.getMessage());
        }
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
            PlayerModel authenticatedUser = ClientConnection.bombGameServer.getPlayer(username, password);
            if (authenticatedUser != null) {
                Callback callback = new CallbackImpl(authenticatedUser);
                ClientConnection.bombGameServer.login(callback);
                currentUser = authenticatedUser;

                // send log to server
                String logMessage = "Log In: " + currentUser.getUsername() + " | IP: " + App.fetchIPAddress;
                ClientConnection.bombGameServer.logMessage(logMessage);

                if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                    ClientConnection.bombGameServer.logMessage("Admin " + currentUser.getUsername() + " logged in");
                    switchToAdminDashboard(event);
                } else {
                    ClientConnection.bombGameServer.logMessage("Player " + currentUser.getUsername() +
                            " logged in. Classic Score: " + currentUser.getClassicScore() +
                            ", Endless Score: " + currentUser.getEndlessScore());
                    switchToMainMenu(event);
                }
            } else {
                handleException(new InvalidCredentialsException("Invalid username or password!"));
                errorLabel.setText("\"Invalid username or password!\"");
            }
        } catch (IOException e) {
            handleException(new InvalidCredentialsException("Login failed. Please try again later."));
            System.out.println("Error reading user data" + e.getMessage());
        }
    }

    private void handleException(Exception e) {
        Platform.runLater(() -> {
            usernameField.clear();
            usernameField.requestFocus();
            passwordField.clear();
            errorLabel.setText(e.getMessage());
        });
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

    private void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // changed to Node instead of Button to check the type dynamically
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();

            //clear data
            usernameField.clear();
            passwordField.clear();
        } catch (IOException e) {
            System.out.println("Failed to load " + fxmlPath + ": " + e.getMessage());
        }
    }
}