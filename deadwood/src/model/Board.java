package model;

import model.areas.*;
import model.areas.Set;
import XML.*;

import java.util.*;
import java.util.stream.*;
import java.util.ArrayList;

import org.w3c.dom.Document;


//singleton class
public class Board {
    private final String DEFAULT_XML_FILEPATH = "./src/resources/XML/"; 

    private static Board instance = new Board();
    
    private ArrayList<Area> areas;
    private ArrayList<SceneCard> undealtSceneCards;
    private ArrayList<SceneCard> dealtSceneCards;

    /**
     * Constructor
     */
    private Board() {
        getAreasFromXML();
        getScenesFromXML();
        dealtSceneCards = new ArrayList<SceneCard>();
    }

    /**
     * 
     */
    private void getAreasFromXML() {
        Document boardDoc = null;
        XMLParser parsing = new XMLParser();
        
        try {
            boardDoc = parsing.getDocFromFile(DEFAULT_XML_FILEPATH + "board.xml");
            areas = parsing.readAreaData(boardDoc);
            if(areas == null) 
                System.out.println("areas is null");

        }  catch(Exception e){
            System.out.println("Error = " + e);
        }
    }

    /**
     * 
     */
    private void getScenesFromXML() {
        Document cardsDoc = null;
        XMLParser parsing = new XMLParser();
        
        try {
            cardsDoc = parsing.getDocFromFile(DEFAULT_XML_FILEPATH + "cards.xml");
            undealtSceneCards = parsing.readSceneData(cardsDoc);
            if(undealtSceneCards == null) 
                System.out.println("undealtSceneCards is null");

        }  catch(Exception e){
            System.out.println("Error = " + e);
        }
    }
    
    /**
     * 
     * @param newArea
     */
    public void addArea(Area newArea) {
        if(!isDuplicateArea(newArea)){
            areas.add(newArea);
        }
    }

    /**
     * 
     */
    public void dealSceneCards() {
        ArrayList<Set> sets = getSets();

        if(sets.size() <= undealtSceneCards.size()){
            sets.forEach(a -> {
                a.reset();
                SceneCard rand = getRandomSceneCard();
                rand.dealScene();
                a.setSceneCard(rand);
            });
        }        
    }

    /**
     * 
     * @param areaString
     * @return Area
     */
    public Area getAreaForString(String areaString) {
        return areas.stream()
            .filter(a -> a.getName().equalsIgnoreCase(areaString))
            .findAny()
            .orElse(null);
    }

    /**
     * 
     * @return int
     */
    public int getNumberOfRemainingScenes() {
        return undealtSceneCards.size();
    }
    
    /**
     * 
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getAreas() {
        return areas;
    }
    
    /**
     * 
     * @return ArrayList<Set>
     */
    private ArrayList<Set> getSets() {
        if(areas == null) {
            getAreasFromXML();
        }
        List<Set> sets = areas.stream()
            .filter(a -> a instanceof Set)
            .map((Area a) -> (Set)a)
            .collect(Collectors.toList());
        return new ArrayList<Set>(sets);
    }
    
    /**
     * 
     * @return SceneCard
     */
    private SceneCard getRandomSceneCard() {
        int randomIndex = (int)((Math.random()*undealtSceneCards.size()));
        SceneCard randomCard = undealtSceneCards.remove(randomIndex);
        dealtSceneCards.add(randomCard);
        return randomCard;
    }
    
    /**
     * 
     * @param area
     * @return boolean
     */
    private boolean isDuplicateArea(Area area){
        return areas.contains(area) || 
            areas.stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(area.getName()));
    }

    /**
     * 
     * @return int
     */
    public int getNumberOfActiveScenes(){
        int i = 0;
        for(Set a : getSets()){
            if(a.hasActiveScene()) {
                i++;
            }
        }
        return i;
    }

    /* public String toString() {
        return "";
    } */

    /**
     * getInstance
     * 
     * Singleton method
     * @return Board
     */
    public static Board getInstance() {
        return instance;
    }
}