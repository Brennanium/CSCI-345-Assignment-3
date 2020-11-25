package model;

import java.util.*;
import java.util.stream.*;

import model.areas.*;
import model.areas.Set;
import model.events.*;


public class Game {
    private ArrayList<Player> players;
    private ArrayList<Player> playersInTurnOrder;
    private Player currentPlayer;
    private Board board;
    private int maxCountDay;
    private int countDay;

    HashSet<PlayerObserver> observers = new HashSet<PlayerObserver>();

    /**
     * Constructor
     * 
     */
    public Game() {
        board = Board.getInstance();
        board.dealSceneCards();
    }

    /**
     * Constructor
     * 
     * @param players
     */
    public Game(ArrayList<Player> players) {
        this.players = players;

        List<Player> playersCopy = players.stream()
            .map(p -> p)
            .collect(Collectors.toList());
        playersInTurnOrder = new ArrayList<Player>(playersCopy);
        Collections.shuffle(playersInTurnOrder);

        board = Board.getInstance();
        board.dealSceneCards();

        initPlayers();
    }

    /**
     * 
     * @param players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;

        List<Player> playersCopy = players.stream()
            .map(p -> p)
            .collect(Collectors.toList());
        playersInTurnOrder = new ArrayList<Player>(playersCopy);
        Collections.shuffle(playersInTurnOrder);
        
        initPlayers();
    }

    /**
     * resets player turn booleans and sets the next player in turn order
     */
    public void setNextPlayer() {
        if(currentPlayer == null) {
            currentPlayer = playersInTurnOrder.get(0);
            return;
        } 
        int currentIndex = playersInTurnOrder.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();

        currentPlayer = playersInTurnOrder.get(nextIndex);

        currentPlayerHasActed = false;
        currentPlayerHasMoved = false;
        currentPlayerHasRehearsed = false;
        currentPlayerHasUpgraded = false;
        currentPlayerHasTakenRole = false;

        updateObservers();
    }

    /**
     * 
     * 
     * @return EndSceneEvent
     */
    public EndSceneEvent endSceneCheck() {
        if(currentPlayer.getCurrentArea() instanceof Set) {
            Set currentArea = (Set)currentPlayer.getCurrentArea();

            return currentArea.checkWrapScene();
        }
        return null;
    }

    /**
     * 
     * @return EndDayEvent
     */
    public EndDayEvent endDayCheck() {
        if(board.getNumberOfActiveScenes() < 2){
            return wrapDay();
        }

        return null;

    }

    /**
     * 
     * @return EndDyEvent
     */
    private EndDayEvent wrapDay() {
        returnToTrailer();
        board.dealSceneCards();
        countDay--;

        return new EndDayEvent(countDay, maxCountDay);
    }

    /**
     * 
     * @return EndGameEvent
     */
    public EndGameEvent endGameCheck() {
        if(countDay == 0) {
            return wrapGame();
        }
        
        return null;
    }
    
    /**
     * 
     * @return EndGameEvent
     */
    private EndGameEvent wrapGame() {

        return new EndGameEvent(players);
    }

    /**
     * returns every player to Trailer
     */
    public void returnToTrailer(){
        players.stream()
            .forEach(p -> {
                p.setRole(null);
                p.getCurrentArea().removePlayer(p);
                Area trailers = getAreaForString("trailer");
                trailers.addPlayer(p);
                p.setArea(trailers);
            });
    }
    
    /**
     * 
     * @param areaString
     * @return Area
     */
    public Area getAreaForString(String areaString) {
        return board.getAreaForString(areaString);
    }
    
    /**
     * 
     * @return Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * 
     * @return int
     */
    public int getNumOfPlayers() {
        return players.size();
    }

    /**
     * 
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * 
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getAreas() {
        return board.getAreas();
    }


    private boolean currentPlayerHasMoved;
    /**
     * 
     * @return boolean
     */
    public boolean getHasMoved(){ return currentPlayerHasMoved; }
    /**
     * 
     */
    public void hasMoved(){ currentPlayerHasMoved = true; }
    private boolean currentPlayerHasActed;
    /**
     * 
     * @return boolean
     */
    public boolean getHasActed(){ return currentPlayerHasActed; }
    /**
     * 
     */
    public void hasActed(){ currentPlayerHasActed = true; }
    private boolean currentPlayerHasRehearsed;
    /**
     * 
     * @return boolean
     */
    public boolean getHasRehearsed(){ return currentPlayerHasRehearsed; }
    /**
     * 
     */
    public void hasRehearsed(){ currentPlayerHasRehearsed = true; }
    private boolean currentPlayerHasUpgraded;
    /**
     * 
     * @return boolean
     */
    public boolean getHasUpgraded(){ return currentPlayerHasUpgraded; }
    /**
     * 
     */
    public void hasUpgraded(){ currentPlayerHasUpgraded = true; }
    private boolean currentPlayerHasTakenRole;
    /**
     * 
     * @return boolean
     */
    public boolean getHasTakenRole(){ return currentPlayerHasTakenRole; }
    /**
     * 
     */
    public void hasTakenRole(){ currentPlayerHasTakenRole = true; }

    /**
     * sets the proper starting rank, credits, and number of days
     */
    public void initPlayers(){
        int startingRank;
        int startingCredits;
        switch(players.size()){
            case 4:
                startingRank = 1;
                startingCredits = 0;
                maxCountDay = 4;
                break;
            case 5:
                startingRank = 1;
                startingCredits = 2;
                maxCountDay = 4;
                break;
            case 6:
                startingRank = 1;
                startingCredits = 4;
                maxCountDay = 4;
                break;
            case 7:
            case 8:
                startingRank = 2;
                startingCredits = 0;
                maxCountDay = 4;
                break;
            default: 
                startingRank = 1;
                startingCredits = 0;
                maxCountDay = 3;
                break;
        }

        countDay = maxCountDay;

        players.stream()
            .forEach(p -> {
                Area trailers = getAreaForString("trailer");
                trailers.addPlayer(p);
                p.setArea(trailers);
                
                p.setRank(startingRank);
                p.pay(0, startingCredits);
            });
        
        setNextPlayer();
    }

    public void addObserverToPlayers(PlayerObserver po) {
        addObserver(po);
        players.stream()
            .forEach(p -> p.addObserver(po));
    }

    private void updateObservers() {
        for(PlayerObserver po : observers) {
            po.update(currentPlayer);
        }
    }

    public void forceUpdate() {
        updateObservers();
    }

    public void addObserver(PlayerObserver po) {
        observers.add(po);
    }

    public void removeObserver(PlayerObserver po) {
        observers.remove(po);
    }
}