package model.events;


public abstract class Event {
    protected String title;

    /**
     * Constructor
     * @param title
     */
    public Event(String title) {
        this.title = title;
    }

    /**
     * To get the title of the event
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * To format the string to printout the title based on 
     * what kind of the event is
     * @return String
     */
    public abstract String toString();
}
