
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
import model.areas.CastingOffice;



public class mainGameController implements PlayerObserver {

    @FXML
    private HBox mainBG;

    @FXML
    private SplitMenuButton moveSplitMenuButton;
    @FXML
    private Button workButton;
    @FXML
    private Button actButton;
    @FXML
    private Button rehearseButton;
    @FXML
    private Button upgradeButton;
    @FXML
    private Button endTurnButton;

    private ActionManager model = ActionManager.getInstance();

    public void initialize() throws Exception {
        Parent board;
        Parent playerInfo;
        
        board = FXMLLoader.load(getClass().getClassLoader().getResource("boardView.fxml"));
        playerInfo = FXMLLoader.load(getClass().getClassLoader().getResource("currentPlayerInfoView.fxml"));

        mainBG.getChildren().addAll(playerInfo, board);


        moveSplitMenuButton.managedProperty().bind(moveSplitMenuButton.visibleProperty());
        workButton.managedProperty().bind(workButton.visibleProperty());
        workButton.setVisible(false);
        actButton.managedProperty().bind(actButton.visibleProperty());
        actButton.setVisible(false);
        rehearseButton.managedProperty().bind(rehearseButton.visibleProperty());
        rehearseButton.setVisible(false);
        upgradeButton.managedProperty().bind(upgradeButton.visibleProperty());
        upgradeButton.setVisible(false);
        endTurnButton.managedProperty().bind(endTurnButton.visibleProperty());
    }


    public void handleButtonAction(ActionEvent event) {
        try {
            if(event.getSource() == moveSplitMenuButton) {
                
            } else if(event.getSource() == workButton) {
                
            } else if(event.getSource() == actButton) {
                
            } else if(event.getSource() == rehearseButton) {
                model.rehearse();
            } else if(event.getSource() == upgradeButton) {
                
            } else if(event.getSource() == endTurnButton) {
                model.end();
            }
        } catch(InvalidActionException e) {
            System.out.println("Invalid Action: " + e.getReason());
        }
    }


    public void update(Player player) {
        if(player == model.getCurrentPlayer()){ 
            if(player.getCurrentArea() instanceof CastingOffice) {
                upgradeButton.setVisible(true);
            }
            if(player.getRole() != null){
                moveSplitMenuButton.setVisible(false);
                workButton.setVisible(false);
                upgradeButton.setVisible(false);
            } else {
                moveSplitMenuButton.setVisible(true);
                workButton.setVisible(true);
            }



            if(moveSplitMenuButton.isVisible()) {
                //loop through and add neighbors
            }
        }
    }

}