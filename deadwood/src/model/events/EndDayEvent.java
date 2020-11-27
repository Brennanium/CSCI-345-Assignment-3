package model.events;


public class EndDayEvent extends Event {
    private int daysLeft;
    private int maxDays;

    /**
     * Constructor
     * @param daysLeft
     * @param maxDays
     */
    public EndDayEvent(int daysLeft, int maxDays){
        super("Day has ended");
        this.daysLeft = daysLeft;
        this.maxDays = maxDays;
    }

    /**
     * To format the string to printout the information of the game days
     * 
     */
    public String toString(){
        int day = maxDays - daysLeft;
        return String.format(
            "%s has ended. There are %d days left.%n", 
            day == maxDays ? "The final day" : "Day " + day, 
            daysLeft);
    }
}