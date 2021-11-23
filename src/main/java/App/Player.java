package App;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;

public class Player extends Entity {


    int currentXP;
    int levelupXP;

    private int gold;

    int maxStamina=100;
    int currentStamina=100;

    int helmetLevel;
    int chestplateLevel;
    int leggingsLevel;
    int bootsLevel;

    public Player() throws ParserConfigurationException, IOException, SAXException {
        super("Player", 1, 100, 2, 0);
        this.initAttributes();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(new File("src/main/resources/XML/playerstate.xml"));

        String Level = document.getElementsByTagName("level").item(0).getTextContent();
        this.level= Integer.parseInt(Level);
        String CurrentHealth = document.getElementsByTagName("currentHealth").item(0).getTextContent();
        this.currentHealth= Integer.parseInt(CurrentHealth);
        String CurrentXP = document.getElementsByTagName("currentXP").item(0).getTextContent();
        this.currentXP= Integer.parseInt(CurrentXP);
        String gold = document.getElementsByTagName("gold").item(0).getTextContent();
        this.gold= Integer.parseInt(gold);

        String helmetLevel = document.getElementsByTagName("helmetLevel").item(0).getTextContent();
        this.helmetLevel= Integer.parseInt(helmetLevel);
        String chestplateLevel = document.getElementsByTagName("chestplateLevel").item(0).getTextContent();
        this.chestplateLevel= Integer.parseInt(chestplateLevel);
        String leggingsLevel = document.getElementsByTagName("leggingsLevel").item(0).getTextContent();
        this.leggingsLevel= Integer.parseInt(leggingsLevel);
        String bootsLevel = document.getElementsByTagName("bootsLevel").item(0).getTextContent();
        this.bootsLevel= Integer.parseInt(bootsLevel);

    }

    public int getCurrentXP() {
        return currentXP;
    }

    public int getLevelupXP() {
        return levelupXP;
    }

    public int getRemainingXP(){
        return levelupXP-currentXP;
    }

    public int totalAttack(){
        return baseAttack/*+currentHelmet.getAttack()+currentChestplate.getAttack()
                +currentLeggings.getAttack()+ currentBoots.getAttack()*/;
    }

    public int getGold() {
        return gold;
    }

    public void goldGain(int g) {
        gold+=g;
    }

    public void xpGain(int xpGot){
        currentXP+=xpGot;
        levelUP();
    }

    public void savePlayer() throws ParserConfigurationException, IOException, SAXException,
            XPathExpressionException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File("src/main/resources/XML/playerstate.xml"));

        XPath xPath = XPathFactory.newInstance().newXPath();
        Node levelNode = (Node) xPath.compile("/playerstate/level").evaluate(doc, XPathConstants.NODE);
        levelNode.setTextContent(String.valueOf(level));

        xPath = XPathFactory.newInstance().newXPath();
        Node currentHealthNode = (Node) xPath.compile("/playerstate/currentHealth").evaluate(doc, XPathConstants.NODE);
        currentHealthNode.setTextContent(String.valueOf(currentHealth));

        xPath = XPathFactory.newInstance().newXPath();
        Node currentXPNode = (Node) xPath.compile("/playerstate/currentXP").evaluate(doc, XPathConstants.NODE);
        currentXPNode.setTextContent(String.valueOf(currentXP));

        xPath = XPathFactory.newInstance().newXPath();
        Node goldNode = (Node) xPath.compile("/playerstate/gold").evaluate(doc, XPathConstants.NODE);
        goldNode.setTextContent(String.valueOf(gold));

        xPath = XPathFactory.newInstance().newXPath();
        Node helmetLevelNode = (Node) xPath.compile("/playerstate/helmetLevel").evaluate(doc, XPathConstants.NODE);
        helmetLevelNode.setTextContent(String.valueOf(helmetLevel));

        xPath = XPathFactory.newInstance().newXPath();
        Node chestplateLevelNode = (Node) xPath.compile("/playerstate/chestplateLevel").evaluate(doc, XPathConstants.NODE);
        chestplateLevelNode.setTextContent(String.valueOf(chestplateLevel));

        xPath = XPathFactory.newInstance().newXPath();
        Node leggingsLevelNode = (Node) xPath.compile("/playerstate/leggingsLevel").evaluate(doc, XPathConstants.NODE);
        leggingsLevelNode.setTextContent(String.valueOf(leggingsLevel));

        xPath = XPathFactory.newInstance().newXPath();
        Node bootsLevelNode = (Node) xPath.compile("/playerstate/bootsLevel").evaluate(doc, XPathConstants.NODE);
        bootsLevelNode.setTextContent(String.valueOf(bootsLevel));

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "no");
        tf.setOutputProperty(OutputKeys.METHOD, "xml");

        DOMSource domSource = new DOMSource(doc);
        StreamResult sr = new StreamResult(new File("src/main/resources/XML/playerstate.xml"));
        tf.transform(domSource, sr);
    }

    public void initAttributes() {
        baseAttack+=level+bootsLevel;
        baseDefense+=level+helmetLevel+chestplateLevel+leggingsLevel;
        levelupXP=5*level;
    }

    public void levelUP(){
        while(0>=getRemainingXP()) {
            currentXP-=levelupXP;
            level++;
            levelupXP=5*level;
            baseAttack++;
            baseDefense++;
        }
    }

    public int getHelmetLevel() {
        return helmetLevel;
    }

    public int getChestplateLevel() {
        return chestplateLevel;
    }

    public int getLeggingsLevel() {
        return leggingsLevel;
    }

    public int getBootsLevel() {
        return bootsLevel;
    }

    public boolean upgrade(int n){
        if(gold>=upgradeCost(n)){
            gold-=upgradeCost(n);
            switch (n) {
                case 0:
                        return true;
                case 1:
                    helmetLevel++;
                    return true;
                case 2:
                    chestplateLevel++;
                    return true;
                case 3:
                    leggingsLevel++;
                    return true;
                case 4:
                    bootsLevel++;
                    return true;
            }
        }
        return false;
    }

    public int upgradeCost(int n){
        switch (n){
            case 1:
                return helmetLevel+1;
            case 2:
                return chestplateLevel+1;
            case 3:
                return leggingsLevel+1;
            case 4:
                return bootsLevel+1;
        }
        return n;
    }

}
