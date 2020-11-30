package model.areas;

import java.util.*;
import java.util.stream.*;

import model.*;
import model.events.*;
import javafx.scene.shape.*;

public class Set extends Area{
    private Role[] offCardRoles;
    private int maxShotTokenCount;
    private int shotTokenCount;
    private Rectangle[] shotTokenLocations;
    private SceneCard scene;
    private Rectangle sceneCardLocation;


    /**
     * Constructor
     * @param name
     * @param maxShotTokenCount
     * @param roles
     */
    public Set(String name, int maxShotTokenCount, Polygon polygon, Role[] roles, Rectangle sceneCardLocation, Rectangle[] shotTokenLocations) {
        super(name, polygon);
        
        this.maxShotTokenCount = maxShotTokenCount;
        this.shotTokenCount = maxShotTokenCount;
        this.sceneCardLocation = sceneCardLocation;
        this.shotTokenLocations = shotTokenLocations;
        offCardRoles = roles;
    }

    /**
     * To get the number of remianing shot tokens
     * @return int
     */
    public int getShotTokenCount() {
        return shotTokenCount;
    }

    /**
     * To get the maximum number of shot tokens
     * @return int
     */
    public int getMaxShotTokenCount() {
        return maxShotTokenCount;
    }

    /**
     * To get the off card roles
     * @return Role[]
     */
    public Role[] getOffCardRoles(){
        return offCardRoles;
    }

    /**
     * To remove the shot token
     */
    public void removeShotToken(){
        shotTokenCount--;
    }

    public Rectangle[] getShotTokenLocations() {
        return shotTokenLocations;
    }
    /**
     * To get the budget of the scene
     * @return int
     */
    public int getBudget() {
        return scene.getBudget();
    }

    /**
     * To set the scene card
     * @param scene
     */
    public void setSceneCard(SceneCard scene){
        this.scene = scene;
    }
   
    /**
     * To get the role name as a string
     * @param roleString
     * @return Role
     */
    public Role getRoleForString(String roleString) { 
     for(int i = 0; i < offCardRoles.length; i++){
        if(offCardRoles[i].getRoleName().equalsIgnoreCase(roleString)){
              return offCardRoles[i];
        }
     }

     return scene.getRoleForString(roleString);
   }

    /**
     * To get all the roles that we have
     * @return ArrayList<Role>
     */
    public ArrayList<Role> getRoles() {
        ArrayList<Role> list = new ArrayList<Role>();
        Collections.addAll(list, offCardRoles);
        if(scene != null) 
            Collections.addAll(list, scene.getOnCardRoles());
        return list;
    }

    /**
     * To get the scene card location
     * @return Rectangle
     */
    public Rectangle getSceneCardLocation(){
        return sceneCardLocation;
    }

    /**
     * To get the current scene card image string
     * @return String
     */
    public String getSceneCardImageString(){
        if(hasActiveScene()){
            return scene.getImageString();
        } else {
            return null;   
        }
    }
    
    /**
     * To check whether the role is free or not
     * @param role
     * @return boolean
     */
   public boolean isRoleFree(Role role){
        for(int i = 0; i < occupants.size(); i++){
            if(occupants.get(i).getRole() == role){
                return false;
            }
        }

        return true;
    }

    /**
     * To check whether the scene is active or not
     * @return boolean
     */
    public boolean hasActiveScene(){
        return scene.getIsActive();
    }

    /**
     * To check whether the scene is wrapped or not
     * @return EndSceneEvent
     */
    public EndSceneEvent checkWrapScene(){
        if(scene.getIsActive() && shotTokenCount == 0) {
            return wrapScene();
        } else { 
            return null;
        }
    }
    
    /**
     * To wrap the scene
     * @return EndSceneEvent
     */
    private EndSceneEvent wrapScene(){
        ArrayList<Player> offCard = getPlayersWorkingOffCard();
        ArrayList<Player> onCard = getPlayersWorkingOnCard();
        HashMap<Player,Integer> bonusForPlayer = new HashMap<Player,Integer>();

        if(onCard.size() > 0) {
            ArrayList<Integer> dice = new ArrayList<Integer>();
            for(int i = 0; i < scene.getBudget(); i++) {
                dice.add(rollDie());
            } 
            Collections.sort(dice, new Comparator<Integer>(){
                public int compare(Integer i1, Integer i2){
                    return i2 - i1;
                }
            });
            Collections.sort(onCard, new Comparator<Player>(){
                public int compare(Player p1, Player p2){
                    return p2.getRole().getRank() - p1.getRole().getRank();
                }
            });

            //pay on card roles
            Player player;
            int bonus;
            for(int i = 0; i < dice.size(); i++) {
                player = onCard.get(i % onCard.size());
                bonus = dice.get(i);
                player.pay(bonus, 0);
                if(bonusForPlayer.get(player) == null) {
                    bonusForPlayer.put(player, bonus);
                } else {
                    int currentBonus = bonusForPlayer.get(player);
                    bonusForPlayer.replace(player, currentBonus + bonus);
                }
            } 

            //pay off card roles if there are any
            if(offCard.size() > 0) {
                offCard.forEach(p -> {
                    int bns = p.getRole().getRank();
                    p.pay(bns, 0);
                    bonusForPlayer.put(p, bns);
                });
            }
        }

        //remove everyone's roles
        occupants.forEach(p -> {
            p.setRole(null);
            
            //remove for testing purposes to speed up game
            //p.resetPracticeChips();
        });
        
        //kill the scene
        scene.wrapScene();

        offCard.addAll(onCard);
        offCard.forEach(p -> p.wrapScene());

        return new EndSceneEvent(offCard, bonusForPlayer, this, scene);
    }


    /**
     * To reset the shot token
     */
    public void reset(){
        shotTokenCount = maxShotTokenCount;
    }

    /**
     * To get all the players that are working on card role
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayersWorkingOnCard() {
        List<Player> working = occupants.stream()
            .filter(p -> p.getRole() != null && p.getRole().checkOnCard())
            .collect(Collectors.toList());
                    
        return new ArrayList<Player>(working);
    }
    
    /**
     * To get all the players that are working on the off card role
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayersWorkingOffCard() {
        List<Player> working = occupants.stream()
            .filter(p -> p.getRole() != null && !p.getRole().checkOnCard())
            .collect(Collectors.toList());
                    
        return new ArrayList<Player>(working);
    }
    
    /**
     * To get the area summary in a string format
     * @return String
     */
    public String getAreaSummary() {
        StringBuffer sb  = new StringBuffer();
        if(scene != null && scene.getIsActive()){
            sb.append(String.format(
                "in %s shooting %s.%n", getName(), scene.toString()));
        } else {
            sb.append(String.format(
                "in %s not shooting anything at the moment.%n", getName()));
        }
        sb.append("Neighboring areas: \n");
        getNeighbors().forEach(b -> sb.append("  " + b.toString() + "\n"));
        return sb.toString();
    }
    
    /**
     * To get the roles information in a string format
     * @return String
     */
    public String getRolesInfo(){
        StringBuffer sb = new StringBuffer();
        if(scene != null && scene.getIsActive() == true){
            for(Role r : offCardRoles) {
                sb.append(
                    (isRoleFree(r) ? "" : "(TAKEN): ") + 
                    r.toString() + "\n");
            }
            for(Role r : scene.getOnCardRoles()) {
                sb.append(
                    (isRoleFree(r) ? "" : "(TAKEN): ") + 
                    r.toString()+ "\n");
            }
        } else {
            sb.append(String.format
                ("There is no active scene card in %s.%n", getName()));
        }
        return sb.toString();
    }

    /**
     * To get the scene information in a string format
     * @return
     */
    public String getSceneInfo(){ 
        String str =  "";
        if(scene != null && scene.getIsActive() == true){
            str = String.format
                ("Area Name: %s %nScene Name: %s %nScene Budget: %d %nShot Token: %d %n", 
                getName(), //area name
                scene.getSceneName(), 
                scene.getBudget(), 
                shotTokenCount);
            str += getRolesInfo();
        } else {
            str= String.format
                ("There is no active scene card in %s.%n", getName());
        }
        return str;
    }

    /**
     * To roll the die
     * @return int
     */
    private int rollDie() {
        int min = 1;
        int max = 6;
        return (int) (Math.random() * (max - min + 1) + min);
    }
}