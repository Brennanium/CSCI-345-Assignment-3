package model.areas;

public class Trailers extends Area {
    public Trailers() {
        super("trailer");
    }

    public String getAreaSummary() {
        StringBuffer sb = new StringBuffer("in Trailers\n");

        sb.append("Neighboring areas: \n");
        getNeighbors().forEach(b -> sb.append("  " + b.toString() + "\n"));
        return sb.toString();
    }

    public String toString() {
        return "Trailers";
    }
}
