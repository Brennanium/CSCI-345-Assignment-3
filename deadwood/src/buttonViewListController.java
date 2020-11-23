import javafx.fxml.*;
import javafx.scene.control.*;

public class buttonViewListController {
    
    @FXML
    private Button moveButton;
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
    
    public void initialize(){
        Button move = new Button("Move");
        Button work = new Button("Work");
        Button act = new Button("Act");
        Button rehearse = new Button("Rehearse");
        Button upgrade = new Button("Upgrade");
        Button endTurn = new Button("End Turn");
    }

}