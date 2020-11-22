
import javafx.fxml.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;

public class boardViewController {

    @FXML
    private ImageView boardImageView;
    @FXML
    private ImageView currentPlayerDiceImage;
    
    public void initialize() {
        
        Image boardImage = new Image(getClass().getResourceAsStream("resources/board.jpg"));
        boardImageView.setImage(boardImage);

        Image diceImage = new Image(getClass().getResourceAsStream("resources/r2.png"));
        currentPlayerDiceImage.setImage(diceImage);
        currentPlayerDiceImage.setLayoutX(1500);
        currentPlayerDiceImage.setLayoutY(1500);
    }
}
