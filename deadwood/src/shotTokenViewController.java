import javafx.fxml.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;

public class shotTokenViewController {
    
    @FXML
    private ImageView shotToken;

    public void initialze(){
        Image shotImage = new Image(getClass().getResourceAsStream("/resources/shot.png"));
        shotToken.setImage(shotImage);
    }
}
