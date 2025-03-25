package Client.Admin.controller;

import Client.User.controller.RegisterController;
import Client.connection.ClientConnection;
import common.Response;
import common.model.QuestionModel;
import exception.ConnectionException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO still to be revised
public class QuestionControllerAdmin {

    @FXML
    private TableView<QuestionModel> questionsTable; 
    @FXML
    private TableColumn<QuestionModel, String> questionColumn; 
    @FXML
    private TableColumn<QuestionModel, String> choicesColumn; 
    @FXML
    private TableColumn<QuestionModel, String> answerColumn; 
    @FXML
    private TableColumn<QuestionModel, Integer> pointsColumn; 
    @FXML
    private Button deleteAllButton;
    @FXML
    private Button editButton; 
    @FXML
    private Button saveButton; 
    @FXML
    private TextField searchBox;
    protected ClientConnection clientConnection;
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());

    private ObservableList<QuestionModel> questionData = FXCollections.observableArrayList();

    public QuestionControllerAdmin () {
        try {
            this.clientConnection = ClientConnection.getInstance();
        } catch (ConnectionException e) {
            logger.log(Level.SEVERE, "\nGameController: Error initializing ClientConnection.", e);
        }
    }

    @FXML
    public void initialize() {
        setupTable();

        try {
            Response response = ClientConnection.bombGameServer.getQuestionsList();
            questionData = FXCollections.observableArrayList((QuestionModel) response.getData());
        } catch (Exception e) {

        }
        editButton.setOnAction(e -> toggleEditMode());
        saveButton.setOnAction(e -> saveChanges());
        deleteAllButton.setOnAction(e -> deleteAllQuestions());
        searchBox.setOnKeyReleased(e -> searchQuestions());
    }

    private void setupTable() {
        // Set up the table columns
        questionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestionText()));
        choicesColumn.setCellValueFactory(cellData -> new SimpleListProperty<>((ObservableList<String>) cellData.getValue().getChoices()).asString());
        answerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorrectAnswer()));
        pointsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

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

                questionData.add(new QuestionModel(category, text, choices, answer, score));
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
            for (QuestionModel question : questionData) {
                JSONObject questionObject = new JSONObject();
                questionObject.put("category", question.getCategory());
                questionObject.put("text", question.getQuestionText());
                questionObject.put("choices", question.getChoices());
                questionObject.put("answer", question.getCorrectAnswer());
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
        for (QuestionModel question : questionData) {
            if (question.getQuestionText().toLowerCase().contains(searchText)) {
                //question.setVisible(true);
            } else {
                //question.setVisible(false);
            }
        }
    }
}