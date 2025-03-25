package Client.Admin.controller;

import App.App;
import Client.connection.ClientConnection;
import common.Response;
import common.model.QuestionModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdminQuestionsController {

    public Button addQuestionButton;
    public Button returnButton;
    public ComboBox categoryComboBox;
    public ComboBox categoryField;
    public TextField questionField;
    public TextField choice1Field;
    public TextField choice2Field;
    public TextField choice3Field;
    public TextField choice4Field;
    public TextField correctAnswerField;
    public TextField scoreField;
    public Button deleteButton;
    public TextField searchField;
    @FXML
    private TableView<QuestionModel> questionTable;

    @FXML
    private TableColumn<QuestionModel, String> categoryColumn;

    @FXML
    private TableColumn<QuestionModel, String> questionColumn;

    @FXML
    private TableColumn<QuestionModel, String> choice1Column;

    @FXML
    private TableColumn<QuestionModel, String> choice2Column;

    @FXML
    private TableColumn<QuestionModel, String> choice3Column;

    @FXML
    private TableColumn<QuestionModel, String> choice4Column;

    @FXML
    private TableColumn<QuestionModel, String> correctAnswerColumn;

    @FXML
    private TableColumn<QuestionModel, Integer> scoreColumn;

    private ObservableList<QuestionModel> questionList;
    private ClientConnection clientConnection;

    public AdminQuestionsController() {
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionText()));
        choice1Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChoices().get(0)));
        choice2Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChoices().get(1)));
        choice3Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChoices().get(2)));
        choice4Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChoices().get(3)));
        correctAnswerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorrectAnswer()));
        scoreColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Select Category","ALGEBRA", "GEOMETRY", "PROBABILITY", "TRIGONOMETRY", "ANGLES", "LOGIC"
        ));
        categoryComboBox.setOnAction(event -> filterByCategory());

        questionList = FXCollections.observableArrayList(
                getQuestionList()
        );

        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchQuestion(newValue));
        addQuestionButton.setOnAction(event -> addNewQuestion());
        deleteButton.setOnAction(event -> deleteEntry());
        returnButton.setOnAction(event -> returnToMainMenu());

        questionTable.setItems(questionList);
    }

    private List<QuestionModel> getQuestionList() {
        List<QuestionModel> allQuestions = new ArrayList<>();
        try {
            Response response = App.bombGameServer.getQuestionsList();

            if (response.isSuccess() && response.getData() instanceof List) {
                allQuestions = (List<QuestionModel>) response.getData();
                System.out.println(response.getData().toString());
            }
            return allQuestions;
        } catch (Exception e){
            return allQuestions;
        }
    }
    public void filterByCategory() {
        String selectedCategory = (String) categoryComboBox.getValue();

        if (Objects.equals(selectedCategory, "Select Category") || selectedCategory.isEmpty()) {
            questionTable.setItems(questionList);
        } else {
            ObservableList<QuestionModel> filteredList = FXCollections.observableArrayList();

            for (QuestionModel question : questionList) {
                if (question.getCategory().equalsIgnoreCase(selectedCategory)) {
                    filteredList.add(question);
                }
            }

            questionTable.setItems(filteredList);
        }
    }
    private void addNewQuestion() {
        try {
            String category = (String) categoryField.getValue();
            String questionText = questionField.getText();
            String choice1 = choice1Field.getText();
            String choice2 = choice2Field.getText();
            String choice3 = choice3Field.getText();
            String choice4 = choice4Field.getText();
            String correctAnswer = correctAnswerField.getText();
            String score = scoreField.getText();

            if (category.isEmpty() || questionText.isEmpty() || choice1.isEmpty() ||
                    choice2.isEmpty() || choice3.isEmpty() || choice4.isEmpty() ||
                    correctAnswer.isEmpty() || score.isEmpty()) {
                showErrorMessage("All fields must be filled!");
            } else {
                int newScore = Integer.parseInt(score);
                QuestionModel newQuestion = new QuestionModel(category, questionText,
                        Arrays.asList(choice1, choice2, choice3, choice4), correctAnswer, newScore);

                try {
                    Response response = App.bombGameServer.updateQuestion(newQuestion);
                    if (response.isSuccess()) {
                        System.out.println(response.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                questionList.add(newQuestion);
                clearInputFields();
            }
        } catch (Exception e) {
            showErrorMessage("All fields must be filled!");
        }
    }

    // Method to show an error message
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear all input fields
    private void clearInputFields() {
        categoryField.getSelectionModel().clearSelection();
        questionField.clear();
        choice1Field.clear();
        choice2Field.clear();
        choice3Field.clear();
        choice4Field.clear();
        correctAnswerField.clear();
        scoreField.clear();
    }

    private void deleteEntry() {
        // Get selected item from the classicTable or endlessTable
        QuestionModel selectedItem = questionTable.getSelectionModel().getSelectedItem();

        try {
            Response response = App.bombGameServer.updateQuestion(selectedItem);
            if (response.isSuccess()) {
                System.out.println(response.getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (selectedItem != null) {
            // Here, you can remove the selected item from the table
            if (selectedItem instanceof QuestionModel) {
                if (questionTable.getSelectionModel().getSelectedItem() != null) {
                    questionTable.getItems().remove(selectedItem);
                } else {
                    questionTable.getItems().remove(selectedItem);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Redundant/admin_menu.fxml"));
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

    public void searchQuestion(String query) {
        ObservableList<QuestionModel> updatedList = FXCollections.observableArrayList();

        for (QuestionModel question : questionList) {
            if (question.getQuestionText().toLowerCase().contains(query.toLowerCase())) {
                updatedList.add(question);
            }
        }
        questionTable.setItems(updatedList);
    }
}
