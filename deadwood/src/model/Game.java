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
     * To set the all the player to be ready for the game
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
     * To resets player turn booleans and sets the next player in turn order
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
     * To check for the end scene
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
     * To check for the end day
     * @return EndDayEvent
     */
    public EndDayEvent endDayCheck() {
        if(board.getNumberOfActiveScenes() < 2){
            return wrapDay();
        }

        return null;

    }

    /**
     * To wrap the day of the game
     * @return EndDyEvent
     */
    private EndDayEvent wrapDay() {
        returnToTrailer();
        board.dealSceneCards();
        countDay--;

        return new EndDayEvent(countDay, maxCountDay);
    }

    /**
     * To check for the end game
     * @return EndGameEvent
     */
    public EndGameEvent endGameCheck() {
        if(countDay == 0) {
            return wrapGame();
        }
        
        return null;
    }
    
    /**
     * To wrap the game
     * @return EndGameEvent
     */
    private EndGameEvent wrapGame() {

        return new EndGameEvent(players);
    }

    /**
     *To return every player to Trailer
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
     * To get the area name as a string
     * @param areaString
     * @return Area
     */
    public Area getAreaForString(String areaString) {
        return board.getAreaForString(areaString);
    }
    
    /**
     * To get the current player
     * @return Player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * To get the number of players in the game
     * @return int
     */
    public int getNumOfPlayers() {
        return players.size();
    }

    /**
     * To get all the players info
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * To get the area
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getAreas() {
        return board.getAreas();
    }

    private boolean currentPlayerHasMoved;

    /**
     * To check whether the player has moved yet
     * @return boolean
     */
    public boolean getHasMoved(){ return currentPlayerHasMoved; }

    /**
     * To set the player has moved
     */
    public void hasMoved(){ currentPlayerHasMoved = true; }

    private boolean currentPlayerHasActed;

    /**
     * To check whether the player has acted yet
     * @return boolean
     */
    public boolean getHasActed(){ return currentPlayerHasActed; }

    /**
     * To set the player has acted
     */
    public void hasActed(){ currentPlayerHasActed = true; }

    private boolean currentPlayerHasRehearsed;

    /**
     * To checl whether the player has rehearsed yet
     * @return boolean
     */
    public boolean getHasRehearsed(){ return currentPlayerHasRehearsed; }

    /**
     * To set the player has rehearsed
     */
    public void hasRehearsed(){ currentPlayerHasRehearsed = true; }

    private boolean currentPlayerHasUpgraded;

    /**
     * To check whether the player has upgraded yet
     * @return boolean
     */
    public boolean getHasUpgraded(){ return currentPlayerHasUpgraded; }

    /**
     * To set the player has upgraded
     */
    public void hasUpgraded(){ currentPlayerHasUpgraded = true; }

    private boolean currentPlayerHasTakenRole;

    /**
     * To check whether the player has taken the role yet
     * @return boolean
     */
    public boolean getHasTakenRole(){ return currentPlayerHasTakenRole; }

    /**
     * To set the player has taken their role
     */
    public void hasTakenRole(){ currentPlayerHasTakenRole = true; }

    /**
     * To set the proper starting rank, credits, and number of days
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

    /**
     * To add the observer to the player
     * @param po
     */
    public void addObserverToPlayers(PlayerObserver po) {
        addObserver(po);
        players.stream()
            .forEach(p -> p.addObserver(po));
    }

    /**
     * To update the observer
     */
    private void updateObservers() {
        for(PlayerObserver po : observers) {
            po.update(currentPlayer);
        }
    }

    /**
     * To force the update on the observer
     */
    public void forceUpdate() {
        updateObservers();
    }

    /**
     * To add the observer
     * @param po
     */
    public void addObserver(PlayerObserver po) {
        observers.add(po);
    }

    /**
     * To remove the observer
     * @param po
     */
    public void removeObserver(PlayerObserver po) {
        observers.remove(po);
    }
}