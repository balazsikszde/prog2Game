package App.GUI;


import App.FightState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class LayoutController {

    @FXML AnchorPane mainAnchorPane;
    @FXML Button playButton;
    @FXML Button quitButton;

    @FXML
    public void initialize() {}

    public void playPressed(ActionEvent actionEvent) throws IOException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/play.fxml"));
        Stage playStage = (Stage) playButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }

    public void quitPressed(ActionEvent actionEvent) {
        Stage stage =(Stage)quitButton.getScene().getWindow();
        stage.close();
    }
}
