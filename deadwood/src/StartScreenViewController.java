import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.input.*;
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
        FXMLLoader loader;
       
        if(event.getSource()==startGameButton){
            stage = (Stage) startGameButton.getScene().getWindow();
            loader = new FXMLLoader(getClass().getResource("playerSelectScreen.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            //For testing purposes only
            /* ArrayList<Player> players2 = new ArrayList<Player>(Arrays.asList(
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
            loader = new FXMLLoader(getClass().getResource("mainGame.fxml"));
            Scene scene = new Scene(loader.load());
            MainGameController controller = loader.getController();
            setUpKeyboardShortcut(scene, controller);
            stage.setScene(scene);
            setupBoardResizing(stage, controller);
            stage.setMinHeight(450 + 80);
            stage.setMinWidth(600 + 200);
            stage.setTitle("Deadwood: " + ActionManager.getInstance().getDayString());
            stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (e) -> {
                controller.leaderboardWindow.close();
                Platform.exit();
            });
            stage.show(); */
        }
    }

    //setup keyboard shortcuts
    private void setUpKeyboardShortcut(Scene scene, MainGameController controller){
        KeyCombination kcm = new KeyCodeCombination(KeyCode.M, KeyCombination.SHORTCUT_DOWN);
        //Mnemonic mnm = new Mnemonic(controller.moveSplitMenuButton, kcm);
        scene.getAccelerators().put(kcm, ()-> {
            if(controller.moveSplitMenuButton.isVisible()) controller.moveSplitMenuButton.show();
        });
        KeyCombination kcw = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        //Mnemonic mnw = new Mnemonic(controller.workSplitMenuButton, kcw);
        scene.getAccelerators().put(kcw, ()-> {
            if(controller.workSplitMenuButton.isVisible()) controller.workSplitMenuButton.show();
        });
        KeyCombination kca = new KeyCodeCombination(KeyCode.A, KeyCombination.SHORTCUT_DOWN);
        Mnemonic mna = new Mnemonic(controller.actButton, kca);
        scene.addMnemonic(mna);
        KeyCombination kcr = new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN);
        Mnemonic mnr = new Mnemonic(controller.rehearseButton, kcr);
        scene.addMnemonic(mnr);
        KeyCombination kcl = new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN);
        Mnemonic mnl = new Mnemonic(controller.leaderboardButton, kcl);
        scene.addMnemonic(mnl);
        KeyCombination kcu = new KeyCodeCombination(KeyCode.U, KeyCombination.SHORTCUT_DOWN);
        //Mnemonic mnu = new Mnemonic(controller.upgradeSplitMenuButton, kcu);
        scene.getAccelerators().put(kcu, ()-> {
            if(controller.upgradeSplitMenuButton.isVisible()) controller.upgradeSplitMenuButton.show();
        });
        //scene.addMnemonic(mnu);
        KeyCombination kce = new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN);
        Mnemonic mne = new Mnemonic(controller.endTurnButton, kce);
        scene.addMnemonic(mne);
        KeyCombination kcb = new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN);
        //Mnemonic mnm = new Mnemonic(controller.moveSplitMenuButton, kcm);
        scene.getAccelerators().put(kcb, ()-> controller.boardContoller.toggleDebugAreaOutlines());
    }


    private void setupBoardResizing(Stage stage, MainGameController controller) {
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            final double playerInfoViewWidth = 200;
            final double buttonsHeight = 80;

            double minScale;
            double heightScale = (stage.getHeight() - buttonsHeight)/ 900;
            double widthScale = (stage.getWidth() - playerInfoViewWidth) / 1200;

            if(heightScale >= 1 && widthScale >= 1 ) {
                minScale = 1;
            } else if(heightScale < widthScale) {
                minScale = heightScale;
            } else if(heightScale > widthScale) {
                minScale = widthScale;
            } else {
                minScale = widthScale;
            }

            controller.board.setScaleX(minScale);
            controller.board.setScaleY(minScale);
            
            controller.mainBG.getChildren().remove(controller.boardGroup);
            controller.boardGroup = new Group(controller.board);
            controller.boardGroup.setAutoSizeChildren(true);
            controller.mainBG.getChildren().add(controller.boardGroup);
            controller.boardGroup.setLayoutX(playerInfoViewWidth - (600 * (1-minScale)));
            controller.boardGroup.setLayoutY(0 - (450 * (1-minScale)));
        };

        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener); 
    }
}