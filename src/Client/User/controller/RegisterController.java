package Client.User.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {
    private static final Logger logger = Logger.getLogger(RegisterController.class.getName());

    @FXML
    private Hyperlink loginLink;

    @FXML
    public void initialize() {

        loginLink.setOnAction(this::switchToLogin);
    }

    private void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/client/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Bomb Defusing Game");
            stage.setResizable(false);
            stage.show();

            logger.info("RegisterController: Switched to Login Page.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load Login Page.", e);
        }
    }
}
