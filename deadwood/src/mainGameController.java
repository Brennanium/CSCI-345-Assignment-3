import java.util.*;

import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;
import model.areas.*;
import model.areas.Set;
import model.events.*;


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
    private Button leaderboardButton;
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
        leaderboardButton.managedProperty().bind(leaderboardButton.visibleProperty());
        endTurnButton.managedProperty().bind(endTurnButton.visibleProperty());

        model.getCurrentGame().addObserverToPlayers(this);
        model.getCurrentGame().forceUpdate();
    }

    public void handleButtonAction(ActionEvent event) throws Exception {
        try {
            if(event.getSource() == actButton) {
                ActEvent e = model.act();
                showAlertForEvent(e.toString(), e.getTitle());
            } else if(event.getSource() == rehearseButton) {
                model.rehearse();
                String rehearseSuccess = "Congrats!  You earned one more practice chip.";
                showAlertForEvent(rehearseSuccess, "Rehearse Success");
            } else if(event.getSource() == leaderboardButton) {     
                Parent root = FXMLLoader.load(getClass().getResource("leaderboardView.fxml"));;
                Stage stage = new Stage();
                Scene scene = new Scene(root, 400, 300);
                stage.setTitle("Leaderboard");
                stage.setScene(scene);
                stage.show();
            } else if(event.getSource() == endTurnButton) {
                model.end()
                    .forEach(e -> showAlertForEvent(e.toString(), e.getTitle()));
            }
        } catch(InvalidActionException e) {
            showAlertForException(e);
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

            showAlertForException(e);
        }
    } 

    public void handleWorkAction(String roleName){
        try {
            model.takeRole(roleName);
        } catch(InvalidActionException e) {
            showAlertForException(e);
        }
    } 

    public void handleUpgradeAction(int rank){
        try {
            UpgradeEvent event = model.upgrade(rank);
            showAlertForEvent(event.toString(), event.getTitle());
        } catch(InvalidActionException e) {
            showAlertForException(e);
        }
    } 

    public void handleLeaderBoardAction(){
        
    }

    private void showAlertForException(InvalidActionException e) {
        Alert a = new Alert(AlertType.NONE, e.getReason(), ButtonType.OK);
        a.setTitle("Invalid Action");
        a.setHeaderText("Invalid Action");

        Window window = endTurnButton.getScene().getWindow();
        double windowCenterX = window.getX() + (window.getWidth() / 2);
        double windowCenterY = window.getY() + (window.getHeight() / 2);

        // set a temp position
        a.setX(windowCenterX);
        a.setY(windowCenterY);

        // Since the alert doesn't have a width or height till it's shown, calculate its position after it's shown
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                a.setX(windowCenterX - (a.getWidth() / 2));
                a.setY(windowCenterY - (a.getHeight() / 2));
            }
        });

        a.show();
        System.out.println("Invalid Action: " + e.getReason());
    }

    private void showAlertForEvent(String eventString, String title) {
        Alert a = new Alert(AlertType.NONE, eventString, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(title);

        Window window = endTurnButton.getScene().getWindow();
        double windowCenterX = window.getX() + (window.getWidth() / 2);
        double windowCenterY = window.getY() + (window.getHeight() / 2);

        // set a temp position
        a.setX(windowCenterX);
        a.setY(windowCenterY);

        // Since the alert doesn't have a width or height till it's shown, calculate its position after it's shown
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                a.setX(windowCenterX - (a.getWidth() / 2));
                a.setY(windowCenterY - (a.getHeight() / 2));
            }
        });

        a.show();
        System.out.println(eventString);
    }
}