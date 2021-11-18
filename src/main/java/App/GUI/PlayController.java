package App.GUI;

import App.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayController implements Initializable {
    @FXML AnchorPane playAnchorPane;
    @FXML Button fightButton;
    @FXML Button playBackButton;
    @FXML Label goldLabel;

    Player player = new Player();

    public PlayController() throws ParserConfigurationException, IOException, SAXException {
    }

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        goldLabel.setText("Gold Pouch: "+player.getGold());
    }
}
