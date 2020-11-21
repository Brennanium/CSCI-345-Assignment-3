
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;

public class Controller {

    @FXML
    private Label dayLabel;
    @FXML
    private ImageView boardImageView;
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
        int day = 1;
        dayLabel.setText("Day " + day);
        
        Image boardImage = new Image(getClass().getResourceAsStream("resources/board.jpg"));
        boardImageView.setImage(boardImage);

        Image diceImage = new Image(getClass().getResourceAsStream("resources/r2.png"));
        currentPlayerDiceImage.setImage(diceImage);
    }
}
