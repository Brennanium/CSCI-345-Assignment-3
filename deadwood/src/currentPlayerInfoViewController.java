
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;

public class currentPlayerInfoViewController {

    @FXML
    private ImageView currentPlayerDiceImage;
    @FXML
    private Label currentPlayerNameLabel;
    @FXML
    private Label currentPlayerMoneyLabel;
    @FXML
    private Label currentPlayerCreditsLabel;
    @FXML
    private Label currentPlayerSuccessfullScenesLabel;
    @FXML
    private Label currentPlayerRankLabel;
    @FXML
    private Label currentPlayerCurrentAreaLabel;
    @FXML
    private Label currentPlayerCurrentRoleLabel;
    @FXML
    private Label currentPlayerPracticeChipsLabel;
    
    public void initialize() {

        Image diceImage = new Image(getClass().getResourceAsStream("resources/r2.png"));
        currentPlayerDiceImage.setImage(diceImage);
    }
}
