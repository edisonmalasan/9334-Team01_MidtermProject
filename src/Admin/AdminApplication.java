package Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //do not remove font
        Font.loadFont(getClass().getResource("resources/fonts/SpaceNova-6Rpd1.otf").toExternalForm(), 14);
        Font.loadFont(getClass().getResource("resources/fonts/RobotoMono-Bold.ttf").toExternalForm(), 12);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Admin/view/AdminDashboard.fxml")); // temporary i2
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Admin Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
