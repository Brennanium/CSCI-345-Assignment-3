package model;

import model.events.*;

public interface EventObserver {

    /**
     * Interface
     */
    public void update(Event event);
}
