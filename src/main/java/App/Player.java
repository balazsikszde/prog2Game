package App;

public class Player extends Entity {
    int currentXP;
    int levelupXP;

    public int getCurrentXP() {
        return currentXP;
    }

    public int getRemainingXP(){
        return levelupXP-currentXP;
    }

    public void xpGain(int xpGot){
        while(xpGot>getRemainingXP()){
            currentXP=xpGot-getRemainingXP();
            level++;
            levelupXP=(level^2)+level;
        }
    }
}
