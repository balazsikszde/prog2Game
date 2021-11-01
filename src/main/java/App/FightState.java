package App;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class FightState {

    Player player;
    int level=1;
    int currentkills=0;
    ArrayList<Integer> killsToAdvance = new ArrayList<>();

    Entity currentEnemy;
    int roundNumber=0;

    int playerRoll;
    int playerDMG;
    boolean playerCrit;

    int enemyRoll;
    int enemyDMG;
    boolean enemyCrit;

    public FightState(Player player, ArrayList<Entity> allEnemies) throws ParserConfigurationException, IOException, SAXException {
        initEnemies(allEnemies);
        this.player = player;
        currentEnemy=allEnemies.get(level);
    }

    public int getPlayerRoll() {
        return playerRoll;
    }

    public int getEnemyRoll() {
        return enemyRoll;
    }

    public int getPlayerDMG() {
        return playerDMG;
    }

    public int getEnemyDMG() {
        return enemyDMG;
    }

    public boolean isPlayerCrit() {
        return playerCrit;
    }

    public boolean isEnemyCrit() {
        return enemyCrit;
    }

    public Entity getCurrentEnemy() {
        return currentEnemy;
    }

    public Player getPlayer() {
        return player;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public boolean criticalHit(int roll){
        Random r = new Random();
        return roll > r.nextInt(6) + 1;
    }


    public void initEnemies(ArrayList<Entity> enemies) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("src/main/resources/XML/enemies.xml"));

        NodeList list = document.getElementsByTagName("enemy");

        for(int i = 0; i< list.getLength();i++){
            Node node  = list.item(i);
            Element element = (Element) node;
            String name = document.getElementsByTagName("name").item(i).getTextContent();
            int level = Integer.parseInt(document.getElementsByTagName("level").item(i).getTextContent());
            int maxHealth = Integer.parseInt(document.getElementsByTagName("maxHealth").item(i).getTextContent());
            int baseAttack = Integer.parseInt(document.getElementsByTagName("baseAttack").item(i).getTextContent());
            int baseDefense = Integer.parseInt(document.getElementsByTagName("baseDefense").item(i).getTextContent());
            killsToAdvance.add(Integer.parseInt(document.getElementsByTagName("killsToAdvance").item(i).getTextContent()));
            enemies.add(new Entity(name,level,maxHealth,baseAttack,baseDefense));
        }
    }

    public void newRound(){
        currentEnemy.setCurrentHealth(currentEnemy.maxHealth);
        roundNumber=0;
    }

    public void round(){
        Random r = new Random();
        playerRoll=r.nextInt(6)+1;
        playerCrit=criticalHit(playerRoll);

        if(playerCrit){
            playerDMG=currentEnemy.takeDMG(player.totalAttack()*2);
        }
        else{
            playerDMG=currentEnemy.takeDMG(player.totalAttack());
        }
        if(currentEnemy.die()){
            roundNumber++;
            currentkills++;
            player.xpGain(currentEnemy.level);
            return;
        }

        enemyRoll=r.nextInt(6)+1;
        enemyCrit=criticalHit(enemyRoll);
        if(enemyCrit){
            enemyDMG = player.takeDMG(currentEnemy.getBaseAttack()*2);
        }
        else {
            enemyDMG = player.takeDMG(currentEnemy.getBaseAttack());
        }
        if(player.die()){
            roundNumber++;
            return;
        }
        roundNumber++;
    }
}


