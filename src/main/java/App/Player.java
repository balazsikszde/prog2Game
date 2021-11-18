package App;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
import java.util.ArrayList;

public class Player extends Entity {


    int currentXP;
    int levelupXP;

    int gold;

    int maxStamina=100;
    int currentStamina=100;

    ArrayList<Item> inventory = new ArrayList<>();

    Helmet currentHelmet;
    Chestplate currentChestplate;
    Leggings currentLeggings;
    Boots currentBoots;

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

        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "no");
        tf.setOutputProperty(OutputKeys.METHOD, "xml");

        DOMSource domSource = new DOMSource(doc);
        StreamResult sr = new StreamResult(new File("src/main/resources/XML/playerstate.xml"));
        tf.transform(domSource, sr);
    }

    public void initAttributes() {
        baseAttack+=level;
        baseDefense+=level;
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

    public void getItem(Item item){
        inventory.add(item);
    }

    public void removeItem(Item item){
        inventory.remove(item);
    }
}
