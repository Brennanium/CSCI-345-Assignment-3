package model;

import model.areas.*;
import model.areas.Set;
import model.events.*;

import java.util.*;

public class ActionManager {
    private static ActionManager instance = new ActionManager();
    
    private Game game;

    /**
     * Constructor
     */
    private ActionManager(){
        game = new Game();
    }

    /**
     * To set all the players to the game
     * @param players
     */
    public void setPlayers(ArrayList<Player> players) {
        game = new Game(players);
        //game.setPlayers(players);
    }

    /**
     * To start a new game
     */
    public void getNewGame(){
        game = new Game();
    }
    
    /**
     * 
     * To move the player to where ever theplayer whishes to go
     * @return Event
     * @throws InvalidActionException
     */
    public Event move(String areaString) throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        Area currentArea = currentPlayer.getCurrentArea();

        Area newArea = game.getAreaForString(areaString);

        if(newArea == currentArea){
            throw new InvalidActionException("You are already there!");
        }
        //check if role == null
        if(currentPlayer.getRole() == null){
            if(newArea != null){  
                if(!game.getHasMoved()){
                    if(currentArea.isNeighbor(newArea)){
                        currentPlayer.setArea(newArea);
                        currentArea.removePlayer(currentPlayer);
                        newArea.addPlayer(currentPlayer);
                        game.hasMoved();

                    } else {
                        throw new InvalidActionException(currentPlayer.getName() + " is not close enough to go there.");
                    }
                } else {
                    throw new InvalidActionException(currentPlayer.getName() + " has already moved.");
                }
            } else{
                throw new InvalidActionException("That area doesn't exist.");
            }
        } else{
            throw new InvalidActionException(
                currentPlayer.getName() + " is working on a role.");
        }
        return null;
    }

    /**
     * To set the player to whichever the role that he/she would like to work
     * @param roleString
     * @return Event
     * @throws InvalidActionException
     */
    public Event takeRole(String roleString) throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();

        if(!(currentPlayer.getCurrentArea() instanceof Set)) 
            throw new InvalidActionException(currentPlayer.getName() + " is not currently on an set.");
        else {
            Set currentArea = (Set)currentPlayer.getCurrentArea();
            
            Role currentRole = currentPlayer.getRole();
            Role newRole = ((Set)currentArea).getRoleForString(roleString);
            if(currentArea.hasActiveScene()){
                if(!game.getHasActed() || !game.getHasTakenRole()){
                    if(currentRole == null){
                        if(currentArea.isRoleFree(newRole)){
                            if(currentPlayer.isRoleValid(newRole)){
                                currentPlayer.setRole(newRole);
                                game.hasTakenRole();
                            } else {
                                throw new InvalidActionException(
                                    currentPlayer.getName() + " doesn't have a high enough rank to take this role.");
                            }
                        } else {
                            throw new InvalidActionException(roleString + " is not available.");
                        }
                    } else {
                        throw new InvalidActionException(currentPlayer.getName() + " is already working.");
                    }
                } else {
                    throw new InvalidActionException(currentPlayer.getName() + " has already acted or taken a role.");
                }
            } else {
                throw new InvalidActionException(currentArea.getName() + " has no active scene.");
            }
        }
        return null;
    }

    /**
     * To set the player to act on the role that they have
     * @return ActEvent
     * @throws InvalidActionException
     */
    public ActEvent act() throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        if(!(currentPlayer.getCurrentArea() instanceof Set))
            throw new InvalidActionException(currentPlayer.getName() + " is not on a set.");
        else {
            Set currentArea = (Set)currentPlayer.getCurrentArea();
            Role currentRole = currentPlayer.getRole();

            int diceRoll = rollDie();

            if(currentRole != null) {
                if(currentArea.hasActiveScene()){
                    if(!game.getHasActed()){
                        if(!game.getHasTakenRole()) {
                            boolean succeeds = (diceRoll + currentPlayer.getPracticeChips()) >= currentArea.getBudget();
                            game.hasActed();
                            game.hasRehearsed();
                            if(currentRole.checkOnCard() && succeeds){
                                currentArea.removeShotToken();
                                currentPlayer.pay(0, 2);
                                return new ActEvent(currentPlayer, true, 0, 2);
                            } else if(currentRole.checkOnCard() && !succeeds){
                                currentPlayer.pay(0, 0);
                                return new ActEvent(currentPlayer, false, 0, 0);
                            } else if(!currentRole.checkOnCard() && succeeds){
                                currentArea.removeShotToken();
                                currentPlayer.pay(1, 1);
                                return new ActEvent(currentPlayer, true, 1, 1);
                            } else if(!currentRole.checkOnCard() && !succeeds){
                                currentPlayer.pay(1, 0);
                                return new ActEvent(currentPlayer, false, 1, 0);
                            } else {
                                throw new InvalidActionException("Something went wrong....");
                            }
                        } else {
                            throw new InvalidActionException(
                                currentPlayer.getName() + " has already taken a role this turn.");
                        }
                    } else {
                        throw new InvalidActionException(
                            currentPlayer.getName() + " has already acted or rehearsed this turn.");
                    }
                } else {
                    throw new InvalidActionException(currentArea.getName() + " has no active scene.");
                }
            } else {
                throw new InvalidActionException(currentPlayer.getName() + " is not working on a role.");
            }
        }
    }

    /**
     * To set the player to rehearse on the role that they have
     * @return boolean
     * @throws InvalidActionException
     */
    public boolean rehearse() throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        if(!(currentPlayer.getCurrentArea() instanceof Set))
            throw new InvalidActionException(currentPlayer.getName() + " is not on a set.");
        else {
            Set currentArea = (Set)currentPlayer.getCurrentArea();
            Role currentRole = currentPlayer.getRole();
            
            if(currentRole != null) {
                if(currentPlayer.getPracticeChips() < currentArea.getBudget()) {
                    if(!game.getHasRehearsed()){
                        if(!game.getHasTakenRole() && !game.getHasActed()) {
                            currentPlayer.addPracticeChip();
                            game.hasRehearsed();
                            game.hasActed();
                        } else {
                            throw new InvalidActionException(
                                currentPlayer.getName() + " has already taken a role this turn.");
                        }
                    } else {
                        throw new InvalidActionException(currentPlayer.getName() + " has already rehearsed or acted this turn.");
                    }
                } else {
                    throw new InvalidActionException(currentPlayer.getName() + " has too many practice chips!");
                }
            } else {
                throw new InvalidActionException(currentPlayer.getName() + " is not working on a role.");
            }
            return true;
        }
    }

    /**
     * To upgrade the player to the new rank that they desired
     * @param desiredRank
     * @return UpgradeEvent
     * @throws InvalidActionException
     */
    public UpgradeEvent upgrade(int desiredRank) throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        if(!(currentPlayer.getCurrentArea() instanceof CastingOffice)) 
            throw new InvalidActionException(
                String.format("Player %s not in Casting Office.", currentPlayer.getName()));
        else {
            if(!game.getHasUpgraded()) {
                CastingOffice currentArea = (CastingOffice)currentPlayer.getCurrentArea();
                if(currentArea.playerCanAffordRank(currentPlayer,desiredRank)){
                    int money = currentArea.getMoneyForRank(desiredRank);
                    int credits = currentArea.getCreditsForRank(desiredRank);
                    
                    int oldRank = currentPlayer.getRank();
                    currentPlayer.buy(money,credits);
                    currentPlayer.setRank(desiredRank);  

                    game.hasUpgraded();
                    
                    return new UpgradeEvent(currentPlayer, oldRank, desiredRank);
                } else {
                    throw new InvalidActionException(
                        String.format("Player %s cannot affort rank %d.", currentPlayer.getName(), desiredRank));
                }
            } else {
                throw new InvalidActionException(
                    currentPlayer.getName() + " has already upgraded this turn.");
            }
        }
    }
    
    /**
     * To end all the events of the game
     * @return ArrayList<Event>
     */
    public ArrayList<Event> end() {
        ArrayList<Event> events = new ArrayList<Event>();
        EndSceneEvent sceneEnd = game.endSceneCheck();
        if(sceneEnd != null) 
            events.add(sceneEnd);
        EndDayEvent dayEnd = game.endDayCheck();
        if(dayEnd != null) 
            events.add(dayEnd);
        EndGameEvent gameEnd = game.endGameCheck();
        if(gameEnd != null) 
            events.add(gameEnd);

        game.setNextPlayer();  
        return events;
    }

    /**
     * To roll the player dice
     * @return int
     */
    private int rollDie() {
        int min = 1;
        int max = 6;
        return (int) (Math.random() * (max - min + 1) + min);
    }

    /**
     * To get the current player game
     * @return Player
     */
	public Player getCurrentPlayer() {
        return game.getCurrentPlayer();
    }
    
    /**
     * To get the current area
     * @return Area
     */
    public Area getCurrentArea(){
        return game.getCurrentPlayer().getCurrentArea();
    }

    /**
     * To get the areas
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getAreas(){
        return game.getAreas();
    }

    /**
     * To get the areas
     * @return ArrayList<Area>
     */
    public ArrayList<Set> getSets(){
        return game.getSets();
    }

    /**
     * To get the player roles
     * @return ArrayList<Role>
     * @throws InvalidActionException
     */
	public ArrayList<Role> getRoles() throws InvalidActionException{
        Player currentPlayer = game.getCurrentPlayer();
        Area currentArea = currentPlayer.getCurrentArea();
        if(currentArea instanceof Set) {
            //if scene is active
            return ((Set)currentArea).getRoles();
        }  else {
            throw new InvalidActionException(
                String.format("Player %s not on a Set.", currentPlayer.getName()));
        }        
	}

    /**
     * To get the player rank for the upgrade
     * @return ArrayList<String>
     * @throws InvalidActionException
     */
	public ArrayList<String> getRanks() throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        Area currentArea = currentPlayer.getCurrentArea();
        if(currentArea instanceof CastingOffice) {
            return ((CastingOffice)currentArea).getRankUpgradeStrings();
        } else {
            throw new InvalidActionException(
                String.format("Player %s not in Casting Office.", currentPlayer.getName()));
        }
	}
    
    /**
     * To get the scene of the current player if they have one
     * @return String
     * @throws InvalidActionException
     */
	public String getScene() throws InvalidActionException {
        Player currentPlayer = game.getCurrentPlayer();
        Area currentArea = currentPlayer.getCurrentArea();
        if(currentArea instanceof Set) {
           return ((Set)currentArea).getSceneInfo();
        }  else {
            throw new InvalidActionException(
                String.format("Player %s not on a Set.", currentPlayer.getName()));
        }       
	}

    /**
     * To get all the player game
     * @return ArrayList<Player>
     */
	public ArrayList<Player> getPlayers() {
        return game.getPlayers();
	}

    /**
     * To get the current role of the current player
     * @return Role
     * @throws InvalidActionException 
     */
	public Role getCurrentRole() throws InvalidActionException {
        Role role = game.getCurrentPlayer().getRole();
        if(role != null) {
            return role;
        } else {
            throw new InvalidActionException("You don't currently have a role.");
        }
    }
    
    /**
     * To get the neighbors of the current area
     * @return ArrayList<Area>
     */
    public ArrayList<Area> getCurrentNeighbors(){
        return game.getCurrentPlayer().getCurrentArea().getNeighbors();
    }

    /**
     * 
     * @return String
     */
    public String getDayString() {
        return game.getDayString();
    }
    
    /**
     * To get the current game
     * @return Game
     */
    public Game getCurrentGame(){
        return game;
    }

    /**
     * getInstance
     * @return ActionManager
     */
    public static ActionManager getInstance() {
        return instance;
    }
}
