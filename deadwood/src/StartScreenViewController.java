import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.stage.*;

public class StartScreenViewController {
    @FXML
    private Button startGameButton;
    @FXML
    private ImageView gameLogoImageView;

    public void initialize(){
        Image gameLogo = new Image(getClass().getResourceAsStream("/resources/Images From Rules/image--001.jpg"));
        gameLogoImageView.setImage(gameLogo);
    }

    public void handleButtonAction(ActionEvent event) throws Exception {
        Stage stage;
        Parent root;
       
        if(event.getSource()==startGameButton){
            stage = (Stage) startGameButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("playerSelectScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}