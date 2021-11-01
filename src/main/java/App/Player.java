package App;

import java.util.ArrayList;

public class Player extends Entity {
    int currentXP;
    int levelupXP;

    int maxStamina=100;
    int currentStamina=100;

    ArrayList<Item> inventory = new ArrayList<>();

    Helmet currentHelmet;
    Chestplate currentChestplate;
    Leggings currentLeggings;
    Boots currentBoots;

    public Player() {
        super("Player", 1, 100, 1, 0);
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getRemainingXP(){
        return levelupXP-currentXP;
    }


    public int totalAttack(){
        return baseAttack/*+currentHelmet.getAttack()+currentChestplate.getAttack()
                +currentLeggings.getAttack()+ currentBoots.getAttack()*/;
    }

    public void xpGain(int xpGot){
        currentXP+=xpGot;
        levelUP();
    }
    public void levelUP(){
        while(0>getRemainingXP()) {
            currentXP-=levelupXP;
            level++;
            levelupXP = (level ^ 2) + level;
            baseAttack++;
            baseDefense++;
        }
    }
}
