package Client.Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Corrected font loading
        Font.loadFont(getClass().getResource("/fonts/SpaceNova-6Rpd1.otf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("/fonts/RobotoMono-Bold.ttf").toExternalForm(), 12);

        // Corrected FXML loading
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/admin/admin_db.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/admin/admin_db.fxml"));


        if (fxmlLoader.getLocation() == null) {
            throw new RuntimeException("FXML file not found: /views/admin/admin_db.fxml");
        }

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Client.Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}