package model;

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
     * 
     * @return String
     */
    public String getColor(){
        return color;
    }

    /**
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @return int
     */
    public int getRank() {
        return pRank;
    }

    /**
     * 
     * @return Role
     */
    public Role getRole(){
        return role;
    }

    /**
     * 
     * @return int
     */
    public int getDollars() {
        return dollars;
    }
    
    /**
     * 
     * @return int
     */
    public int getCredits() {
        return credits;
    }
    /**
     * 
     * @return int
     */
    public int getCurrentScore() {
        return dollars + credits + (pRank * 5);
    }

    /**
     * 
     * @return int
     */
    public int getSuccessfulScenes() {
        return successfulScenes;
    }
    
    /**
     * 
     * @return Area
     */
    public Area getCurrentArea() {
        return currentArea;
    }

    /**
     * 
     * @return int 
     */
    public int getPracticeChips() {
        return practiceChips;
    }
    
    /**
     * 
     * @param newRank
     */
    public void setRank(int newRank){
        pRank = newRank;
    }

    /**
     * 
     * @param roleName
     */
    public void setRole(Role roleName){
        this.role = roleName;
    }

    /**
     * 
     */
    public void addPracticeChip(){
         this.practiceChips++;    
    }

    // convenience method
    /**
     * convenience method
     */
    public void rehearse(){
        addPracticeChip();    
    }

    /**
     * 
     */
    public void resetPracticeChips(){
         this.practiceChips = 0;  
    }
    
    /**
     * 
     * @param areaName
     */
    public void setArea(Area areaName){
         this.currentArea = areaName;    
    }
    
    /**
     * 
     * @param dollars
     * @param credits
     */
    public void pay(int dollars, int credits) {
        this.dollars += dollars;
        this.credits += credits;
    }
    
    /**
     * 
     */
    public void wrapScene() {
        successfulScenes++;
    }
    
    /**
     * 
     * @param dollars
     * @param credits
     * @return boolean
     */
    public boolean canAfford(int dollars, int credits) {
        return this.dollars >= dollars && this.credits >= credits;
    }
    
    /**
     * 
     * @param dollars
     * @param credits
     */
    public void buy(int dollars, int credits) {
        this.dollars -= dollars;
        this.credits -= credits;
    }

    /**
     * 
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
     * 
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