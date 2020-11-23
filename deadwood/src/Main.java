
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

        Parent root = FXMLLoader.load(getClass().getResource("boardView.fxml"));
        //primaryStage.setTitle("Welcome to Deadwood");
        primaryStage.setScene(new Scene(root, 1200, 900));
        primaryStage.setMinWidth(1200);
        primaryStage.setMinHeight(920);

        Board testBoard = Board.getInstance();

        primaryStage.show();
        /* Image diceImage =  new Image(getClass().getResourceAsStream("resources/r2.png"));
        this.currentPlayerDiceImage.setImage(diceImage);
        this.currentPlayerDiceImage.setLayoutX(100);
        this.currentPlayerDiceImage.setLayoutY(100); */

        //Parent root1 = FXMLLoader.load(getClass().getResource("currentPlayerInfoView.fxml"));
        //primaryStage.setTitle("Welcome to Deadwood");
        //primaryStage.setScene(new Scene(root1, 1000, 700));
        //primaryStage.show();
        

    }


    public static void main(String[] args) {
        launch(args);
    }
}
