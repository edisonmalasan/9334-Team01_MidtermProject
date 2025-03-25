package Client.Admin.controller;

import App.App;
import Client.common.connection.ClientConnection;
import common.Response;
import common.model.PlayerModel;
import common.model.QuestionModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdminPlayersController {
    public Button saveListButton;
    public TableColumn<PlayerModel, String> usernameColumn;
    public TableColumn<PlayerModel, String> passwordColumn;
    public TableColumn<PlayerModel, String> roleColumn;
    public TableColumn<PlayerModel, Integer> classicScoreColumn;
    public TableColumn<PlayerModel, Integer> endlessScoreColumn;
    public TableColumn<PlayerModel, Boolean> hasPlayedClassicColumn;
    public TableColumn<PlayerModel, Boolean> hasPlayedEndlessColumn;
    public TableView<PlayerModel> playerTable;
    public TextField searchField;
    public Button deleteButton;
    public Button returnButton;
    private ObservableList<PlayerModel> playerList;
    private ClientConnection clientConnection;
    public AdminPlayersController() {
    }
    public void initialize() {

        usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        passwordColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        roleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        classicScoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClassicScore()).asObject());
        endlessScoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getEndlessScore()).asObject());
        hasPlayedClassicColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getHasPlayedClassic()));
        hasPlayedEndlessColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getHasPlayedEndless()));

        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        playerList = FXCollections.observableArrayList(
                getPlayerList()
        );

        usernameColumn.setOnEditCommit(event -> {
            PlayerModel player = event.getRowValue();
            player.setUsername(event.getNewValue());
        });

        passwordColumn.setOnEditCommit(event -> {
            PlayerModel player = event.getRowValue();
            player.setPassword(event.getNewValue());
        });


        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchPlayer(newValue));
        deleteButton.setOnAction(event -> deletePlayer());
        returnButton.setOnAction(event -> returnToMainMenu());
        saveListButton.setOnAction(this::handleSaveList);

        playerTable.setItems(playerList);
    }

    private void handleSaveList(ActionEvent event) {
        // Example: Show an alert when saving
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save List");
        alert.setHeaderText("List Saved");
        alert.setContentText("The list has been successfully saved.");
        alert.showAndWait();
        try {
            Response response = ClientConnection.bombGameServer.updatePlayerList(playerTable.getItems().stream().toList());
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    private List<PlayerModel> getPlayerList() {
        List<PlayerModel> allPlayers = new ArrayList<>();
        try {
            Response response = ClientConnection.bombGameServer.getPlayerList();

            if (response.isSuccess() && response.getData() instanceof List) {
                allPlayers = (List<PlayerModel>) response.getData();
                System.out.println(response.getData().toString());
            }
            return allPlayers;
        } catch (Exception e){
            return allPlayers;
        }
    }

    private void deletePlayer() {
        // Get selected item from the classicTable or endlessTable
        PlayerModel selectedItem = playerTable.getSelectionModel().getSelectedItem();

        try {
            Response response = App.bombGameServer.removePlayer(selectedItem);
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (selectedItem != null) {
            // Here, you can remove the selected item from the table
            if (selectedItem instanceof PlayerModel) {
                if (playerTable.getSelectionModel().getSelectedItem() != null) {
                    playerTable.getItems().remove(selectedItem);
                } else {
                    playerTable.getItems().remove(selectedItem);
                }

                // Optionally show a confirmation or success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Item Deleted");
                alert.setContentText("The selected entry has been deleted successfully.");
                alert.showAndWait();
            }
        } else {
            // If no item is selected, show an alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No item selected");
            alert.setContentText("Please select an entry to delete.");
            alert.showAndWait();
        }
    }

    public void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin/admin_categories.fxml"));
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

    public void searchPlayer(String query) {
        ObservableList<PlayerModel> updatedList = FXCollections.observableArrayList();

        for (PlayerModel player : playerList) {
            if (player.getUsername().toLowerCase().contains(query.toLowerCase())) {
                updatedList.add(player);
            }
        }
        playerTable.setItems(updatedList);
    }
}
