package App.GUI;

import App.Player;
import App.Wolf;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.util.Random;



public class FightController {
    @FXML TextArea fightTextArea;

    Wolf wolf = new Wolf();
    Player player = new Player();
    boolean playerTurn = true;

    public boolean criticalHit(int roll){
        Random r = new Random();
        return roll > r.nextInt(6) + 1;
    }

    public void fightButtonPressed(ActionEvent actionEvent) {
        Random r = new Random();
        int roll;
        if (playerTurn)
        {
            roll=r.nextInt(6)+1;
            int dmg;
            if(criticalHit(roll))
            {
                dmg=player.getBaseAttack()*2-wolf.getBaseDefense();
                fightTextArea.appendText("you rolled a: "+roll+" ");
                fightTextArea.appendText("CRITICAL HIT\n");
            }
            else{
                dmg=player.getBaseAttack()-wolf.getBaseDefense();
                fightTextArea.appendText("you rolled a: "+roll+ "\n");
            }
            fightTextArea.appendText("player dealt dmg: "+ dmg+"\n");
            wolf.takeDMG(41);
            fightTextArea.appendText("wolf hp: "+wolf.getCurrentHealth()+"\n");
            if(wolf.die()){
                player.setCurrentHealth(player.getMaxHealth());
                fightTextArea.appendText("you defeated wolf\n");
            }
            playerTurn=false;
        }
        else{
            roll=r.nextInt(6)+1;
            int dmg;
            if(criticalHit(roll))
            {
                dmg=player.getBaseAttack()*2-wolf.getBaseDefense();
                fightTextArea.appendText("you rolled a: "+roll);
                fightTextArea.appendText("CRITICAL HIT");
            }
            else {
                dmg = player.getBaseAttack() - wolf.getBaseDefense();
                fightTextArea.appendText("you rolled a: "+roll+ "\n");
            }
            fightTextArea.appendText("wolf dealt dmg: "+ dmg+"\n");
            player.takeDMG(wolf.getBaseAttack()- player.getBaseDefense());
            fightTextArea.appendText("your hp: "+player.getCurrentHealth()+"\n");
            if(player.die()){
                fightTextArea.appendText("you dead\n");
            }
            playerTurn=true;
        }
    }
}
