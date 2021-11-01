package App.GUI;

import App.Entity;
import App.FightState;
import App.Player;
import App.Wolf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;



public class FightController {
    @FXML Button fightButton;
    @FXML Button nextFightButton;
    @FXML Button fightBackButton;
    @FXML TextArea fightTextArea;

    @FXML Label playerHealthLabel;
    @FXML Label enemyHealthLabel;

    Player player = new Player();
    ArrayList<Entity> allEnemies = new ArrayList<>();
    FightState currentFightState = new FightState(player,allEnemies);

    public FightController() throws ParserConfigurationException, IOException, SAXException {
    }

    public void roundOver(){
        fightBackButton.setDisable(false);
        fightBackButton.setVisible(true);

        nextFightButton.setVisible(true);
        nextFightButton.setDisable(false);

        fightButton.setDisable(true);
        fightButton.setVisible(false);
    }

    public void nextFightPressed(ActionEvent actionEvent) {
        currentFightState.newRound();
        nextFightButton.setDisable(true);
        nextFightButton.setVisible(false);

        fightButton.setDisable(false);
        fightButton.setVisible(true);

        enemyHealthLabel.setText(currentFightState.getCurrentEnemy().getName()+" HP " +currentFightState.getCurrentEnemy().getCurrentHealth()+
                "/" + currentFightState.getCurrentEnemy().getMaxHealth());
    }

    public void fightButtonPressed(ActionEvent actionEvent) {
        currentFightState.round();

        fightTextArea.appendText("Round "+ currentFightState.getRoundNumber() + ".\n");

        fightBackButton.setDisable(true);
        fightBackButton.setVisible(false);

        fightTextArea.appendText("you rolled a "+currentFightState.getPlayerRoll()+".\n");

        fightTextArea.appendText("you dealt "+currentFightState.getPlayerDMG()+" damage.");

        if(currentFightState.isPlayerCrit()){
            fightTextArea.appendText(" *CRITICAL HIT*\n");
        }
        else {
            fightTextArea.appendText("\n");
        }
        enemyHealthLabel.setText(currentFightState.getCurrentEnemy().getName()+" HP " +currentFightState.getCurrentEnemy().getCurrentHealth()+
                "/" + currentFightState.getCurrentEnemy().getMaxHealth());
        if(currentFightState.getCurrentEnemy().die()){
            fightTextArea.appendText("You have defeated: "+currentFightState.getCurrentEnemy().getName()+".\n");
            currentFightState.newRound();
            roundOver();
            return;
        }

        fightTextArea.appendText(currentFightState.getCurrentEnemy().getName()+" rolled a "
                +currentFightState.getEnemyRoll()+".\n");

        fightTextArea.appendText(currentFightState.getCurrentEnemy().getName()+" dealt "
                +currentFightState.getEnemyDMG()+" damage.");

        if(currentFightState.isEnemyCrit()){
            fightTextArea.appendText(" *CRITICAL HIT*\n");
        }
        else {
            fightTextArea.appendText("\n");
        }

        playerHealthLabel.setText("Your HP: " + currentFightState.getPlayer().getCurrentHealth()+
                "/"+ currentFightState.getPlayer().getMaxHealth());

        fightTextArea.appendText("End of round "+ currentFightState.getRoundNumber() + ".\n");
    }

    public void fightBackPressed(ActionEvent actionEvent) throws IOException {
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/play.fxml"));
        Stage playStage = (Stage) fightBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }


}
