package Client.Admin.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CategoryControllerAdmin {

    @FXML
    private Button editBttn, saveBttn;

    @FXML
    private GridPane categoriesGrid;

    @FXML
    private TextField searchBox, addCategoryField;

    @FXML
    private Button addCategoryBttn;

    private boolean isEditing = false;

    @FXML
    public void initialize() {
        saveBttn.setVisible(false);
        addCategoryBttn.setVisible(false);
        addCategoryField.setVisible(false);
    }

    @FXML
    private void toggleEditMode() {
        isEditing = !isEditing;
        editBttn.setVisible(!isEditing);
        saveBttn.setVisible(isEditing);
        addCategoryBttn.setVisible(isEditing);
        addCategoryField.setVisible(isEditing);

        categoriesGrid.getChildren().forEach(node -> {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                pane.getChildren().forEach(child -> {
                    if (child instanceof Button) {
                        Button btn = (Button) child;
                        if (btn.getStyleClass().contains("view")) {
                            btn.setVisible(!isEditing);
                        }
                    }
                    if (child instanceof ImageView && child.getId().equals("delVis")) {
                        child.setVisible(isEditing);
                    }
                });
            }
        });
    }

    @FXML
    private void addCategory() {
        String categoryName = addCategoryField.getText().trim();
        if (!categoryName.isEmpty()) {
            // Logic to add category (needs model handling)
            System.out.println("Adding category: " + categoryName);
            addCategoryField.clear();
        }
    }
}
