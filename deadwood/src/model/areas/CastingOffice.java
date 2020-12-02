package model.areas;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.shape.Polygon;
import model.*;


public class CastingOffice extends Area {
    private HashMap<Integer, Integer> moneyForRank;
    private HashMap<Integer, Integer> creditsForRank;
    
    /**
     * Constructor
     * @param moneyForRank
     * @param creditsForRank
     */
    public CastingOffice(Polygon polygon, HashMap<Integer, Integer> moneyForRank, HashMap<Integer, Integer> creditsForRank) {
        super("Casting Office",polygon);
        this.moneyForRank = moneyForRank;
        this.creditsForRank = creditsForRank;
    }

    /**
     * To get player's money for upgrading
     * @param rank
     * @return Integer
     */
    public Integer getMoneyForRank(int rank) {
        return moneyForRank.get(rank);
    }
    
    /**
     * To get player's credits for upgrading
     * @param rank
     * @return Integer
     */
    public Integer getCreditsForRank(int rank) {
        return creditsForRank.get(rank);
    }

    /**
     * To check whether player can afford the rank that they want
     * @param p
     * @param rank
     * @return boolean
     * @throws InvalidActionException
     */
    public boolean playerCanAffordRank(Player p, int rank) throws InvalidActionException {
        Integer money = moneyForRank.get(rank);
        Integer credits = creditsForRank.get(rank);
        
        if(money == null || credits == null) {
            throw new InvalidActionException(rank + " is not a valid rank.");
        }
        if(p.getRank() >= rank) {
            throw new InvalidActionException("must upgrade to rank greater than current rank");
        }
        return (p.getCredits() >= credits && p.getDollars() >= money);
    }

    /**
     * To get all the possibilities of all the ranks that the player can upgrade to
     * @param p
     * @return ArrayLIst<Integer>
     * @throws InvalidActionException
     */
    public ArrayList<Integer> affordablePlayerRanks(Player p){
        //int rank = p.getRank();
        ArrayList<Integer> affordableRanks = new ArrayList<Integer>();

        moneyForRank.forEach((k, v) -> {
            Integer money = moneyForRank.get(k);
            Integer credits = creditsForRank.get(k);
            if(p.getCredits() >= credits && p.getDollars() >= money) {
                affordableRanks.add(k);
            }
        });

        return affordableRanks;
    }

    /**
     * To get the rank in the form of string
     * @return ArrayList<String>
     */
    public ArrayList<String> getRankUpgradeStrings() {
        ArrayList<String> strings = new ArrayList<String>();

        moneyForRank.forEach((k, v) -> {
            String str = getUpgradeStringForRank(k);
            strings.add(str);
        });

        return strings;
    }

    /**
     * To get the upgrade in string for rank
     * @param r
     * @return String
     */
    public String getUpgradeStringForRank(int r) {
        String str = String.format(
                "Rank %d: $%d, %d credits", r, moneyForRank.get(r), creditsForRank.get(r));
    
        return str;
    }
    /**
     * To get the summary of the area, where is whose are the neighbors of Casting Office
     * @return String
     */
    public String getAreaSummary() {
        StringBuffer sb = new StringBuffer("in Casting Office\n");

        sb.append("Neighboring areas: \n");
        getNeighbors().forEach(b -> sb.append("  " + b.toString() + "\n"));
        return sb.toString();
    }

    /**
     * To format the name of Casting Office
     * @return String
     */
    public String toString() {
        return "Casting Office";
    }

}
