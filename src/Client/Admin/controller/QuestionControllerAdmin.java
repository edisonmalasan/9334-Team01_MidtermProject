package Client.Admin.controller;

import Client.Admin.model.QuestionModelAdmin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

// TODO still to be revised
public class QuestionControllerAdmin {

    @FXML
    private TableView<QuestionModelAdmin> questionsTable; 
    @FXML
    private TableColumn<QuestionModelAdmin, String> questionColumn; 
    @FXML
    private TableColumn<QuestionModelAdmin, String> choicesColumn; 
    @FXML
    private TableColumn<QuestionModelAdmin, String> answerColumn; 
    @FXML
    private TableColumn<QuestionModelAdmin, Integer> pointsColumn; 
    @FXML
    private Button deleteAllButton;
    @FXML
    private Button editButton; 
    @FXML
    private Button saveButton; 
    @FXML
    private TextField searchBox; 

    private ObservableList<QuestionModelAdmin> questionData = FXCollections.observableArrayList(); 

    @FXML
    public void initialize() {
        setupTable();
        loadQuestions();
        editButton.setOnAction(e -> toggleEditMode());
        saveButton.setOnAction(e -> saveChanges());
        deleteAllButton.setOnAction(e -> deleteAllQuestions());
        searchBox.setOnKeyReleased(e -> searchQuestions());
    }

    private void setupTable() {
        // Set up the table columns
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().textProperty());
        choicesColumn.setCellValueFactory(cellData -> cellData.getValue().choicesProperty().asString());
        answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        pointsColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        questionsTable.setItems(questionData);
    }

    private void loadQuestions() {
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
        } catch (Exception e) {
            System.out.println("Error loading questions: " + e.getMessage());
        }
    }

    private void toggleEditMode() {
       
        if (editButton.getText().equals("Edit")) {
            editButton.setText("Cancel");
            saveButton.setVisible(true);
            questionsTable.setEditable(true);
        } else {
            editButton.setText("Edit");
            saveButton.setVisible(false);
            questionsTable.setEditable(false);
        }
    }

    private void saveChanges() {
        // Save changes to questions
        try {
            // Create a JSON array to hold all question objects
            JSONArray jsonArray = new JSONArray();

            // Iterate over question data and create a JSON object for each question
            for (QuestionModelAdmin question : questionData) {
                JSONObject questionObject = new JSONObject();
                questionObject.put("category", question.getCategory());
                questionObject.put("text", question.getText());
                questionObject.put("choices", question.getChoices());
                questionObject.put("answer", question.getAnswer());
                questionObject.put("score", question.getScore());

                jsonArray.put(questionObject);
            }

            // Write the JSON array to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter("questions.json"));
            writer.write(jsonArray.toString(4));  // Format the output with 4 spaces for indentation
            writer.close();

            System.out.println("Changes saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving changes: " + e.getMessage());
        }
    }

    private void deleteAllQuestions() {
       
        questionData.clear();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("questions.json"));
            writer.write("[]");  // Write an empty JSON array to the file
            writer.close();

            System.out.println("All questions deleted successfully!");
        } catch (IOException e) {
            System.out.println("Error deleting questions: " + e.getMessage());
        }
    }

    private void searchQuestions() {
     
        String searchText = searchBox.getText().toLowerCase();
        for (QuestionModelAdmin question : questionData) {
            if (question.getText().toLowerCase().contains(searchText)) {
                question.setVisible(true);
            } else {
                question.setVisible(false);
            }
        }
    }
}