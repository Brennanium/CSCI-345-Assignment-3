package model.events;

import model.*;

public class ActEvent extends Event {
    private Player affectedPlayer;
    private boolean success;
    private int money;
    private int credits;

    /**
     * Constructor
     * @param affectedPlayer
     * @param success
     * @param money
     * @param credits
     */
    public ActEvent(Player affectedPlayer, boolean success, int money, int credits){
        super("Act Outcome");
        this.affectedPlayer = affectedPlayer;
        this.success = success;
        this.money = money;
        this.credits = credits;
    }

    /**
     * To format the string to printout the information of the player act
     * @return String
     */
    public String toString(){
        return String.format("%s %nPlayer name: %s %n%s", 
            success ? "Success! " : "Fail... ", 
            affectedPlayer.getName(),
            money == 0 && credits == 0 ? "" : "$" + money + ", " + credits + " credits\n");
    }
}

