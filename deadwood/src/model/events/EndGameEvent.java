package model.events;

import java.util.*;

import model.*;

public class EndGameEvent extends Event {
    private ArrayList<Player> players;

    public EndGameEvent(ArrayList<Player> players){
        this.players = players;
    }

    private void sortPlayers(){
        Collections.sort(players, new Comparator<Player>(){
            public int compare(Player p1, Player p2){
                return p2.getCurrentScore() - p1.getCurrentScore();
            }
        });
    }

    public String toString(){
        sortPlayers();

        StringBuffer sb = new StringBuffer("\n\n\n\nGame Over! \n\n Final Scores:\n");
        int i = 1;
        for(Player p : players) {
            sb.append(String.format(
                "%d. %s \t Score: %d%n", i, p.getName(), p.getCurrentScore()));
            i++;
        }
        return sb.toString();
    }
}