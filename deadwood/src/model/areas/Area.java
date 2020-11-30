package model.areas;

import model.*;

import java.util.ArrayList;

import javafx.scene.shape.Polygon;

public abstract class Area {
    private String areaName;
    protected ArrayList<Player> occupants;
    private ArrayList<Area> neighbors;
    private boolean isSet;
    private Polygon bounds;

    /**
     * Constructor
     * 
     * @param name
     */
    public Area(String name, Polygon polygon) {
        occupants = new ArrayList<Player>();
        areaName = name;
        bounds = polygon;
    }

    /**
     * To get the name of the area
     * @return String
     */
    public String getName() {
        return areaName;
    }

    /**
     * To check whether the area is a set or not
     * @return boolean
     */
    public boolean getIsSet() {
        return isSet;
    }

    public Polygon getPolygon() {
        return bounds;
    }
    
    /**
     * To check whether a specific area is a neighbor of them or not
     * @param area
     * @return boolean
     */
    public boolean isNeighbor(Area area) {
        return neighbors.contains(area);
    }
    
    /**
     * To check whether the area is the casting office or not
     * @return boolean
     */
    public boolean isCastingOffice(){
        return areaName.equalsIgnoreCase("Casting Office");
    }
    
    /**
     * To add the player
     * @param p
     */
    public void addPlayer(Player p) {
        occupants.add(p);
    }
    
    /**
     * To set all the neighbors that in that area
     * @param neighbors
     */
    public void setNeighbors(ArrayList<Area> neighbors){
        this.neighbors = neighbors;
    }
    
    /**
     * To get all the neighbors in the area
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getNeighbors(){
        return neighbors;
    }
    
    /**
     * To remove the player
     * @param p
     */
    public void removePlayer(Player p) {
        occupants.remove(p);
    }

    /**
     * To get the summary of the area
     * @return String
     */
    public abstract String getAreaSummary();

    /**
     * To format the string to printout the information of the area
     * @return String
     */
    public String toString(){
        return areaName;
    }

}