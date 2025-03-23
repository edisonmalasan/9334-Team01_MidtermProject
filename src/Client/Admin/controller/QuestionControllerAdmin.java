package Client.Admin.controller;

import Client.Admin.model.QuestionModelAdmin;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    private ObservableList<QuestionModelAdmin> questionData = FXCollections.observableArrayList(); 

    @FXML
    public void initialize() {
        setupQuestions();
    }

    private void setupQuestions() {
        // Load questions from JSON file
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/Client/Admin/data/questions.json")));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            // Parse the content as a JSON array
            JSONArray jsonArray = new JSONArray(content.toString());

            // Loop through each question in the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject questionObject = jsonArray.getJSONObject(i);
                String category = questionObject.getString("category");
                String text = questionObject.getString("text");
                JSONArray choicesArray = questionObject.getJSONArray("choices");
                List<String> choices = choicesArray.toList().stream().map(Object::toString).toList();
                String answer = questionObject.getString("answer");
                int score = questionObject.getInt("score");

                questionData.add(new QuestionModelAdmin(category, text, choices, answer, score));
            }

            // Populate the grid with questions
            for (int i = 0; i < questionData.size(); i++) {
                QuestionModelAdmin question = questionData.get(i);
                Pane questionPane = new Pane();
                questionPane.setPrefHeight(200);
                questionPane.setPrefWidth(200);
                questionPane.getStyleClass().add("question");

                Label categoryLabel = new Label("Category: " + question.getCategory());
                categoryLabel.setLayoutX(10);
                categoryLabel.setLayoutY(10);
                questionPane.getChildren().add(categoryLabel);

                Label textLabel = new Label("Question: " + question.getText());
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
}