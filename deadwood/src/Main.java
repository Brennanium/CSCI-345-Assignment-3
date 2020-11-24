
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
//import javafx.scene.image.*;

import model.Board;

public class Main extends Application {

    //private ImageView currentPlayerDiceImage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /* Parent root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show(); */

        /* Parent root = FXMLLoader.load(getClass().getResource("boardView.fxml"));
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(920);

        Board testBoard = Board.getInstance();

        primaryStage.show(); */

        //Parent root1 = FXMLLoader.load(getClass().getResource("currentPlayerInfoView.fxml"));
        //primaryStage.setScene(new Scene(root1, 1200, 900));
        //primaryStage.show();
        
        /* Parent root = FXMLLoader.load(getClass().getResource("buttonListView.fxml"));
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(920);
        primaryStage.show(); */

        /* Parent root = FXMLLoader.load(getClass().getResource("sceneCardView.fxml"));
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(920);
        primaryStage.show(); */

        /* Parent root = FXMLLoader.load(getClass().getResource("playerDiceView.fxml"));
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(920);
        primaryStage.show(); */

        Parent root = FXMLLoader.load(getClass().getResource("shotTokenView.fxml"));
        primaryStage.setScene(new Scene(root, 45, 59));
        primaryStage.setMinWidth(45);
        primaryStage.setMinHeight(59);
        primaryStage.show();

       /*  Parent root = FXMLLoader.load(getClass().getResource("shotTokenView.fxml"));
        primaryStage.setScene(new Scene(root, 45, 59));
        primaryStage.setBackground(new Background(new BackgroundFill(Gauge.DARK_COLOR, CornerRadii.EMPTY)));

        primaryStage.setMinWidth(45);
        primaryStage.setMinHeight(59);
        primaryStage.show(); */
        
    }


    public static void main(String[] args) {
        launch(args);
    }
}
