
import java.util.*;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;



public class mainGameController {

    @FXML
    private HBox mainBG;

    public void initialize() throws Exception {
        Stage stage;
        Parent board;
        Parent playerInfo;
        Parent buttons;
        
        board = FXMLLoader.load(getClass().getResource("boardView.fxml"));
        playerInfo = FXMLLoader.load(getClass().getResource("currentPlayerInfoView.fxml"));
        buttons = FXMLLoader.load(getClass().getResource("buttonListView.fxml"));
        HBox mainBG = new HBox();
        mainBG.getChildren().add(board);
        mainBG.getChildren().add(playerInfo);
        mainBG.getChildren().add(buttons);

        Scene scene = new Scene(mainBG);
        
        stage.setScene(scene);
        stage.show();


        
    }


}
