package App;

import Client.common.connection.ClientConnection;
import Client.Player.utils.SoundUtility;
import common.Log.LogManager;
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
import common.Protocol;

public class App extends Application {
    public static BombGameServer bombGameServer;
    public static String fetchIPAddress; // for logs
    private final LogManager logManager = LogManager.getInstance();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FXMLLoadingException, IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/client/login.fxml"));
            Parent root = fxmlLoader.load();
            SoundUtility.playBackgroundMusic();

            while (true) {
                try {
                    Registry registry = LocateRegistry.getRegistry(Protocol.IP_ADDRESS, Protocol.PORT_NUMBER);
                    bombGameServer = (BombGameServer) registry.lookup("server");
                    break;
                } catch (Exception e) {
                    logManager.appendLog("⚠ Server is not running. The client will continue in offline mode.");
                    logManager.appendLog("⚠ Connection failed. Retrying in 5 seconds...");

                    try {
                        Thread.sleep(5000);  // Retry after 5 seconds
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
            ClientConnection.bombGameServer.logMessage("\nClientConnection: Connected to server successfully!");

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
            ClientConnection.bombGameServer.logMessage("❌ Failed to load FXML: login.fxml " + e.getMessage());
            throw new FXMLLoadingException("login.fxml", e);
        }
    }

    static {
        try {
            fetchIPAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (IOException e) {
            fetchIPAddress = "127.0.0.1"; // Default IP address
        }
    }
}
