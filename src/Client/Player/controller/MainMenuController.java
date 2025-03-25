package Client.Player.controller;
/**
 * Controls main menu view window
 */
import Client.Player.utils.ClientSession;
import Client.connection.ClientConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

public class MainMenuController {

    @FXML
    private Button playButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        playButton.setOnAction(event -> switchToModeMenu(event));
        leaderboardButton.setOnAction(event -> switchToLeaderboard(event));
        logoutButton.setOnAction(event -> logoutPlayer(event));
    }

    private void switchToModeMenu(ActionEvent event) {
        switchScene(event, "/views/client/mode_menu.fxml", "Bomb Defusing Game");
    }

    private void switchToLeaderboard(ActionEvent event) {
        switchScene(event, "/views/client/leaderboard.fxml", "Leaderboard");
    }

    private void logoutPlayer(ActionEvent event) {
        String username = ClientSession.getPlayerUsername(); // retrieve the logged-in player's username

        if (ClientConnection.bombGameServer != null && username != null) {
            try {
                ClientConnection.bombGameServer.logoutPlayer(username); // pass username in server if logout
                ClientConnection.bombGameServer.logMessage("Player " + username + " logged out.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ClientSession.clear();

        switchScene(event, "/views/client/login.fxml", "Login - Bomb Defusing Game");
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

            ClientConnection.bombGameServer.logMessage("Switched to " + title);
        } catch (IOException e) {
            try {
                ClientConnection.bombGameServer.logMessage("❌ Failed to load " + title);
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}