package App;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class FightState {

    Player player;
    int level=1;

    int currentKills=0;

    ArrayList<Integer> killsToAdvance = new ArrayList<>();
    ArrayList<Entity> enemies = new ArrayList<>();

    Entity currentEnemy;
    int roundNumber=0;

    int playerRoll;
    int playerDMG;
    boolean playerCrit;

    int enemyRoll;
    int enemyDMG;
    boolean enemyCrit;

    public FightState() throws IOException, SAXException, ParserConfigurationException {
        this.player= new Player();
        player.initAttributes();

        DocumentBuilderFactory dbfgs = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbgs = dbfgs.newDocumentBuilder();
        Document documentgs = dbgs.parse(new File("src/main/resources/XML/gamestate.xml"));
        String gsLevel = documentgs.getElementsByTagName("level").item(0).getTextContent();
        level= Integer.parseInt(gsLevel);
        String gsCurrentKills = documentgs.getElementsByTagName("currentKills").item(0).getTextContent();
        currentKills= Integer.parseInt(gsCurrentKills);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("src/main/resources/XML/enemies.xml"));
        NodeList list = document.getElementsByTagName("enemy");
        for(int i = 0; i< list.getLength();i++){
            Node node  = list.item(i);
            Element element = (Element) node;
            String name = document.getElementsByTagName("name").item(i).getTextContent();
            int enemyLevel = Integer.parseInt(document.getElementsByTagName("level").item(i).getTextContent());
            int maxHealth = Integer.parseInt(document.getElementsByTagName("maxHealth").item(i).getTextContent());
            int baseAttack = Integer.parseInt(document.getElementsByTagName("baseAttack").item(i).getTextContent());
            int baseDefense = Integer.parseInt(document.getElementsByTagName("baseDefense").item(i).getTextContent());
            killsToAdvance.add(Integer.parseInt(document.getElementsByTagName("killsToAdvance").item(i).getTextContent()));
            enemies.add(new Entity(name,enemyLevel,maxHealth,baseAttack,baseDefense));
        }
        this.currentEnemy=enemies.get(level);
    }

    public int getLevel() {
        return level;
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
    
    public int getCurrentKills() {
        return currentKills;
    }

    public int getKillsToAdvance() {
        return killsToAdvance.get(level);
    }

    public void nextEnemy(){
        currentEnemy=enemies.get(level);
    }

    public boolean criticalHit(int roll){
        Random r = new Random();
        return roll > r.nextInt(6) + 1;
    }

    public void roundOver(){
        if(player.die()) {
            player.setCurrentHealth(player.getMaxHealth());
        }
        currentEnemy.setCurrentHealth(currentEnemy.getMaxHealth());
        roundNumber=0;
    }

    public void saveGame() throws ParserConfigurationException, IOException, SAXException, XPathExpressionException, TransformerException {
        player.savePlayer();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("src/main/resources/XML/gamestate.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node levelNode = (Node) xPath.compile("/gamestate/level").evaluate(doc, XPathConstants.NODE);
        levelNode.setTextContent(String.valueOf(level));

        xPath = XPathFactory.newInstance().newXPath();
        Node currentKillsNode = (Node) xPath.compile("/gamestate/currentKills").evaluate(doc, XPathConstants.NODE);
        currentKillsNode.setTextContent(String.valueOf(currentKills));

        Transformer tf = TransformerFactory.newInstance().newTransformer();
       tf.setOutputProperty(OutputKeys.INDENT, "no");
        tf.setOutputProperty(OutputKeys.METHOD, "xml");

        DOMSource domSource = new DOMSource(doc);
        StreamResult sr = new StreamResult(new File("src/main/resources/XML/gamestate.xml"));
        tf.transform(domSource, sr);
    }


    public void round(){
        currentEnemy= enemies.get(level);
        Random r = new Random();
        playerRoll=r.nextInt(6)+1;
        playerCrit=criticalHit(playerRoll);
        playerDMG=currentEnemy.takeDMG(player.totalAttack());
        if(playerCrit){
            playerDMG*=2;
        }
        if(currentEnemy.die()){
            roundNumber++;
            currentKills++;
            player.goldGain(currentEnemy.level);
            player.xpGain(currentEnemy.level);
            if(currentKills==getKillsToAdvance()){
                level++;
                nextEnemy();
                currentKills=0;
            }
            return;
        }

        enemyRoll=r.nextInt(6)+1;
        enemyCrit=criticalHit(enemyRoll);
        enemyDMG = player.takeDMG(currentEnemy.getBaseAttack());
        if(enemyCrit){
            enemyDMG*=2;
        }
        if(player.die()){
            roundNumber++;
            player.goldGain(-1*(player.getGold()/2));
            return;
        }
        roundNumber++;
    }
}


