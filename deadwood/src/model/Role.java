package model;

import javafx.scene.shape.Rectangle;

public class Role {
    private int rank;
    private String roleName;
    private boolean onCard;
    private String description;
    private Rectangle position;

    /**
     * Constructor
     * @param roleName
     * @param rank
     * @param description
     * @param onCard
     */
    public Role(String roleName, int rank, String description, Rectangle position, boolean onCard) {
        this.roleName = roleName;
        this.rank = rank;
        this.description = description;
        this.position = position;
        this.onCard = onCard;
    }

    /**
     * To check whether the role is on card or not
     * @return boolean
     */
	public boolean checkOnCard(){
         return onCard;
    }

    /**
     * To get the role description
     * @return String
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * To check whether the player can afford that role or not from their rank
     * @param pRank
     * @return boolean
     */
    public boolean checkRank(Player pRank){
        if(pRank.getRank() >= rank){
            return true;       
        } else{
            return false;
        }
    }

    /**
     * To get the rank of the role
     * @return int
     */
    public int getRank() {
        return rank;
    }
    
    /**
     * To get the role name
     * @return String
     */
    public String getRoleName() {
      return roleName;
    }

    /**
     * To get the position on the board of the role 
     * @return Rectangle
     */
    public Rectangle getPosition() {
        return position;
    }
    
    /**
     * To format the string to printout the information of the role
     * @return String
     */
    public String toString() {
        String str = String.format(
            "Role Name: %s %nRank: %d %nDescription: \"%s\" %n%s %n", 
            roleName, 
            rank, 
            description.trim(),
            onCard ? "Staring Role" : "Extra");
        return str;
    }
}