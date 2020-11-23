package model.events;


public class EndDayEvent extends Event {
    private int daysLeft;
    private int maxDays;

    public EndDayEvent(int daysLeft, int maxDays){
        this.daysLeft = daysLeft;
        this.maxDays = maxDays;
    }

    public String toString(){
        int day = maxDays - daysLeft;
        return String.format(
            "%s has ended. There are %d days left.%n", 
            day == maxDays ? "The final day" : "Day " + day, 
            daysLeft);
    }
}