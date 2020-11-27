package model.events;


public abstract class Event {
    protected String title;

    public Event(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public abstract String toString();
}
