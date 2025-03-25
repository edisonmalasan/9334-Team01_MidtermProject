package Client.Admin.controller;

import Client.Admin.model.CategoryModelAdmin; // Ensure you have the correct import for the Category model
import Client.Admin.model.CategoryModelAdmin; // Import your CategoryModelAdmin
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class CategoryControllerAdmin {
    private static final Logger logger = Logger.getLogger(CategoryControllerAdmin.class.getName());

    // Main components
    @FXML private HBox adminCategories;
    @FXML private Button exitBttn;

    // Title section
    @FXML private Button editBttn;
    @FXML private Button saveBttn;
    @FXML private Label titleSubheadText1;

    // Search and categories
    @FXML private TextField searchBox;
    @FXML private GridPane categoriesGrid;

    // Bottom buttons
    @FXML private Button delBttn;
    @FXML private Button addBttn;

    private boolean isEditing = false;
    private CategoryModelAdmin categoryModel; // Model to manage categories

    @FXML
    public void initialize() {
        logger.info("Initializing CategoryControllerAdmin");
        categoryModel = new CategoryModelAdmin(); // Initialize the category model

        // Debug injected components
        logger.info("Components initialized:");
        logger.info("- adminCategories: " + (adminCategories != null ? "OK" : "NULL"));
        logger.info("- exitBttn: " + (exitBttn != null ? "OK" : "NULL"));
        logger.info("- editBttn: " + (editBttn != null ? "OK" : "NULL"));
        logger.info("- saveBttn: " + (saveBttn != null ? "OK" : "NULL"));
        logger.info("- searchBox: " + (searchBox != null ? "OK" : "NULL"));
        logger.info("- categoriesGrid: " + (categoriesGrid != null ? "OK" : "NULL"));
        logger.info("- delBttn: " + (delBttn != null ? "OK" : "NULL"));
        logger.info("- addBttn: " + (addBttn != null ? "OK" : "NULL"));

        // Initial state setup
        saveBttn.setVisible(false);

        // Set up event handlers
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        if (editBttn != null) {
            editBttn.setOnAction(e -> toggleEditMode());
        }

        if (saveBttn != null) {
            saveBttn.setOnAction(e -> saveChanges());
        }

        if (delBttn != null) {
            delBttn.setOnAction(e -> deleteAllCategories());
        }

        if (addBttn != null) {
            addBttn.setOnAction(e -> addCategory(searchBox.getText())); // Get category name from searchBox
        }

        if (exitBttn != null) {
            exitBttn.setOnAction(e -> exitToDashboard());
        }
    }

    private void toggleEditMode() {
        isEditing = !isEditing;
        logger.info("Edit mode: " + isEditing);

        if (editBttn != null) editBttn.setVisible(!isEditing);
        if (saveBttn != null) saveBttn.setVisible(isEditing);

        // Toggle visibility of delete icons in all category panes
        if (categoriesGrid != null) {
            categoriesGrid.getChildren().forEach(node -> {
                if (node instanceof Pane) {
                    Pane pane = (Pane) node;
                    pane.getChildren().forEach(child -> {
                        if (child instanceof ImageView && "delVis".equals(child.getId())) {
                            child.setVisible(isEditing);
                        } else if (child instanceof Button && "view".equals(((Button) child).getStyleClass())) {
                            ((Button) child).setVisible(!isEditing);
                        }
                    });
                }
            });
        }
    }

    private void saveChanges() {
        logger.info("Saving category changes");
        try {
            // Example: Save categories to a database or file
            // Implement your save logic here
            logger.info("Categories saved successfully");
        } catch (Exception e) {
            logger.severe("Failed to save categories: " + e.getMessage());
        }
        toggleEditMode(); // Return to view mode
    }

    private void deleteAllCategories() {
        logger.warning("Attempting to delete all categories");
        try {
            categoryModel.clearCategories(); // Clear the list of categories
            // Optionally, delete from a database or file
            logger.info("All categories deleted successfully");
        } catch (Exception e) {
            logger.severe("Failed to delete categories: " + e.getMessage());
        }
    }

    private void addCategory(String categoryName) {
        logger.info("Adding new category: " + categoryName);
        try {
            categoryModel.addCategory(categoryName); // Add category to the model
            logger.info("Category added successfully: " + categoryName);
        } catch (Exception e) {
            logger.severe("Failed to add category: " + e.getMessage());
        }
    }

    private void exitToDashboard() {
        logger.info("Exiting to dashboard");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) adminCategories.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
        } catch (IOException e) {
            logger.severe("Failed to load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}