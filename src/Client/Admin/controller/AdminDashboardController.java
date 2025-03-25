package Client.Admin.controller;

import Client.connection.ClientConnection;
import common.Log.LogManager;
import common.Log.AnsiFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminDashboardController {
    private final LogManager logManager = LogManager.getInstance();
    private static final String USERS_JSON_PATH = "data/players.json";

    @FXML private HBox adminDashboard;
    @FXML private Label totalPlayersLabel;
    @FXML private Label classicPlayersLabel;
    @FXML private Label endlessPlayersLabel;
    @FXML private Button exitButton;
    @FXML private Button playersButton;
    @FXML private Button questionsButton;

    @FXML
    public void initialize() {
        loadPlayerStatistics();
        setupButtonActions();
    }

    private void loadPlayerStatistics() {
        try {
            JSONArray players = readPlayersFile();
            int totalPlayers = players.length();
            int classicPlayers = 0;
            int endlessPlayers = 0;

            //count players with scores >0 for each mode (ganyan ba or live tracking)
            for (int i = 0; i < players.length(); i++) {
                JSONObject player = players.getJSONObject(i);
                if (player.getInt("classicScore") > 0) classicPlayers++;
                if (player.getInt("endlessScore") > 0) endlessPlayers++;
            }

            totalPlayersLabel.setText(String.valueOf(totalPlayers));
            classicPlayersLabel.setText(String.valueOf(classicPlayers));
            endlessPlayersLabel.setText(String.valueOf(endlessPlayers));

            logManager.appendLog("Admin dashboard loaded with " + totalPlayers + " players");
            ClientConnection.bombGameServer.logMessage("Admin dashboard loaded with " + totalPlayers + " players");

        } catch (IOException e) {
            logManager.appendLog("Failed to load statistics: " + e.getMessage());
            totalPlayersLabel.setText("Error");
            classicPlayersLabel.setText("Error");
            endlessPlayersLabel.setText("Error");
        }
    }

    private JSONArray readPlayersFile() throws IOException {
        File file = new File(USERS_JSON_PATH);
        if (!file.exists()) {
            return new JSONArray();
        }

        try (FileReader reader = new FileReader(file)) {
            return new JSONArray(new JSONTokener(reader));
        }
    }

    private void setupButtonActions() {
        exitButton.setOnAction(event -> switchToScene("/views/client/login.fxml", "Login"));
        playersButton.setOnAction(event -> switchToScene("/views/admin_leaderboard.fxml", "Player Management"));
        questionsButton.setOnAction(event -> switchToScene("/views/admin/admin_categories.fxml", "Question Management"));
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