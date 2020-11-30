package XML;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javafx.scene.shape.*;

import model.areas.*;
import model.*;
import model.Role;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class XMLParser {


   private ArrayList<Area> areas = new ArrayList<Area>();
   
   // building a document from the XML file
   public Document getDocFromFile(String filename) throws ParserConfigurationException {
 
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = null;
      
      try{
            doc = db.parse(filename);
      } catch (Exception ex){
            System.out.println("XML parse failure");
            ex.printStackTrace();
      }
      return doc;
      // exception handling
   
   }  
        
   /**
    * To read all the data and parse data from the board.xml file
    * @param d
    * @return ArrayList<Area>
    */
   public ArrayList<Area> readAreaData(Document d){
   
      Element root = d.getDocumentElement();
      
      NodeList sets = root.getElementsByTagName("set");
      Element set;
      Element trailer = (Element)root.getElementsByTagName("trailer").item(0);
      Element office = (Element)root.getElementsByTagName("office").item(0);

      Area area;
      String areaName;
      
      NodeList takes;
      Element take;
      int takeOrder;
      Rectangle[] takesCoords;
      int takesCount;

      Element pointsElement;
      NodeList pointNodes;
      Polygon polygon;

      Element sceneCardCoordsList;
      Rectangle sceneCardCoords;

      Element partsNode;
      NodeList parts;
      Element part;
      String roleName;
      Element roleArea;
      int rank;
      String description;
      Rectangle position;
      ArrayList<Role> roles;
      
      for (int i=0; i<sets.getLength();i++){

         //set
         set = (Element)sets.item(i);
         areaName = set.getAttributes().getNamedItem("name").getNodeValue();
         
         //shot tokens
         takes = set.getElementsByTagName("take");
         takesCount = takes.getLength();
         takesCoords = new Rectangle[takesCount];
         for(int j = 0; j<takes.getLength(); j++) {
            take = (Element)takes.item(j);
            takeOrder = Integer.parseInt(take.getAttributes().getNamedItem("number").getNodeValue());
            takesCoords[takeOrder - 1] = getCoordsFromElement((Element)take.getElementsByTagName("area").item(0));
         }

         sceneCardCoordsList = (Element)set.getElementsByTagName("area").item(0);
         sceneCardCoords = getCoordsFromElement(sceneCardCoordsList);
         
         //add polygons to sets
         pointsElement = (Element)set.getElementsByTagName("points").item(0);
         pointNodes = pointsElement.getElementsByTagName("point");
         polygon = addPolygon(pointNodes);

         //roles
         partsNode = (Element)set.getElementsByTagName("parts").item(0);
         parts = partsNode.getElementsByTagName("part");
         roles = new ArrayList<Role>();
         for (int j=0; j< parts.getLength(); j++){
            part = (Element)parts.item(j);

            roleName = part.getAttributes().getNamedItem("name").getNodeValue();
            rank = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
            description = part.getTextContent();
            roleArea = (Element)part.getElementsByTagName("area").item(0);
            position = getCoordsFromElement(roleArea);

            roles.add(new Role(roleName, rank, description, position, false));
         }
         
         //initialize Set
         area = new Set(
            areaName, 
            takesCount, 
            polygon, 
            roles.toArray(new Role[0]), 
            sceneCardCoords, 
            takesCoords);

         if(areaName != null && !isDuplicateArea(area))
            areas.add(area);
            
      }

      //tailers
      if(trailer != null){
         //get bounds of trailer
         pointsElement = (Element)trailer.getElementsByTagName("points").item(0);
         pointNodes = pointsElement.getElementsByTagName("point");
         polygon = addPolygon(pointNodes);

         areas.add(new Trailers(polygon));
      }
      
      //casting office
      if(office != null){
         //setup upgrade
         HashMap<Integer, Integer> moneyForRank = new HashMap<Integer, Integer>();
         HashMap<Integer, Integer> creditsForRank = new HashMap<Integer, Integer>();
         

         pointsElement = (Element)office.getElementsByTagName("points").item(0);
         pointNodes = pointsElement.getElementsByTagName("point");
         polygon = addPolygon(pointNodes);


         Element upgradesNode = (Element)office.getElementsByTagName("upgrades").item(0);
         NodeList upgrades = upgradesNode.getElementsByTagName("upgrade");
         Node upgrade;
         int level;
         String currency = "";
         int amount;
         for(int i = 0; i < upgrades.getLength(); i++){
            upgrade = upgrades.item(i);
            level = Integer.parseInt(upgrade.getAttributes().getNamedItem("level").getNodeValue());
            currency = upgrade.getAttributes().getNamedItem("currency").getNodeValue();
            amount = Integer.parseInt(upgrade.getAttributes().getNamedItem("amt").getNodeValue());
            if("dollar".equals(currency)) {
               moneyForRank.put(level, amount);
            } else if("credit".equals(currency)) {
               creditsForRank.put(level, amount);
            }
         }

         areas.add(new CastingOffice(polygon, moneyForRank,creditsForRank));
      }

      //add neighbors to sets
      Element neighborsElement;
      NodeList neighborNodes;
      
      for (int i=0; i<sets.getLength();i++){
         set = (Element)sets.item(i);
         String name = set.getAttributes().getNamedItem("name").getNodeValue();
         area = areas.stream()
            .filter(a -> a.getName().equalsIgnoreCase(name))
            .findAny()
            .orElse(null);
         if(area == null) continue;


         neighborsElement = (Element)set.getElementsByTagName("neighbors").item(0);
         neighborNodes = neighborsElement.getElementsByTagName("neighbor");
         addNeighbors(area, neighborNodes);
      }
      
      //add neighbors to trailers
      area = areas.stream()
         .filter(a -> a instanceof Trailers)
         .findAny()
         .orElse(null);
      if(area != null){
         neighborsElement = (Element)trailer.getElementsByTagName("neighbors").item(0);
         neighborNodes = neighborsElement.getElementsByTagName("neighbor");

         addNeighbors(area, neighborNodes);
      }

      //add neighbors to casting office
      area = areas.stream()
         .filter(a -> a instanceof CastingOffice)
         .findAny()
         .orElse(null);
      if(area != null){
         neighborsElement = (Element)office.getElementsByTagName("neighbors").item(0);
         neighborNodes = neighborsElement.getElementsByTagName("neighbor");

         addNeighbors(area, neighborNodes);
      }


      //add polygons to set
      for (int i=0; i<sets.getLength();i++){
         set = (Element)sets.item(i);
         String name = set.getAttributes().getNamedItem("name").getNodeValue();
         area = areas.stream()
            .filter(a -> a.getName().equalsIgnoreCase(name))
            .findAny()
            .orElse(null);
         if(area == null) continue;

         pointsElement = (Element)set.getElementsByTagName("points").item(0);
         pointNodes = pointsElement.getElementsByTagName("point");
         addPolygon(pointNodes);
      }

      return areas;
   }

   /**
    * To check whether the area is duplicate or not
    * @param area
    * @return boolean
    */
   private boolean isDuplicateArea(Area area){
      return areas.contains(area) || 
      areas.stream()
          .anyMatch(a -> a.getName().equalsIgnoreCase(area.getName()));
   }

   /**
    * To add all the neighbors to the specific area
    * @param area
    * @param neighborNodes
    */
   private void addNeighbors(Area area, NodeList neighborNodes){
      Node neighborNode;
      ArrayList<Area> neighbors = new ArrayList<Area>();
      ArrayList<String> neighborNames = new ArrayList<String>();
      for (int i=0; i<neighborNodes.getLength();i++){
         neighborNode = neighborNodes.item(i);
         neighborNames.add(neighborNode.getAttributes().getNamedItem("name").getNodeValue());
      }

      neighbors = new ArrayList<Area>(
         areas.stream()
            .filter(a -> neighborNames.contains(a.getName()))
            .collect(Collectors.toList())
      );

      area.setNeighbors(neighbors);
   }

   /**
    * To add the polygon from the point nodes
    * @param pointNodes
    * @return Polygon
    */
   private Polygon addPolygon(NodeList pointNodes){
      Node pointNode;
      ArrayList<Double> coordinates = new ArrayList<Double>();

      for (int i=0; i<pointNodes.getLength();i++){
         pointNode = pointNodes.item(i);
         coordinates.add(Double.parseDouble(pointNode.getAttributes().getNamedItem("x").getNodeValue()));
         coordinates.add(Double.parseDouble(pointNode.getAttributes().getNamedItem("y").getNodeValue()));
      }

      Polygon polygon = new Polygon();
      polygon.getPoints().addAll(coordinates);

      return polygon;
   }

   /**
    * To get the coordinates from the element
    * @param e
    * @return Rectangle
    */
   private static Rectangle getCoordsFromElement(Element e){
      return new Rectangle(
            Integer.parseInt(e.getAttributes().getNamedItem("x").getNodeValue()), 
            Integer.parseInt(e.getAttributes().getNamedItem("y").getNodeValue()), 
            Integer.parseInt(e.getAttributes().getNamedItem("w").getNodeValue()), 
            Integer.parseInt(e.getAttributes().getNamedItem("h").getNodeValue()));
   }
   
   /**
    * To read all the data and parse data from the card.xml file
    * @param d
    * @return ArrayList<SceneCard>
    */
   public ArrayList<SceneCard> readSceneData(Document d){

      ArrayList<SceneCard> sceneInfo = new ArrayList<SceneCard>();
      SceneCard scene;

      Element root = d.getDocumentElement();
      
      NodeList cards = root.getElementsByTagName("card");
   
      NodeList parts;
      Element part;
      String roleName;
      int rank;
      String description;
      ArrayList<Role> roles;
      Element roleArea;
      Rectangle roleCoords;

      //add all the scene cards
      for (int i=0; i<cards.getLength();i++){

         //reads data from the nodes
         Element card = (Element)cards.item(i);
         
         String cardName = card.getAttributes().getNamedItem("name").getNodeValue();
         String image = card.getAttributes().getNamedItem("img").getNodeValue();
         int budget = Integer.parseInt(card.getAttributes().getNamedItem("budget").getNodeValue());
  
         //reads data                     
         NodeList children = card.getChildNodes();

         Node sceneNode = children.item(1);
         int sceneNum = Integer.parseInt(sceneNode.getAttributes().getNamedItem("number").getNodeValue());
         String sceneDescr = sceneNode.getTextContent().trim();

         parts = card.getElementsByTagName("part");
         roles = new ArrayList<Role>();
         for (int j=0; j< parts.getLength(); j++){
            part = (Element)parts.item(j);

            roleName = part.getAttributes().getNamedItem("name").getNodeValue();
            rank = Integer.parseInt(part.getAttributes().getNamedItem("level").getNodeValue());
            roleArea = (Element)part.getElementsByTagName("area").item(0);
            
            roleCoords = getCoordsFromElement(roleArea);
            
            description = part.getTextContent();

            roles.add(new Role(roleName, rank, description, roleCoords, true));
         }
         
         //initialize new SceneCard
         scene = new SceneCard(cardName, sceneNum, budget, sceneDescr, roles.toArray(new Role[0]), image); 
         //add new scene to ArrayList
         sceneInfo.add(scene);
      }

      return sceneInfo;
   
   }// method

}//class