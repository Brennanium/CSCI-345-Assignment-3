package model.areas;

import java.util.ArrayList;
import java.util.HashMap;

import model.*;


public class CastingOffice extends Area {
    private HashMap<Integer, Integer> moneyForRank;
    private HashMap<Integer, Integer> creditsForRank;
    
    /**
     * 
     * @param moneyForRank
     * @param creditsForRank
     */
    public CastingOffice(HashMap<Integer, Integer> moneyForRank, HashMap<Integer, Integer> creditsForRank) {
        super("office");
        this.moneyForRank = moneyForRank;
        this.creditsForRank = creditsForRank;
    }

    /**
     * 
     * @param rank
     * @return Integer
     */
    public Integer getMoneyForRank(int rank) {
        return moneyForRank.get(rank);
    }
    
    /**
     * 
     * @param rank
     * @return Integer
     */
    public Integer getCreditsForRank(int rank) {
        return creditsForRank.get(rank);
    }

    /**
     * 
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
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getRankUpgradeStrings() {
        ArrayList<String> strings = new ArrayList<String>();

        moneyForRank.forEach((k, v) -> {
            String str = String.format(
                "Rank %d: $%d, %d credits", k, v, creditsForRank.get(k));
            strings.add(str);
        });

        return strings;
    }

    /**
     * 
     * @return String
     */
    public String getAreaSummary() {
        StringBuffer sb = new StringBuffer("in Casting Office\n");

        sb.append("Neighboring areas: \n");
        getNeighbors().forEach(b -> sb.append("  " + b.toString() + "\n"));
        return sb.toString();
    }

    /**
     * 
     * @return String
     */
    public String toString() {
        return "Casting Office";
    }

}
