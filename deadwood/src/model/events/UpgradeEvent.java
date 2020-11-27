package model.events;

import model.*;

public class UpgradeEvent extends Event {
    private Player affectedPlayer;
    private int oldRank;
    private int newRank;

    public UpgradeEvent(Player affectedPlayer, int oldRank, int newRank){
        super("Upgrade Success");
        this.affectedPlayer = affectedPlayer;
        this.oldRank = oldRank;
        this.newRank = newRank;
    }

    public String toString(){
        String str = String.format
            ("Congrats! %s has upgraded from rank %d to rank %d!",
            affectedPlayer.getName(), oldRank, newRank);
        return str;
    }
}

