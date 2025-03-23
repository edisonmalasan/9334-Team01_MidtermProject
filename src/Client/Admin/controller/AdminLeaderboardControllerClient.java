package Client.Admin.controller;
/**
 * Controls leaderboard view for admin
 */
import App.App;
import Client.User.model.LeaderboardEntryModelClient;
import utility.LeaderboardEntryModel;
import common.Response;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminLeaderboardControllerClient {
    // FXML Elements
    public TableColumn classicRank;
    public TableColumn endlessRank;
    public Button returnButton;
    public Button deleteButton;
    @FXML
    private TableView<LeaderboardEntryModel> classicTable;

    @FXML
    private TableColumn<LeaderboardEntryModel, String> classicUsername;

    @FXML
    private TableColumn<LeaderboardEntryModel, Integer> classicScore;

    @FXML
    private TableView<LeaderboardEntryModel> endlessTable;

    @FXML
    private TableColumn<LeaderboardEntryModel, String> endlessUsername;

    @FXML
    private TableColumn<LeaderboardEntryModel, Integer> endlessScore;

    @FXML
    private TextField classicSearchBox;

    @FXML
    private TextField endlessSearchBox;

    // Observable list to hold leaderboard data
    private ObservableList<LeaderboardEntryModel> classicLeaderboard;
    private ObservableList<LeaderboardEntryModel> endlessLeaderboard;

    public AdminLeaderboardControllerClient() {
    }

    public void initialize() throws IOException, ClassNotFoundException {
        // Initialize table columns for Classic leaderboard
        classicUsername.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPlayerName()));
        classicScore.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getScore()).asObject());

        // Initialize table columns for Endless leaderboard
        endlessUsername.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPlayerName()));
        endlessScore.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getScore()).asObject());

        // Initialize leaderboard data (this data would be fetched from a server in a real-world scenario)
        classicLeaderboard = FXCollections.observableArrayList(
                (List<LeaderboardEntryModel>) App.bombGameServer.getLeaderboards("classic").getData()
        );

        endlessLeaderboard = FXCollections.observableArrayList(
                (List<LeaderboardEntryModel>) App.bombGameServer.getLeaderboards("endless").getData()
        );

        // Sort the leaderboard based on score in descending order
        sortLeaderboardByScore(classicLeaderboard);
        sortLeaderboardByScore(endlessLeaderboard);

        // Set the data to the tables
        classicTable.setItems(classicLeaderboard);
        endlessTable.setItems(endlessLeaderboard);

        // Initialize Rank columns to compute the rank dynamically
        classicRank.setCellValueFactory((Callback<TableColumn.CellDataFeatures<LeaderboardEntryModelClient, Integer>, ObservableValue<Integer>>) param -> new SimpleIntegerProperty(getRankForIndex(classicLeaderboard, classicLeaderboard.indexOf(param.getValue()))).asObject());

        endlessRank.setCellValueFactory((Callback<TableColumn.CellDataFeatures<LeaderboardEntryModelClient, Integer>, ObservableValue<Integer>>) param -> new SimpleIntegerProperty(getRankForIndex(endlessLeaderboard, endlessLeaderboard.indexOf(param.getValue()))).asObject());

        // Search box handlers for filtering Classic and Endless tables
        classicSearchBox.textProperty().addListener((observable, oldValue, newValue) -> filterLeaderboardData(newValue, "classic"));
        endlessSearchBox.textProperty().addListener((observable, oldValue, newValue) -> filterLeaderboardData(newValue, "endless"));

        deleteButton.setOnAction(event -> deleteEntry());
        returnButton.setOnAction(event -> returnToMainMenu());
    }

    public void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sort leaderboard data based on score in descending order.
     * In case of a tie, it sorts alphabetically by the player name.
     *
     * @param leaderboard The leaderboard to be sorted.
     */
    private void sortLeaderboardByScore(ObservableList<LeaderboardEntryModel> leaderboard) {
        leaderboard.sort((entry1, entry2) -> {
            // Compare scores in descending order
            int scoreComparison = Integer.compare(entry2.getScore(), entry1.getScore());
            if (scoreComparison == 0) {
                // If scores are equal, sort alphabetically by player name
                return entry1.getPlayerName().compareTo(entry2.getPlayerName());
            }
            return scoreComparison;
        });
    }

    /**
     * Get the rank for a given index based on the leaderboard sorting.
     * In case of a tie, it assigns the same rank to entries with equal scores.
     *
     * @param leaderboard The leaderboard.
     * @param index The index of the current entry.
     * @return The rank (1-based index) of the entry.
     */
    private int getRankForIndex(ObservableList<LeaderboardEntryModel> leaderboard, int index) {
        if (index == 0) {
            return 1;
        } else {
            LeaderboardEntryModel previous = leaderboard.get(index - 1);
            LeaderboardEntryModel current = leaderboard.get(index);

            // If scores are the same, assign the same rank
            if (previous.getScore() == current.getScore()) {
                return getRankForIndex(leaderboard, index - 1);
            } else {
                return index + 1; // Rank is 1-based index
            }
        }
    }

    /**
     * Filter leaderboard data based on the search query.
     *
     * @param query Search text.
     * @param type  Type of leaderboard (classic or endless).
     */
    private void filterLeaderboardData(String query, String type) {
        ObservableList<LeaderboardEntryModel> filteredList = FXCollections.observableArrayList();

        // Apply search filter based on the type of leaderboard
        if ("classic".equalsIgnoreCase(type)) {
            for (LeaderboardEntryModel entry : classicLeaderboard) {
                if (entry.getPlayerName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(entry);
                }
            }
            classicTable.setItems(filteredList);
        } else if ("endless".equalsIgnoreCase(type)) {
            for (LeaderboardEntryModel entry : endlessLeaderboard) {
                if (entry.getPlayerName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(entry);
                }
            }
            endlessTable.setItems(filteredList);
        }
    }

    @FXML
    private void deleteEntry() {
        // Get selected item from the classicTable or endlessTable
        String leaderboardType = "classic";
        LeaderboardEntryModel selectedItem = classicTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            leaderboardType = "endless";
            selectedItem = endlessTable.getSelectionModel().getSelectedItem();
        }

        try {
            Response response = App.bombGameServer.removeFromLeaderboard(selectedItem, leaderboardType);
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (selectedItem != null) {
            // Here, you can remove the selected item from the table
            if (classicTable.getSelectionModel().getSelectedItem() != null) {
                classicTable.getItems().remove(selectedItem);
            } else {
                endlessTable.getItems().remove(selectedItem);
            }

            // Optionally show a confirmation or success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Item Deleted");
            alert.setContentText("The selected entry has been deleted successfully.");
            alert.showAndWait();
        } else {
            // If no item is selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No item selected");
            alert.setContentText("Please select an entry to delete.");
            alert.showAndWait();
        }
    }
}