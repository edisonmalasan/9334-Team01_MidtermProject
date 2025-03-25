package Redundant;

import Client.Admin.model.PlayerModelAdmin;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.util.Optional;

/**
 * Manipulates the admin GUI with JSON data instead of XML
 */
public class PlayerControllerAdmin_kezpk {

    @FXML
    private TableView<PlayerModelAdmin> userTable;
    @FXML
    private TableColumn<PlayerModelAdmin, String> username;
    @FXML
    private TableColumn<PlayerModelAdmin, Integer> classicRank;
    @FXML
    private TableColumn<PlayerModelAdmin, Integer> classicScore;
    @FXML
    private TableColumn<PlayerModelAdmin, Integer> endlessRank;
    @FXML
    private Button editButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteBttn;

    private ObservableList<PlayerModelAdmin> playerData = FXCollections.observableArrayList();
    private File jsonFile = new File("players.json");

    @FXML
    public void initialize() {
        setupTable();
        loadJSON();

        editButton.setOnAction(e -> enableEditing());
        saveButton.setOnAction(e -> saveChanges());
        deleteBttn.setOnAction(e -> deleteSelectedPlayer());
    }

    private void setupTable() {
        // Set up the cell value factories for the table columns
        username.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        classicRank.setCellValueFactory(cellData -> cellData.getValue().classicRankProperty().asObject());
        classicScore.setCellValueFactory(cellData -> cellData.getValue().classicScoreProperty().asObject());
        endlessRank.setCellValueFactory(cellData -> cellData.getValue().endlessRankProperty().asObject());

        // Set cell factories for editable columns
        username.setCellFactory(TextFieldTableCell.forTableColumn());
        classicRank.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        classicScore.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        endlessRank.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        userTable.setItems(playerData);
    }

    private void loadJSON() {
        playerData.clear();
        try {
            if (jsonFile.exists()) {

                StringBuilder content = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(jsonFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();

                // Parse the content as a JSON array
                JSONArray jsonArray = new JSONArray(content.toString());

                // Loop through each player in the JSON array
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject playerObject = jsonArray.getJSONObject(i);
                    String name = playerObject.getString("username");
                    int rank = playerObject.getInt("classicRank");
                    int score = playerObject.getInt("classicScore");
                    int endless = playerObject.getInt("endlessRank");

                    playerData.add(new PlayerModelAdmin(name, rank, score, endless));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        try {

            JSONArray jsonArray = new JSONArray();

            // Iterate over player data and create a JSON object for each player
            for (PlayerModelAdmin player : playerData) {
                JSONObject playerObject = new JSONObject();
                playerObject.put("username", player.getUsername());
                playerObject.put("classicRank", player.getClassicRank());
                playerObject.put("classicScore", player.getClassicScore());
                playerObject.put("endlessRank", player.getEndlessRank());

                jsonArray.put(playerObject);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
            writer.write(jsonArray.toString(4));
            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Player data saved successfully!");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enableEditing() {
        userTable.setEditable(true);
    }

    public void deleteSelectedPlayer() {
        PlayerModelAdmin selectedPlayer = userTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete " + selectedPlayer.getUsername() + "?");
            alert.setContentText("Are you sure you want to remove this player?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                playerData.remove(selectedPlayer);
                saveChanges();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Player Selected");
            alert.setContentText("Please select a player to delete.");
            alert.showAndWait();
        }
    }
}