
import javafx.fxml.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;

public class boardViewController {

    @FXML
    private ImageView boardImageView;
    @FXML
    private ImageView currentPlayerDiceImage;
    @FXML
    private ImageView backCardTrainStation;
    @FXML
    private ImageView backCardJail;
    @FXML
    private ImageView backCardMainStreet;
    @FXML
    private ImageView backCardRanch;
    @FXML
    private ImageView backCardBank;
    @FXML
    private ImageView backCardGeneralStore;
    @FXML
    private ImageView backCardSaloon;
   /*  @FXML
    private ImageView backCardSecretHideout; */
    @FXML
    private ImageView backCardChurch;
    @FXML
    private ImageView backCardHotel;
    @FXML
    private ImageView sceneCardSecretHideout;
    @FXML
    private ImageView shot;
    
    public void initialize() {
        
        Image boardImage = new Image(getClass().getResourceAsStream("resources/board.jpg"));
        boardImageView.setImage(boardImage);
        boardImageView.minWidth(1200);
        boardImageView.minHeight(900);

        Image diceImage = new Image(getClass().getResourceAsStream("resources/r2.png"));
        currentPlayerDiceImage.setImage(diceImage);
        /* currentPlayerDiceImage.setLayoutX(1500);
        currentPlayerDiceImage.setLayoutY(1500); */

        Image backCard1 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardTrainStation.setImage(backCard1);

        Image backCard2 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardJail.setImage(backCard2);

        Image backCard3 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardMainStreet.setImage(backCard3);

        Image backCard4 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardGeneralStore.setImage(backCard4);

        Image backCard5 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardSaloon.setImage(backCard5);

        /* Image backCard6 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardSecretHideout.setImage(backCard6); */

        Image backCard7 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardChurch.setImage(backCard7);

        Image backCard8 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardHotel.setImage(backCard8);

        Image backCard9 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardRanch.setImage(backCard9);

        Image backCard10 = new Image(getClass().getResourceAsStream("resources/CardBack-small.png"));
        backCardBank.setImage(backCard10);

        Image sceneCard1 = new Image(getClass().getResourceAsStream("resources/01.png"));
        sceneCardSecretHideout.setImage(sceneCard1);

        Image shotToken = new Image(getClass().getResourceAsStream("resources/shot.png"));
        shot.setImage(shotToken);

    }
}
