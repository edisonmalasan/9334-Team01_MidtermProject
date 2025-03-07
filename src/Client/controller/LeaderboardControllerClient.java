package Client.controller;
/**
 * Controls leaderboard view
 */
import Client.connection.ClientConnection;
import Client.model.LeaderboardEntryModelClient;
import Server.model.LeaderboardEntryModelServer;
import common.Response;
import exception.ConnectionException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LeaderboardControllerClient {
    // FXML Elements
    public TableColumn classicRank;
    public TableColumn endlessRank;
    public Button returnButton;
    @FXML
    private TableView<LeaderboardEntryModelServer> classicTable;

    @FXML
    private TableColumn<LeaderboardEntryModelServer, String> classicUsername;

    @FXML
    private TableColumn<LeaderboardEntryModelServer, Integer> classicScore;

    @FXML
    private TableView<LeaderboardEntryModelServer> endlessTable;

    @FXML
    private TableColumn<LeaderboardEntryModelServer, String> endlessUsername;

    @FXML
    private TableColumn<LeaderboardEntryModelServer, Integer> endlessScore;

    @FXML
    private TextField classicSearchBox;

    @FXML
    private TextField endlessSearchBox;

    @FXML
    private ImageView classicImageView;

    @FXML
    private ImageView endlessImageView;

    // Observable list to hold leaderboard data
    private ObservableList<LeaderboardEntryModelServer> classicLeaderboard;
    private ObservableList<LeaderboardEntryModelServer> endlessLeaderboard;
    private ClientConnection clientConnection;

    public LeaderboardControllerClient() throws ConnectionException {
        this.clientConnection = ClientConnection.getInstance();
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
                getClassicLeaderboard()
        );

        endlessLeaderboard = FXCollections.observableArrayList(
                getEndlessLeaderboard()
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

        returnButton.setOnAction(event -> returnToMainMenu());
    }

    public void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/main_menu.fxml"));
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
    private void sortLeaderboardByScore(ObservableList<LeaderboardEntryModelServer> leaderboard) {
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
    private int getRankForIndex(ObservableList<LeaderboardEntryModelServer> leaderboard, int index) {
        if (index == 0) {
            return 1;
        } else {
            LeaderboardEntryModelServer previous = leaderboard.get(index - 1);
            LeaderboardEntryModelServer current = leaderboard.get(index);

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
        ObservableList<LeaderboardEntryModelServer> filteredList = FXCollections.observableArrayList();

        // Apply search filter based on the type of leaderboard
        if ("classic".equalsIgnoreCase(type)) {
            for (LeaderboardEntryModelServer entry : classicLeaderboard) {
                if (entry.getPlayerName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(entry);
                }
            }
            classicTable.setItems(filteredList);
        } else if ("endless".equalsIgnoreCase(type)) {
            for (LeaderboardEntryModelServer entry : endlessLeaderboard) {
                if (entry.getPlayerName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(entry);
                }
            }
            endlessTable.setItems(filteredList);
        }
    }

    public List<LeaderboardEntryModelServer> getClassicLeaderboard() {
        List<LeaderboardEntryModelServer> leaderboardEntryModelServerList = new ArrayList<>();
        try {
            // send obj req
            clientConnection.sendObject("GET_LEADERBOARD_CLASSIC");

            // receive the response obj from server
            Response response = (Response) clientConnection.receiveObject();

            if (response.isSuccess() && response.getData() instanceof List) {
                leaderboardEntryModelServerList = (List<LeaderboardEntryModelServer>) response.getData();
                System.out.println(response.getData().toString());
            }
            return leaderboardEntryModelServerList;
        } catch (IOException | ClassNotFoundException e) {
            return leaderboardEntryModelServerList;
        }
    }

    public List<LeaderboardEntryModelServer> getEndlessLeaderboard() {
        List<LeaderboardEntryModelServer> leaderboardEntryModelServerList = new ArrayList<>();
        try {
            // send obj req
            clientConnection.sendObject("GET_LEADERBOARD_ENDLESS");

            // receive the response obj from server
            Response response = (Response) clientConnection.receiveObject();

            if (response.isSuccess() && response.getData() instanceof List) {
                leaderboardEntryModelServerList = (List<LeaderboardEntryModelServer>) response.getData();
                System.out.println(response.getData().toString());
            }
            return leaderboardEntryModelServerList;
        } catch (IOException | ClassNotFoundException e) {
            return leaderboardEntryModelServerList;
        }
    }
}