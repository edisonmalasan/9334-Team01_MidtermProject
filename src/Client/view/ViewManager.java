package Client.view;
/**
 * Manages loading of windows for the game
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewManager {
    private static final Logger logger = Logger.getLogger(ViewManager.class.getName());

    // fxml paths
    public static final String MAIN_MENU = "/views/main_menu.fxml";
    public static final String LEADERBOARD = "/views/leaderboard.fxml";
    public static final String MODE_MENU = "/views/mode_menu.fxml";
    public static final String CATEGORY_MENU = "/views/category_menu.fxml";
    public static final String CLASSIC_GAME = "/views/classic_game.fxml";
    public static final String ENDLESS_GAME = "/views/endless_game.fxml";
    public static final String SCORE_VIEW = "/views/score_view.fxml";

    public static void goTo(ActionEvent event, String url, String title) {
        goTo(event, url, title, null);
    }

    public static void goTo(ActionEvent event, String url, String title, Consumer<FXMLLoader> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(url));
            Parent root = loader.load();

            if (controllerSetup != null) {
                controllerSetup.accept(loader);
            }

            Stage stage;
            if (event != null) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            } else {
                stage = new Stage();
            }

            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();

            logger.info("Switched to " + title);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load " + title, e);
        }
    }

}

