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
     * 
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
     * 
     * @return String
     */
    public String getSceneName() {
        return sceneName;
    }

    /**
     * 
     * @return int
     */
    public int getsceneNumber(){
        return sceneNumber;
    }

    /**
     * 
     * @return String
     */
    public String getDescr(){
        return sceneDescr;
    }

    /**
     * 
     * @return int
     */
    public int getBudget() {
        return sceneBudget;
    }

    /**
     *  
     * @return String
     */
    public String getImageString(){
        return image;
    }

    /**
     * 
     * @return boolean
     */
    public boolean getIsActive() {
        return isActive;
    }

    /**
     * 
     * @return Role[]
     */
    public Role[] getOnCardRoles(){
      return onCardRoles;
    }
    
    /**
     * 
     * @return int
     */
    public int getOnCardRolesCount(){
        return onCardRoles.length;
    }
      
    /**
     * 
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
     * 
     */
    public void dealScene() {
        isActive = true;
    }
    
    /**
     * 
     */
    public void wrapScene() {
        isActive = false;
    }

    /**
     * 
     * @return String
     */
    public String toString(){
        return sceneName + " scene " + sceneNumber;
    }
}