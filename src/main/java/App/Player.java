package App;

public class Player extends Entity {
    int currentXP;
    int levelupXP;

    public Player() {
        super("Player", 1, 100, 100, 1, 1);
    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getRemainingXP(){
        return levelupXP-currentXP;
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
