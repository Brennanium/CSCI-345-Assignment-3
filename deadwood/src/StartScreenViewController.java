import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.stage.*;

import model.*;

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
            /* stage = (Stage) startGameButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("playerSelectScreen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); */

            //For testing purposes only
            ArrayList<Player> players2 = new ArrayList<Player>(Arrays.asList(
                new Player[]{
                    new Player("A", "blue"),
                    new Player("B", "cyan")
                }
            ));
            ArrayList<Player> players4 = new ArrayList<Player>(Arrays.asList(
                new Player[]{
                    new Player("Brennan 1", "blue"),
                    new Player("Brennan 2", "cyan"),
                    new Player("Thannaree 1", "green"),
                    new Player("Thannaree 2", "pink"),
                }
            ));
            ArrayList<Player> players8 = new ArrayList<Player>(Arrays.asList(
                new Player[]{
                    new Player("A", "blue"),
                    new Player("B", "cyan"),
                    new Player("C", "green"),
                    new Player("D", "pink"),
                    new Player("E", "red"),
                    new Player("F", "violet"),
                    new Player("G", "yellow"),
                    new Player("H", "white")
                }
            ));
            ActionManager.getInstance().setPlayers(players4);

            //switch scene
            stage = (Stage) startGameButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("mainGame.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}