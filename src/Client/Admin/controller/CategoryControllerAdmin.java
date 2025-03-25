package Client.Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CategoryControllerAdmin {

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadScene(event, "/views/admin/admin_db.fxml");
    }

    @FXML
    private void handlePlayers(ActionEvent event) {
        loadScene(event, "/views/admin/admin_leaderboard.fxml");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        loadScene(event, "/views/client/login.fxml");
    }

    @FXML
    private void handleViewQuestions(ActionEvent event) {
        loadScene(event, "/views/admin/questions.fxml");
    }
}
