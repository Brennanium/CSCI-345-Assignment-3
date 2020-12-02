import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.*;
import model.*;
import model.areas.*;
import model.areas.Set;
import model.events.*;

public class MainGameController implements PlayerObserver {

    @FXML
    public Group mainBG;
    @FXML
    public SplitMenuButton moveSplitMenuButton;
    @FXML
    public SplitMenuButton workSplitMenuButton;
    @FXML
    public Button actButton;
    @FXML
    public Button rehearseButton;
    @FXML
    public SplitMenuButton upgradeSplitMenuButton;
    @FXML
    public Button leaderboardButton;
    @FXML
    public Button endTurnButton;
    @FXML
    public Button quitButton;

    public Node board;
    public Group boardGroup;
    public BoardViewController boardContoller;

    private ActionManager model = ActionManager.getInstance();

    private ObservableList<MenuItem> moveMenuItems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> workMenuItems = FXCollections.observableArrayList();
    private ObservableList<MenuItem> upgradeMenuItems = FXCollections.observableArrayList();

    public Stage leaderboardWindow;

    public void initialize() throws Exception {
        Parent playerInfo;
        
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("boardView.fxml"));
        board = loader.load();
        boardContoller = loader.getController();

        boardGroup = new Group(board);
        playerInfo = FXMLLoader.load(getClass().getClassLoader().getResource("currentPlayerInfoView.fxml"));

        mainBG.getChildren().addAll(playerInfo, boardGroup);
        boardGroup.setLayoutX(200);
        boardGroup.setLayoutY(0);
        playerInfo.setLayoutX(0);
        playerInfo.setLayoutY(0);

        moveSplitMenuButton.managedProperty().bind(moveSplitMenuButton.visibleProperty());
        moveSplitMenuButton.setOnAction((e) -> moveSplitMenuButton.show());
        workSplitMenuButton.managedProperty().bind(workSplitMenuButton.visibleProperty());
        workSplitMenuButton.setOnAction((e) -> workSplitMenuButton.show());
        workSplitMenuButton.setVisible(false);
        actButton.managedProperty().bind(actButton.visibleProperty());
        actButton.setVisible(false);
        rehearseButton.managedProperty().bind(rehearseButton.visibleProperty());
        rehearseButton.setVisible(false);
        upgradeSplitMenuButton.managedProperty().bind(upgradeSplitMenuButton.visibleProperty());
        upgradeSplitMenuButton.setOnAction((e) -> upgradeSplitMenuButton.show());
        upgradeSplitMenuButton.setVisible(false);
        leaderboardButton.managedProperty().bind(leaderboardButton.visibleProperty());
        endTurnButton.managedProperty().bind(endTurnButton.visibleProperty());
        quitButton.managedProperty().bind(quitButton.visibleProperty());
        quitButton.setVisible(false);

        model.getCurrentGame().addCurrentPlayerObserver(this);
        model.getCurrentGame().forcePlayerUpdate();
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
                handleLeaderboardButton();
            } else if(event.getSource() == endTurnButton) {
                handleEndTurnButton();
            } else if(event.getSource() == quitButton) {
                leaderboardWindow.close();
                Stage stage = (Stage) quitButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("startScreenView.fxml"));
                stage.setTitle("Deadwood");
                stage.setScene(new Scene(root, 1000, 700));
                stage.show();
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
                    if(((Set)player.getCurrentArea()).hasActiveScene()) {
                        workSplitMenuButton.setVisible(true);
                    }
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

        a.showAndWait();
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

        a.showAndWait();
        System.out.println(eventString);
    }

    private void handleLeaderboardButton() throws Exception {
        if(leaderboardWindow == null) {
            Parent root = FXMLLoader.load(getClass().getResource("leaderboardView.fxml"));;
            leaderboardWindow = new Stage();
            Scene scene = new Scene(root, 400, 300);
            leaderboardWindow.setTitle("Leaderboard");
            leaderboardWindow.setScene(scene);
            leaderboardWindow.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> {
                leaderboardWindow.hide();
                e.consume();
            });
            leaderboardWindow.show();
        } else if(leaderboardWindow.isShowing()){
            leaderboardWindow.hide();
        } else {
            leaderboardWindow.show();
        }
    }

    private void handleEndTurnButton(){
        for(Event e : model.end()){
            showAlertForEvent(e.toString(), e.getTitle());
            if(e instanceof EndDayEvent) {
                Stage stage = (Stage) mainBG.getScene().getWindow();
                stage.setTitle("Deadwood: " + model.getDayString());
            } else if (e instanceof EndGameEvent) {
                moveSplitMenuButton.setVisible(false);
                workSplitMenuButton.setVisible(false);
                upgradeSplitMenuButton.setVisible(false);
                actButton.setVisible(false);
                rehearseButton.setVisible(false);
                endTurnButton.setVisible(false);
                
                quitButton.setVisible(true);
                leaderboardButton.setVisible(true);
            }
            
        }
    }
}