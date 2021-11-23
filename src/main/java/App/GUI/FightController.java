package App.GUI;


import App.Entity;
import App.FightState;
import App.Player;
import App.Wolf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;


public class FightController implements Initializable {
    @FXML Button fightButton;
    @FXML Button nextFightButton;
    @FXML Button fightBackButton;
    @FXML TextArea fightTextArea;

    @FXML Label killsToAdvanceLabel;

    @FXML Label enemyLevelLabel;
    @FXML Label playerLevelLabel;

    @FXML Label playerHealthLabel;
    @FXML Label enemyHealthLabel;

    @FXML ProgressBar xpBar;
    @FXML Label xpLabel;

    public final FightState currentFightState = new FightState();

    public FightController() throws IOException, ParserConfigurationException, SAXException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        killsToAdvanceLabel.setText("Kills To Advance: "+currentFightState.getCurrentKills()+" / "+
                currentFightState.getKillsToAdvance());

        playerHealthLabel.setText("Your HP: " + currentFightState.getPlayer().getCurrentHealth()+" / "
                +currentFightState.getPlayer().getMaxHealth());
        enemyHealthLabel.setText(currentFightState.getCurrentEnemy().getName()+" HP: "
                +currentFightState.getCurrentEnemy().getCurrentHealth()+" / "+currentFightState.getCurrentEnemy().getMaxHealth());

        enemyLevelLabel.setText(currentFightState.getCurrentEnemy().getName()+"'s level: "+currentFightState.getCurrentEnemy().getLevel());
        playerLevelLabel.setText("Your level: "+currentFightState.getPlayer().getLevel());

        xpLabel.setText("Your XP: "+currentFightState.getPlayer().getCurrentXP()+" / "+currentFightState.getPlayer().getLevelupXP());
        xpBar.setProgress((double)currentFightState.getPlayer().getCurrentXP()/(double)currentFightState.getPlayer().getLevelupXP());
    }

    public void fightOver(){
        fightBackButton.setDisable(false);
        fightBackButton.setVisible(true);

        nextFightButton.setVisible(true);
        nextFightButton.setDisable(false);

        fightButton.setDisable(true);
        fightButton.setVisible(false);

        xpBar.setProgress((double)currentFightState.getPlayer().getCurrentXP()/(double)currentFightState.getPlayer().getLevelupXP());
        xpLabel.setText("Your XP: "+currentFightState.getPlayer().getCurrentXP()+" / "+ currentFightState.getPlayer().getLevelupXP());

        enemyLevelLabel.setText(currentFightState.getCurrentEnemy().getName()+"'s level: "+currentFightState.getLevel());
        playerLevelLabel.setText("Your level: "+currentFightState.getPlayer().getLevel());
    }

    public void nextFightPressed(ActionEvent actionEvent) {

        nextFightButton.setDisable(true);
        nextFightButton.setVisible(false);

        fightButton.setDisable(false);
        fightButton.setVisible(true);

        fightBackButton.setDisable(true);
        fightBackButton.setVisible(false);

        fightTextArea.clear();

        enemyHealthLabel.setText(currentFightState.getCurrentEnemy().getName()+" HP: " +currentFightState.getCurrentEnemy().getCurrentHealth()+
                " / " + currentFightState.getCurrentEnemy().getMaxHealth());

        playerHealthLabel.setText("Your HP: " + currentFightState.getPlayer().getCurrentHealth() +
                " / " + currentFightState.getPlayer().getMaxHealth());

        xpBar.setProgress((double)currentFightState.getPlayer().getCurrentXP()/(double)currentFightState.getPlayer().getLevelupXP());
        xpLabel.setText("Your XP: "+currentFightState.getPlayer().getCurrentXP()+" / "+ currentFightState.getPlayer().getLevelupXP());

    }

    public void fightButtonPressed(ActionEvent actionEvent) {
        currentFightState.round();

        killsToAdvanceLabel.setText("Kills To Advance: "+currentFightState.getCurrentKills() +
                " / " + currentFightState.getKillsToAdvance());

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
                " / " + currentFightState.getCurrentEnemy().getMaxHealth());

        if(currentFightState.getCurrentEnemy().die()){
            fightOver();
            fightTextArea.appendText("End of round "+ currentFightState.getRoundNumber() + ".\n");
            fightTextArea.appendText("You have defeated: "+currentFightState.getCurrentEnemy().getName()+".\n");;
            currentFightState.roundOver();
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
                " / "+ currentFightState.getPlayer().getMaxHealth());

        if(currentFightState.getPlayer().die()){
            fightOver();
            fightTextArea.appendText("End of round "+ currentFightState.getRoundNumber() + ".\n");
            fightTextArea.appendText("You have died and lost half your gold.\n");
            currentFightState.roundOver();
            return;
        }
        fightTextArea.appendText("End of round "+ currentFightState.getRoundNumber() + ".\n");
    }

    public void fightBackPressed(ActionEvent actionEvent) throws IOException, XPathExpressionException, ParserConfigurationException,
            SAXException, TransformerException {
        currentFightState.saveGame();
        Parent switcher = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/play.fxml"));
        Stage playStage = (Stage) fightBackButton.getScene().getWindow();
        playStage.getScene().setRoot(switcher);
    }
}
