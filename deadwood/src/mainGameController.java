import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class mainGameController {

    @FXML
    private HBox mainBG;

    public void initialize() throws Exception {
        Stage stage;
        Parent board;
        Parent playerInfo;
        
        //stage = (Stage)
        //stage =  
        board = FXMLLoader.load(getClass().getResource("boardView.fxml"));
        playerInfo = FXMLLoader.load(getClass().getResource("currentPlayerInfoView.fxml"));

        Scene scene = new Scene(board);
        
        //stage.setScene(scene);
        //stage.show();
        //HBox mainBG = new HBox();
       // mainBG.setId("currentPlayerInfoView.fxml");


        
    }

    private Parent makeNewHBox(){
        HBox mainBG = new HBox();
        mainBG.setPadding(new Insets(10, 0, 0, 10));

        return mainBG;

    }


}
