package Client.Admin.controller;

import Client.common.connection.ClientConnection;
import Client.Player.utils.ClientSession;
import common.Log.LogManager;
import common.Response;
import common.model.PlayerModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class AdminDashboardController {
    private final LogManager logManager = LogManager.getInstance();
    private static final String USERS_JSON_PATH = "data/players.json";

    @FXML private HBox adminDashboard;
    @FXML private Label totalPlayersLabel;
    @FXML private Label classicPlayersLabel;
    @FXML private Label endlessPlayersLabel;
    @FXML private Button logoutButton;
    @FXML private Button playersButton;
    @FXML private Button questionsButton;

    @FXML
    public void initialize() {
        loadPlayerStatistics();
        setupButtonActions();
    }

    private void loadPlayerStatistics() {
        try {
            Response response = ClientConnection.bombGameServer.getPlayerList();
            List<PlayerModel> playerList = (List<PlayerModel>) response.getData();
            int totalPlayers = playerList.size();
            int classicPlayers = 0;
            int endlessPlayers = 0;

            for (PlayerModel player : playerList) {
                if (player.getHasPlayedClassic()) classicPlayers++;
                if (player.getHasPlayedEndless()) endlessPlayers++;
            }

            totalPlayersLabel.setText(String.valueOf(totalPlayers));
            classicPlayersLabel.setText(String.valueOf(classicPlayers));
            endlessPlayersLabel.setText(String.valueOf(endlessPlayers));

            logManager.appendLog("Admin dashboard loaded with " + totalPlayers + " players");

            if (ClientConnection.bombGameServer != null) {
                ClientConnection.bombGameServer.logMessage("Admin dashboard loaded with " + totalPlayers + " players");
            } else {
                logManager.appendLog("Warning: bombGameServer is null. Skipping logMessage.");
            }
        } catch (IOException e) {
            logManager.appendLog("Failed to load statistics: " + e.getMessage());
            totalPlayersLabel.setText("Error");
            classicPlayersLabel.setText("Error");
            endlessPlayersLabel.setText("Error");
        }
    }

    private void setupButtonActions() {
        logoutButton.setOnAction(event -> logoutAdmin(event));
        playersButton.setOnAction(event -> switchToScene("/views/admin/admin_leaderboard.fxml", "Player Management"));
        questionsButton.setOnAction(event -> switchToScene("/views/admin/admin_categories.fxml", "Question Management"));
    }

    private void logoutAdmin(ActionEvent event) {
        String adminUsername = ClientSession.getPlayerUsername();

        if (ClientConnection.bombGameServer != null && adminUsername != null) {
            try {
                ClientConnection.bombGameServer.logoutPlayer(adminUsername);
                ClientConnection.bombGameServer.logMessage("Admin " + adminUsername + " logged out.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ClientSession.clear();

        switchToScene("/views/client/login.fxml", "Login");
    }

    private void switchToScene(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) adminDashboard.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

            logManager.appendLog("Admin navigated to: " + title);
        } catch (IOException e) {
            logManager.appendLog("Failed to load scene: " + e.getMessage());
        }
    }
}
