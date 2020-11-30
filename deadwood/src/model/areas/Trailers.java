package model.areas;

import javafx.scene.shape.Polygon;

public class Trailers extends Area {

    /**
     * Constructor
     * @param polygon
     */
    public Trailers(Polygon polygon) {
        super("trailer", polygon);
    }

    /**
     * To get the area summary
     * @return String
     */
    public String getAreaSummary() {
        StringBuffer sb = new StringBuffer("in Trailers\n");

        sb.append("Neighboring areas: \n");
        getNeighbors().forEach(b -> sb.append("  " + b.toString() + "\n"));
        return sb.toString();
    }

    /**
     * To format the name of Trailers
     * @return String
     */
    public String toString() {
        return "Trailers";
    }
}
