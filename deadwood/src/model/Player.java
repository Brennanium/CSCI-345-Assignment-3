package model;

import java.util.ArrayList;

import java.util.HashSet;

import model.areas.*;

public class Player{

    private String name;
    private int pRank;
    private Role role;
    private int dollars;
    private int credits;
    private int successfulScenes;
    private Area currentArea;
    private int practiceChips;
    private String color;

    HashSet<PlayerObserver> observers = new HashSet<PlayerObserver>();
    
    /**
     * Constructor
     * 
     * @param pName
     * @param color
     */
    public Player(String pName, String color){
        this.name = pName;
        this.color = color;
        this.pRank = 1;
    }

    /**
     * To get the color of the player
     * @return String
     */
    public String getColor(){
        return color;
    }

    /**
     * To get player name
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * To get player rank
     * @return int
     */
    public int getRank() {
        return pRank;
    }

    /**
     * To get the role of the player
     * @return Role
     */
    public Role getRole(){
        return role;
    }

    /**
     * To get the number of money of the player
     * @return int
     */
    public int getDollars() {
        return dollars;
    }
    
    /**
     * To get the number of credits of the player
     * @return int
     */
    public int getCredits() {
        return credits;
    }
    
    /**
     * To get the score of the player
     * @return int
     */
    public int getCurrentScore() {
        return dollars + credits + (pRank * 5);
    }

    /**
     * To get number of player successful scenes
     * @return int
     */
    public int getSuccessfulScenes() {
        return successfulScenes;
    }
    
    /**
     * To get the placurrent area of the player
     * @return Area
     */
    public Area getCurrentArea() {
        return currentArea;
    }

    /**
     * To get player practice chips
     * @return int 
     */
    public int getPracticeChips() {
        return practiceChips;
    }

    /**
     * To get the dice image name
     * @return String
     */
    public String getImageString() {
        return color.substring(0, 1) + pRank + ".png";
    }
    
    /**
     * To set player rank
     * @param newRank
     */
    public void setRank(int newRank){
        pRank = newRank;
        updateObservers();
    }

    /**
     * Set the role to that player
     * @param roleName
     */
    public void setRole(Role roleName){
        this.role = roleName;
        updateObservers();
    }

    /**
     * Update the practice chips of the player
     */
    public void addPracticeChip(){
         this.practiceChips++; 
         updateObservers();   
    }

    // convenience method
    /**
     * To add the number of practice chips to that player when they rehearse
     */
    public void rehearse(){
        addPracticeChip();    
    }

    /**
     * To reset the number of practice chips
     */
    public void resetPracticeChips(){
         this.practiceChips = 0;  
         updateObservers();
    }
    
    /**
     *  To set player area
     * @param areaName
     */
    public void setArea(Area areaName){
         this.currentArea = areaName;   
         updateObservers(); 
    }
    
    /**
     * To pay the player money and credits
     * @param dollars
     * @param credits
     */
    public void pay(int dollars, int credits) {
        this.dollars += dollars;
        this.credits += credits;
        updateObservers();
    }
    
    /**
     * To wrap the scene
     */
    public void wrapScene() {
        successfulScenes++;
        updateObservers();
    }
    
    /**
     * To check whether the player have enough money to upgrade or not
     * @param dollars
     * @param credits
     * @return boolean
     */
    public boolean canAfford(int dollars, int credits) {
        return this.dollars >= dollars && this.credits >= credits;
    }
    
    /**
     * To update the player money and credits after player upgrade
     * @param dollars
     * @param credits
     */
    public void buy(int dollars, int credits) {
        this.dollars -= dollars;
        this.credits -= credits;
        updateObservers();
    }

    /**
     * To check whether the player is valid to get take that role or not
     * @param checkRank
     * @return boolean
     */
    public boolean isRoleValid(Role checkRank){
      if(pRank >= checkRank.getRank()){
         return true;
      } else {
         return false;
      }
    }

    /**
     * To update the observers
     */
    private void updateObservers() {
        for(PlayerObserver po : observers) {
            po.update(this);
        }
    }

    /**
     * 
     * To add the observer
     * @param po
     */
    public void addObserver(PlayerObserver po) {
        observers.add(po);
    }

    /**
     * To remove the observer
     * @param po
     */
    public void removeObserver(PlayerObserver po) {
        observers.remove(po);
    }

    /**
     * To format the string to printout the information of the player
     * @return String
     */
    public String toString() {
        String str = "";
        if(role != null){
            str = String.format(
                "Player: %s %nRank: %d %nDollars: %d %nCredits: %d %n" +  
                "Number of Practice Chips: %d %nNumber of Successful Scenes: %d %n" +
                "Role: %s %nCurrent Area: %s %n", 
                name, pRank, dollars, credits, practiceChips, successfulScenes, role.getRoleName(), currentArea);
        } else {
            str = String.format(
                "Player: %s %nRank: %d %nDollars: %d %nCredits: %d %n" +  
                "Number of Successful Scenes: %d %nCurrent Area: %s %n", 
                name, pRank, dollars, credits, successfulScenes, currentArea);
        }
        return str;
    }
}