
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import model.ActionManager;
import model.*;

public class CurrentPlayerInfoViewController implements PlayerObserver {

    @FXML
    private ImageView currentPlayerDiceImageView;
    @FXML
    private Label currentPlayerNameLabel;
    @FXML
    private Label currentPlayerMoneyLabel;
    @FXML
    private Label currentPlayerCreditsLabel;
    @FXML
    private Label currentPlayerSuccessfulScenesLabel;
    @FXML
    private Label currentPlayerRankLabel;
    @FXML
    private Label currentPlayerCurrentAreaLabel;
    @FXML
    private Label currentPlayerCurrentRoleLabel;
    @FXML
    private Label currentPlayerPracticeChipsLabel;

    private Image diceImage;

    private ActionManager model = ActionManager.getInstance();

    /**
     * To initialize the current player information
     */
    public void initialize() {

        diceImage = new Image(getClass().getResourceAsStream("resources/dice/" + model.getCurrentPlayer().getImageString()));
        currentPlayerDiceImageView.setImage(diceImage);

        model.getCurrentGame().addCurrentPlayerObserver(this);

        currentPlayerCurrentRoleLabel.managedProperty().bind(currentPlayerCurrentRoleLabel.visibleProperty());
        currentPlayerCurrentRoleLabel.setVisible(false);
        currentPlayerSuccessfulScenesLabel.managedProperty().bind(currentPlayerSuccessfulScenesLabel.visibleProperty());
        currentPlayerSuccessfulScenesLabel.setVisible(false);
        currentPlayerPracticeChipsLabel.managedProperty().bind(currentPlayerPracticeChipsLabel.visibleProperty());
        currentPlayerPracticeChipsLabel.setVisible(false);

        update(model.getCurrentPlayer());
    }

    /**
     * To update all the information about the current player
     * @param player
     */
    public void update(Player player) {
        if(player == model.getCurrentPlayer()){
            diceImage = new Image(getClass().getResourceAsStream("resources/dice/" + model.getCurrentPlayer().getImageString()));
            currentPlayerDiceImageView.setImage(diceImage);
            
            currentPlayerCurrentRoleLabel.setVisible(false);
            currentPlayerPracticeChipsLabel.setVisible(false);
            currentPlayerSuccessfulScenesLabel.setVisible(false);

            if(player.getRole() != null){
                currentPlayerCurrentRoleLabel.setVisible(true);
                currentPlayerCurrentRoleLabel.setText(
                    String.format("Role: %s", player.getRole().getRoleName()));
            }
            if(player.getPracticeChips() != 0){
                currentPlayerPracticeChipsLabel.setVisible(true);
                currentPlayerPracticeChipsLabel.setText(
                    String.format("Number of Practice Chips: %d", player.getPracticeChips()));
            }
            if(player.getSuccessfulScenes() != 0) {
                currentPlayerSuccessfulScenesLabel.setVisible(true);
                currentPlayerSuccessfulScenesLabel.setText(
                    String.format("Successful Scenes: %d", player.getSuccessfulScenes()));
            }
            currentPlayerCurrentAreaLabel.setText("Area: " + player.getCurrentArea());
            currentPlayerNameLabel.setText("Name: " + player.getName());
            currentPlayerMoneyLabel.setText("Money: " + player.getDollars());
            currentPlayerCreditsLabel.setText("Credits: " + player.getCredits());
            currentPlayerRankLabel.setText("Rank: " + player.getRank());
        }
    }
}
