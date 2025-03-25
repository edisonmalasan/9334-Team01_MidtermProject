package Client.Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class QuestionControllerAdmin {

    // FXML Injections
    @FXML private AnchorPane rootPane;
    @FXML private Label categoryTitleLabel;
    @FXML private Button returnButton;
    @FXML private Button deleteButton;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TableView<?> questionTable;
    @FXML private ComboBox<String> categoryField;
    @FXML private TextField questionField;
    @FXML private TextField choice1Field;
    @FXML private TextField choice2Field;
    @FXML private TextField choice3Field;
    @FXML private TextField choice4Field;
    @FXML private TextField correctAnswerField;
    @FXML private TextField scoreField;
    @FXML private Button addQuestionButton;

    private String currentCategory;

    @FXML
    public void initialize() {
        // Initialize category dropdowns
        ObservableList<String> categories = FXCollections.observableArrayList(
                "ALGEBRA", "GEOMETRY", "PROBABILITY", "TRIGONOMETRY", "ANGLES", "LOGIC"
        );
        categoryComboBox.setItems(categories);
        categoryField.setItems(categories);

        // Set up event handlers
        returnButton.setOnAction(this::handleReturn);
        addQuestionButton.setOnAction(this::handleAddQuestion);
        deleteButton.setOnAction(this::handleDeleteQuestion);

        // Update the title if category was set before initialization
        updateCategoryTitle();
    }

    public void setCategory(String category) {
        this.currentCategory = category;

        // Set the category in the filter dropdown
        if (category != null && categoryComboBox != null) {
            categoryComboBox.setValue(category);
        }

        updateCategoryTitle();

        // TODO: Load questions for this category
        loadQuestionsForCategory(category);
    }

    private void updateCategoryTitle() {
        if (categoryTitleLabel != null) {
            String title = currentCategory != null ?
                    "Questions for: " + currentCategory :
                    "All Questions";
            categoryTitleLabel.setText(title);
        }
    }

    private void loadQuestionsForCategory(String category) {
        // TODO: Implement actual data loading
        System.out.println("Loading questions for category: " + category);
    }

    private void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin/admin_categories.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not return to categories", Alert.AlertType.ERROR);
        }
    }

    private void handleAddQuestion(ActionEvent event) {
        // Validate inputs
        if (categoryField.getValue() == null || questionField.getText().isEmpty()) {
            showAlert("Input Error", "Category and Question are required", Alert.AlertType.WARNING);
            return;
        }

        // TODO: Save the new question
        System.out.println("Adding new question:");
        System.out.println("Category: " + categoryField.getValue());
        System.out.println("Question: " + questionField.getText());
        System.out.println("Choices: " + choice1Field.getText() + ", " + choice2Field.getText() +
                ", " + choice3Field.getText() + ", " + choice4Field.getText());
        System.out.println("Correct Answer: " + correctAnswerField.getText());
        System.out.println("Score: " + scoreField.getText());

        // Clear the form
        clearQuestionForm();
    }

    private void handleDeleteQuestion(ActionEvent event) {
        // TODO: Implement question deletion
        Object selectedItem = questionTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Selection Error", "Please select a question to delete", Alert.AlertType.WARNING);
            return;
        }

        System.out.println("Deleting selected question");
    }

    private void clearQuestionForm() {
        questionField.clear();
        choice1Field.clear();
        choice2Field.clear();
        choice3Field.clear();
        choice4Field.clear();
        correctAnswerField.clear();
        scoreField.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}