package model.areas;

import model.*;

import java.util.ArrayList;

public abstract class Area {
    private String areaName;
    protected ArrayList<Player> occupants;
    private ArrayList<Area> neighbors;
    private boolean isSet;

    /**
     * Constructor
     * 
     * @param name
     */
    public Area(String name) {
        occupants = new ArrayList<Player>();
        areaName = name;
    }

    /**
     * 
     * @return String
     */
    public String getName() {
        return areaName;
    }

    /**
     * 
     * @return boolean
     */
    public boolean getIsSet() {
        return isSet;
    }
    
    /**
     * 
     * @param area
     * @return boolean
     */
    public boolean isNeighbor(Area area) {
        return neighbors.contains(area);
    }
    
    /**
     * 
     * @return boolean
     */
    public boolean isCastingOffice(){
        return areaName.equalsIgnoreCase("Casting Office");
    }
    
    /**
     * 
     * @param p
     */
    public void addPlayer(Player p) {
        occupants.add(p);
    }
    
    /**
     * 
     * @param neighbors
     */
    public void setNeighbors(ArrayList<Area> neighbors){
        this.neighbors = neighbors;
    }
    
    /**
     * 
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getNeighbors(){
        return neighbors;
    }
    
    /**
     * 
     * @param p
     */
    public void removePlayer(Player p) {
        occupants.remove(p);
    }

    /**
     * 
     * @return String
     */
    public abstract String getAreaSummary();

    /**
     * 
     * @return String
     */
    public String toString(){
        return areaName;
    }

}