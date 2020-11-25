
import java.util.*;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;
import model.areas.*;
import model.areas.Set;
import model.events.UpgradeEvent;


public class mainGameController implements PlayerObserver {

    @FXML
    private HBox mainBG;
    @FXML
    private SplitMenuButton moveSplitMenuButton;
    @FXML
    private SplitMenuButton workSplitMenuButton;
    @FXML
    private Button actButton;
    @FXML
    private Button rehearseButton;
    @FXML
    private SplitMenuButton upgradeSplitMenuButton;
    @FXML
    private Button endTurnButton;

    private ActionManager model = ActionManager.getInstance();

    private ObservableList<MenuItem> moveMenuItems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> workMenuItems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> upgradeMenuItems = FXCollections.observableArrayList();

    public void initialize() throws Exception {
        Parent board;
        Parent playerInfo;
        
        board = FXMLLoader.load(getClass().getClassLoader().getResource("boardView.fxml"));
        playerInfo = FXMLLoader.load(getClass().getClassLoader().getResource("currentPlayerInfoView.fxml"));

        mainBG.getChildren().addAll(playerInfo, board);


        moveSplitMenuButton.managedProperty().bind(moveSplitMenuButton.visibleProperty());
        workSplitMenuButton.managedProperty().bind(workSplitMenuButton.visibleProperty());
        workSplitMenuButton.setVisible(false);
        actButton.managedProperty().bind(actButton.visibleProperty());
        actButton.setVisible(false);
        rehearseButton.managedProperty().bind(rehearseButton.visibleProperty());
        rehearseButton.setVisible(false);
        upgradeSplitMenuButton.managedProperty().bind(upgradeSplitMenuButton.visibleProperty());
        upgradeSplitMenuButton.setVisible(false);
        endTurnButton.managedProperty().bind(endTurnButton.visibleProperty());

        model.getCurrentGame().addObserverToPlayers(this);
        model.getCurrentGame().forceUpdate();
    }

    public void handleButtonAction(ActionEvent event) {
        try {
            if(event.getSource() == actButton) {
                model.act();
            } else if(event.getSource() == rehearseButton) {
                model.rehearse();
            } else if(event.getSource() == endTurnButton) {
                model.end();
            }
        } catch(InvalidActionException e) {
            //it's real easy to just popup from anywhere
            /* Popup up = new Popup();
            up.title("invalid action")
            up.body(e.getReason() */

            //more complicated case?
            /* stage = (Stage) endTurnButton.getScene().getWindow(); */

            System.out.println("Invalid Action: " + e.getReason());
        }
    }

    public void update(Player player) {
        if(player == model.getCurrentPlayer()){ 
            moveSplitMenuButton.setVisible(true);
            upgradeSplitMenuButton.setVisible(false);
            actButton.setVisible(false);
            workSplitMenuButton.setVisible(false);
            rehearseButton.setVisible(false);
            
            if(player.getCurrentArea() instanceof CastingOffice) {
                upgradeSplitMenuButton.setVisible(true);
            } else if(player.getCurrentArea() instanceof Set) {
                if(player.getRole() != null){
                    moveSplitMenuButton.setVisible(false);
                    workSplitMenuButton.setVisible(false);
                    rehearseButton.setVisible(true);
                    actButton.setVisible(true);
                } else {
                    workSplitMenuButton.setVisible(true);
                    rehearseButton.setVisible(false);
                    actButton.setVisible(false);
                }
            }

            
            if(moveSplitMenuButton.isVisible()) {
                updateMoveSplitMenuButton(player);
            }
            if(workSplitMenuButton.isVisible()) {
                updateWorkButton(player);
            }
            if(upgradeSplitMenuButton.isVisible()) {
                updateUpgradeButton(player);
            }
        }
    }

    private void updateMoveSplitMenuButton(Player player) {
        //loop through and add neighbors     

        //moveSplitMenuButton.getItems().clear();
        moveMenuItems.clear();
        for(Area b : player.getCurrentArea().getNeighbors()) {
            MenuItem m = new MenuItem(b.getName());
            m.setOnAction((e) -> handleMoveAction(b.getName()));
            moveMenuItems.add(m);
        }
        moveSplitMenuButton.getItems().clear();
        moveSplitMenuButton.getItems().addAll(moveMenuItems);
    }

    private void updateWorkButton(Player player) {
        //loop through and add neighbors     
        
        //moveSplitMenuButton.getItems().clear();
        workMenuItems.clear();

        if(player.getCurrentArea() instanceof Set){
            Set set = (Set)player.getCurrentArea();
            if(set.hasActiveScene()){
                for(Role b : set.getRoles()) {
                    if(set.isRoleFree(b) && b.checkRank(player)){
                        MenuItem m = new MenuItem(b.getRoleName());
                        m.setOnAction((e) -> handleWorkAction(b.getRoleName()));
                        workMenuItems.add(m);
                    }
                }
                workSplitMenuButton.getItems().clear();
                workSplitMenuButton.getItems().addAll(workMenuItems);
            }
        }
    }

    private void updateUpgradeButton(Player player) {
        //loop through and add neighbors     
        
        //moveSplitMenuButton.getItems().clear();
        upgradeMenuItems.clear();
        if(player.getCurrentArea() instanceof CastingOffice){
            CastingOffice office = (CastingOffice)player.getCurrentArea();

            for(Integer i : office.affordablePlayerRanks(player)) {
                MenuItem m = new MenuItem(office.getUpgradeStringForRank(i));
                m.setOnAction((e) -> handleUpgradeAction(i));
                upgradeMenuItems.add(m);
            }
            upgradeSplitMenuButton.getItems().clear();
            upgradeSplitMenuButton.getItems().addAll(upgradeMenuItems); 
        }
    }

    public void handleMoveAction(String areaName){
        try {
            model.move(areaName);
        } catch(InvalidActionException e) {
            System.out.println("Invalid Action: " + e.getReason());
        }
    } 

    public void handleWorkAction(String roleName){
        try {
            model.takeRole(roleName);
        } catch(InvalidActionException e) {
            System.out.println("Invalid Action: " + e.getReason());
        }
    } 

    public void handleUpgradeAction(int rank){
        try {
            model.upgrade(rank);
        } catch(InvalidActionException e) {
            System.out.println("Invalid Action: " + e.getReason());
        }
    } 
}