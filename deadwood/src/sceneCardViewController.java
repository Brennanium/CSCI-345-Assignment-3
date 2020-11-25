import javafx.fxml.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;

public class sceneCardViewController {
    @FXML
    private ImageView backSceneCard;
    @FXML
    private ImageView frontSceneCard;

    public void initializa(){
        Image back = new Image(getClass().getResourceAsStream("/resources/CardBack-small.jpg"));
        backSceneCard.setImage(back);

        Image front = new Image(getClass().getResourceAsStream("/resources/01.png"));
        frontSceneCard.setImage(front);
    }
}
