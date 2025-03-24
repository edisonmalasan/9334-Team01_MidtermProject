package Client.Admin.controller;

import App.App;
import common.Response;
import common.model.QuestionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

//TODO to be revise
// fix the connection  with fxml
public class QuestionControllerAdmin {

    @FXML
    private GridPane questionsGrid; 
    @FXML
    private TextField searchBox; 
    @FXML
    private Button deleteBttn; 
    @FXML
    private Button editBttn; 

    private ObservableList<QuestionModel> questionData;

    @FXML
    public void initialize() {
        setupQuestions();
    }

    private void setupQuestions() {
        // Load questions from JSON file
        try {
            questionData = FXCollections.observableArrayList(
                    getQuestionList()
            );

            // Populate the grid with questions
            for (int i = 0; i < questionData.size(); i++) {
                QuestionModel question = questionData.get(i);
                Pane questionPane = new Pane();
                questionPane.setPrefHeight(200);
                questionPane.setPrefWidth(200);
                questionPane.getStyleClass().add("question");

                Label categoryLabel = new Label("Category: " + question.getCategory());
                categoryLabel.setLayoutX(10);
                categoryLabel.setLayoutY(10);
                questionPane.getChildren().add(categoryLabel);

                Label textLabel = new Label("Question: " + question.getQuestionText());
                textLabel.setLayoutX(10);
                textLabel.setLayoutY(30);
                questionPane.getChildren().add(textLabel);

                // Add more UI elements for choices, answer, etc. as needed

                questionsGrid.add(questionPane, i % 2, i / 2); // Add to grid
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load questions from JSON file: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}