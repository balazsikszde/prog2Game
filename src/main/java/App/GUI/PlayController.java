package App.GUI;

import App.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayController implements Initializable {
    @FXML AnchorPane playAnchorPane;
    @FXML Button fightButton;
    @FXML Button playBackButton;
    @FXML Label goldLabel;
    @FXML TextArea upgradeTextArea;
    @FXML TextField inputTextField;
    @FXML Label helmetLabel;
    @FXML Label chestplateLabel;
    @FXML Label leggingsLabel;
    @FXML Label bootsLabel;

    int upgradeSelected;

    Player player = new Player();

    public PlayController() throws ParserConfigurationException, IOException, SAXException {
    }

    public void fightPressed(ActionEvent actionEvent) throws IOException, XPathExpressionException,
            ParserConfigurationException, TransformerException, SAXException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/fight.fxml"));
        Stage playStage = (Stage) playBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }

    public void playBackPressed(ActionEvent actionEvent) throws IOException, XPathExpressionException,
            ParserConfigurationException, TransformerException, SAXException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/layout.fxml"));
        Stage playStage = (Stage) playBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        goldLabel.setText("Gold Pouch: "+player.getGold());

        helmetLabel.setText("Helmet: "+player.getHelmetLevel());
        chestplateLabel.setText("Chestplate: "+player.getChestplateLevel());
        leggingsLabel.setText("Leggings: "+player.getLeggingsLevel());
        bootsLabel.setText("Boots: "+player.getBootsLevel());
        
        upgradeTextArea.setText("This is the upgrade menu, input your choice in the box below:\n" +
                "1.Helmet\n2.Chestplate\n3.Leggings\n4.Boots\n");
    }

    public void enterButtonPressed(ActionEvent actionEvent) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException, SAXException {
        String what = inputTextField.getText();
        switch (what){
            case "1":
                upgradeSelected=1;
                upgradeTextArea.appendText("Helmet upgrade selected, cost: "+player.upgradeCost(1)
                        +". Proceed? Y/N\n");
                inputTextField.clear();
                break;
            case "2":
                upgradeSelected=2;
                upgradeTextArea.appendText("Chestplate upgrade selected, cost: "+player.upgradeCost(2)
                        +". Proceed? Y/N\n");
                inputTextField.clear();
                break;
            case "3":
                upgradeSelected=3;
                upgradeTextArea.appendText("Leggings upgrade selected, cost: "+player.upgradeCost(3)
                        +". Proceed? Y/N\n");
                inputTextField.clear();
                break;
            case "4":
                upgradeSelected=4;
                upgradeTextArea.appendText("Boots upgrade selected, cost: "+player.upgradeCost(4)
                        +". Proceed? Y/N\n");
                inputTextField.clear();
                break;
            case "Y":
            case "y":
                if (upgradeSelected==0){
                    upgradeTextArea.appendText("You didn't select anything.\n");
                }
                else if(player.upgrade(upgradeSelected)){
                    upgradeTextArea.appendText("Upgrade Successful.\n");
                }
                else upgradeTextArea.appendText("Not enough gold.\n");
                upgradeSelected=0;
                inputTextField.clear();
                break;
            case "N":
            case "n":
                if (upgradeSelected==0){
                    upgradeTextArea.appendText("You didn't select anything.\n");
                }
                else upgradeTextArea.appendText("aborted\n");
                upgradeSelected=0;
                inputTextField.clear();
                break;
            default:
                upgradeTextArea.appendText("invalid input.\n");
                inputTextField.clear();

        }
        player.savePlayer();
        helmetLabel.setText("Helmet: "+player.getHelmetLevel());
        chestplateLabel.setText("Chestplate: "+player.getChestplateLevel());
        leggingsLabel.setText("Leggings: "+player.getLeggingsLevel());
        bootsLabel.setText("Boots: "+player.getBootsLevel());
        goldLabel.setText("Gold Pouch: "+player.getGold());
    }
}
