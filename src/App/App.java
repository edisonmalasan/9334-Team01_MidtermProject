package App;

import common.AnsiFormatter;
import Client.connection.ClientConnection;
import Client.utils.SoundUtility;
import exception.ConnectionException;
import exception.FXMLLoadingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/client/input_username.fxml"));
            Parent root = fxmlLoader.load();
            //test
            SoundUtility.playBackgroundMusic();

            try {
                ClientConnection.getInstance().connect();
                logger.info("✅ Client successfully connected to the server.");
            } catch (ConnectionException e) {
                logger.warning("⚠ Server is not running. The client will continue in offline mode.");
            }

            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/bomb_mad.png")));

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Bomb Defusing Game");
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Closing application...");
                Platform.exit();
                System.exit(0);
            });

        } catch (IOException e) {
            logger.log(Level.SEVERE, "❌ Failed to load FXML: input_username.fxml", e);
            throw new FXMLLoadingException("input_username.fxml", e);
        }
    }
}