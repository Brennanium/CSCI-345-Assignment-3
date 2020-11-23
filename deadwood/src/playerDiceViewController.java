import javafx.fxml.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;

public class playerDiceViewController {
    
    @FXML
    private ImageView playerDice;

    public void initialize() {
        Image diceImage = new Image(getClass().getResourceAsStream("resources/r2.png"));
        playerDice.setImage(diceImage);
    }
}
