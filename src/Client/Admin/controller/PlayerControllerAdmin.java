package Client.Admin.controller;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.StreamResult;

import Client.Admin.model.PlayerModelAdmin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.w3c.dom.*;
import java.io.*;
import java.util.Optional;

/**
 * Manipulates the admin GUI
 */
public class PlayerControllerAdmin {
    @FXML
    private TableView<PlayerModelAdmin> userTable;
    @FXML private TableColumn<PlayerModelAdmin, String> username;
    @FXML private TableColumn<PlayerModelAdmin, Integer> classicRank;
    @FXML private TableColumn<PlayerModelAdmin, Integer> classicScore;
    @FXML private TableColumn<PlayerModelAdmin, Integer> endlessRank;
    @FXML private Button editBttn;
    @FXML private Button saveBttn;

    private ObservableList<PlayerModelAdmin> playerData = FXCollections.observableArrayList();
    private File xmlFile = new File("players.xml");

    @FXML
    public void initialize() {
        setupTable();
        loadXML();

        // add refresh bttn**
        editBttn.setOnAction(e -> enableEditing());
        saveBttn.setOnAction(e -> saveChanges());
    }

    private void setupTable() {
        username.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        classicRank.setCellValueFactory(cellData -> cellData.getValue().classicRankProperty().asObject());
        classicScore.setCellValueFactory(cellData -> cellData.getValue().classicScoreProperty().asObject());
        endlessRank.setCellValueFactory(cellData -> cellData.getValue().endlessRankProperty().asObject());

        username.setCellFactory(TextFieldTableCell.forTableColumn());
        classicRank.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        classicScore.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        endlessRank.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        userTable.setItems(playerData);
    }

    private void loadXML() {
        playerData.clear();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("entry");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String name = element.getElementsByTagName("player").item(0).getTextContent();
                    int rank = Integer.parseInt(element.getElementsByTagName("classicRank").item(0).getTextContent());
                    int score = Integer.parseInt(element.getElementsByTagName("classicScore").item(0).getTextContent());
                    int endless = Integer.parseInt(element.getElementsByTagName("endlessRank").item(0).getTextContent());

                    playerData.add(new PlayerModelAdmin(name, rank, score, endless));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("entry");

            for (int i = 0; i < playerData.size(); i++) {
                PlayerModelAdmin player = playerData.get(i);
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    element.getElementsByTagName("player").item(0).setTextContent(player.getUsername());
                    element.getElementsByTagName("classicRank").item(0).setTextContent(String.valueOf(player.getClassicRank()));
                    element.getElementsByTagName("classicScore").item(0).setTextContent(String.valueOf(player.getClassicScore()));
                    element.getElementsByTagName("endlessRank").item(0).setTextContent(String.valueOf(player.getEndlessRank()));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFile);
            transformer.transform(source, result);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Player data saved successfully!");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableEditing() {
        userTable.setEditable(true);
    }

    // to revise i2 hehehe
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


