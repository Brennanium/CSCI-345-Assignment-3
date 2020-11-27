package model;

public class SceneCard {
    private String sceneName;
    private int sceneBudget;
    private Role[] onCardRoles;
    private boolean isActive;
    private int sceneNumber;
    private String sceneDescr;
    private String image;

    /**
     * Constructor
     * @param name
     * @param number
     * @param budget
     * @param descr
     * @param roles
     * @param img
     */
    public SceneCard(String name, int number, int budget, String descr, Role[] roles, String img){
        sceneName = name;
        sceneNumber = number;
        sceneBudget = budget;
        sceneDescr = descr;
        onCardRoles = roles;
        image = img;
    }

    // getters
    /**
     * To get the scene name
     * @return String
     */
    public String getSceneName() {
        return sceneName;
    }

    /**
     * To get the scene number
     * @return int
     */
    public int getsceneNumber(){
        return sceneNumber;
    }

    /**
     * To get the description of the scene
     * @return String
     */
    public String getDescr(){
        return sceneDescr;
    }

    /**
     * To get the budget of the scene
     * @return int
     */
    public int getBudget() {
        return sceneBudget;
    }

    /**
     * To get the image of the scene card
     * @return String
     */
    public String getImageString(){
        return image;
    }

    /**
     * To check whether the card is in-used or not
     * @return boolean
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * To get all of the on card roles
     * @return Role[]
     */
    public Role[] getOnCardRoles(){
      return onCardRoles;
    }
    
    /**
     * To gt the number of on card roles
     * @return int
     */
    public int getOnCardRolesCount(){
        return onCardRoles.length;
    }
      
    /**
     * To get the role from the string
     * @param roleString
     * @return Role
     */
    public Role getRoleForString(String roleString){
        for(int i = 0; i < onCardRoles.length; i++){
            if(onCardRoles[i].getRoleName().equalsIgnoreCase(roleString)){
                  return onCardRoles[i];
            }
        }
        return null;
    }

    /**
     * To deal the scene card
     */
    public void dealScene() {
        isActive = true;
    }
    
    /**
     * To wrap the scene
     */
    public void wrapScene() {
        isActive = false;
    }

    /**
     * To format the string to printout the information of the scene
     * @return String
     */
    public String toString(){
        return sceneName + " scene " + sceneNumber;
    }
}