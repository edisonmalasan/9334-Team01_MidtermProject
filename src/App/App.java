package App;

import common.Log.AnsiFormatter;
import Client.Player.utils.SoundUtility;
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
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {
    private static final Logger logger = Logger.getLogger(App.class.getName());
    public static BombGameServer bombGameServer;

    public static String fetchIPAddress;

    static {
        AnsiFormatter.enableColorLogging(logger);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException, IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/client/login.fxml"));
            Parent root = fxmlLoader.load();
            //test
            SoundUtility.playBackgroundMusic();

                while (true) {
                    try {
                        Registry registry = LocateRegistry.getRegistry(fetchIPAddress, 1099);
                        bombGameServer = (BombGameServer) registry.lookup("server");
                        break;
                    } catch (Exception e) {
                        logger.warning("⚠ Server is not running. The client will continue in offline mode.");
                        logger.warning("⚠ Connection failed. Retrying in 5 seconds...");

                        try {
                            Thread.sleep(5000);  // Retry after 5 seconds
                        } catch (InterruptedException interruptedException) {
                            interruptedException.printStackTrace();
                        }
                    }
                }
            logger.info("\nClientConnection: Connected to server successfully!");

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

        } catch (Exception e) {
            logger.log(Level.SEVERE, "❌ Failed to load FXML: login.fxml", e);
            throw new FXMLLoadingException("login.fxml", e);
        }
    }

    static {
        try {
            fetchIPAddress = InetAddress.getLocalHost().getHostAddress();
            logger.info("Detected IP Address: " + fetchIPAddress);
        } catch (IOException e) {
            logger.warning("Failed to detect local IP.");
            fetchIPAddress = "127.0.0.1"; // make ip address default
        }
    }
}