package App;

import Client.User.controller.InputIPAddressController;
import common.AnsiFormatter;
import Client.User.utils.SoundUtility;
import exception.FXMLLoadingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utility.BombGameServer;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static BombGameServer bombGameServer;

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/input_IP.fxml"));
            Parent root = fxmlLoader.load();
            //test
            SoundUtility.playBackgroundMusic();

            while (true) {
                try {
                    Registry registry = LocateRegistry.getRegistry(InputIPAddressController.ipAddress, 1099);
                    bombGameServer = (BombGameServer) registry.lookup("server");
                    logger.info("✅ Client successfully connected to the server.");
                    break;
                } catch (Exception e) {
                    logger.warning("⚠ Server is not running. The client will continue in offline mode.");
                }
                logger.info("⚠ Server is not running. Reconnecting...");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    logger.info("Reconnection error: " + e.getMessage());
                }
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
            logger.log(Level.SEVERE, "❌ Failed to load FXML: login.fxml", e);
            throw new FXMLLoadingException("login.fxml", e);
        }
    }
}