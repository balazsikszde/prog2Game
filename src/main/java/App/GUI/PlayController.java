package App.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayController {
    @FXML AnchorPane playAnchorPane;
    @FXML Button fightButton;
    @FXML Button playBackButton;

    public void fightPressed(ActionEvent actionEvent) throws IOException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/fight.fxml"));
        Stage playStage = (Stage) playBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }

    public void playBackPressed(ActionEvent actionEvent) throws IOException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/layout.fxml"));
        Stage playStage = (Stage) playBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }
}
