package App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

//inventory rendszer, equipment classok, item class,
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/layout.fxml"));
        Scene scene = new Scene(root, 720, 480);
        scene.getStylesheets().add("CSS/scheme.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game");
        primaryStage.show();
    }
}

