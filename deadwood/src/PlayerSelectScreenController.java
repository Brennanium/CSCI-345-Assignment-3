
import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;

public class PlayerSelectScreenController {
    private final String textFieldErrorStyleText = 
        "-fx-text-box-border: red ;" +
        "-fx-focus-color: red ;";
    private final String colorPickerErrorStyleText = 
        "-fx-border-color: red ;" +
        "-fx-focus-color: red ;";

    @FXML
    private ComboBox<Integer> numOfPlayersComboBox;

    @FXML
    private VBox PACPVStack;

    @FXML
    private Button startGameButton;

    private ObservableList<Integer> listOfNums = FXCollections.observableArrayList();

    private ArrayList<HBox> playerAndNumberPickerList = new ArrayList<HBox>();
    private ArrayList<ComboBox<String>> colorPickerList = new ArrayList<ComboBox<String>>();
    private ArrayList<TextField> textFieldList = new ArrayList<TextField>();

    private ObservableList<String> colors = FXCollections.observableArrayList();
    private HashMap<String, String> colorToHex = new HashMap<String,String>();

    public void initialize(){
        listOfNums.addAll(2,3,4,5,6,7,8);
        setupColors();


        numOfPlayersComboBox.setItems(listOfNums);
    }
    

    public void handleComboBoxAction(ActionEvent event) throws Exception {
        Integer selection = numOfPlayersComboBox.getSelectionModel().getSelectedItem();
        int numOfChoices = playerAndNumberPickerList.size();

        if (selection != null && numOfChoices == 0) {
            //add enough playerAndColorPickerViews 
            //selection is new
            for(int i = 1; i <= selection; i++){
                addNewPlayerAndColorPickerView(i);
            }
        }
        else if (selection != null && selection != numOfChoices) {
            //add new playerAndColorPickerViews
            //selection is greater than current number of views
            if(selection - numOfChoices > 0) {
                for(int i = numOfChoices + 1; i <= selection; i++){
                    addNewPlayerAndColorPickerView(i);
                }
            } else {
                //remove excess views
                //selection is less than current number of views
                while(selection - playerAndNumberPickerList.size() < 0){
                    HBox PACPV = playerAndNumberPickerList.remove(playerAndNumberPickerList.size()-1);
                    ComboBox<String> colorPicker = colorPickerList.remove(colorPickerList.size() -1 );
                    textFieldList.remove(textFieldList.size() -1 );

                    String selectedColor = colorPicker.getSelectionModel().getSelectedItem();
                    
                    //add back the removed colors to other color pickers 
                    if(selectedColor != null) {
                        colorPickerList.stream()
                            .filter(comboBox -> !comboBox.equals(colorPicker))
                            .forEach(comboBox -> {
                                comboBox.getItems().add(selectedColor);
                            });
                    }

                    PACPVStack.getChildren().remove(PACPV);
                }
            }
        }
        if(selection != null) {
            Stage stage = (Stage) startGameButton.getScene().getWindow();
            
            //double stackHeight = PACPVStack.getBoundsInParent().getHeight();

            stage.setHeight((selection * 50) + 250);
        }
    }


    public void handleButtonAction(ActionEvent event) throws Exception{
        Stage stage;
        FXMLLoader loader;
       
        if(event.getSource()==startGameButton){
            if(ready()){
                //setup players in model
                ActionManager.getInstance().setPlayers(getPlayers());

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
                stage.show();
            }
        }
    }

    private void addNewPlayerAndColorPickerView(int i){
        HBox container = new HBox();
        container.setPadding(new Insets(20, 0, 5, 0));
        container.setSpacing(5);
        
        Label player = new Label();
        player.setText("Player " + i);

        //setup text field
        TextField textField = new TextField();
        textField.setPromptText("Name");
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ArrayList<TextField> duplicates = new ArrayList<TextField>();

                textFieldList.stream().filter((TextField tf) -> !tf.equals(textField)).forEach(tf -> {
                    String otherText = tf.getText();
                    if(otherText != null && !otherText.isEmpty()){ 
                        if(otherText.equals(newValue)) {
                            tf.setStyle(textFieldErrorStyleText);
                            duplicates.add(tf);
                        } else {
                            int otherDuplicates = (int)textFieldList.stream().filter(t -> t.getText().equals(otherText)).count();
                            if(otherText.equals(oldValue) && otherDuplicates <= 1){
                                tf.getStyleClass().clear();
                                tf.setStyle(null);
                                tf.getStyleClass().addAll("text-field", "text-input");
                            }
                        }
                    }
                });
                if(duplicates.size() != 0){
                    textField.setStyle(textFieldErrorStyleText);
                } else {
                    textField.getStyleClass().clear();
                    textField.setStyle(null);
                    textField.getStyleClass().addAll("text-field", "text-input");
                }
            }
        });
        textFieldList.add(textField);


        //setup color picker
        ComboBox<String> colorPicker = new ComboBox<String>();
        colorPicker.getItems().addAll(getAvailableColors());
        colorPicker.setPromptText("Select color");
        colorPicker.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                colorPickerList.stream().filter((ComboBox<String> comboBox) -> !comboBox.equals(colorPicker)).forEach(comboBox -> {
                    comboBox.getItems().remove(newValue);
        
                    if (oldValue != null && !comboBox.getItems().contains(oldValue)){
                        comboBox.getItems().add(oldValue);
                    }
                });

                if(oldValue == null && newValue != null){
                    colorPicker.getStyleClass().clear();
                    colorPicker.setStyle(null);
                    colorPicker.getStyleClass().addAll("combo-box", "combo-box-base");
                }
            }
        });
        colorPickerList.add(colorPicker);

        container.getChildren().addAll(player,textField,colorPicker);

        playerAndNumberPickerList.add(container);
        PACPVStack.getChildren().add(container);
    }

    private ArrayList<String> getAvailableColors(){
        ArrayList<String> availableColors = new ArrayList<String>();
        availableColors.addAll(colors);

        String selection;
        for(ComboBox<String> comboBox : colorPickerList) {
            selection = comboBox.getSelectionModel().getSelectedItem();
            if(selection != null) {
                availableColors.remove(selection);
            }
        }

        return availableColors;
    }

    private void setupColors(){
        colors.addAll("blue","cyan","green","orange","pink","red","violet","white","yellow");

        colorToHex.put("blue", "#0075FF");
        colorToHex.put("cyan", "#00FFFF");
        colorToHex.put("green", "2DFF2D");
        colorToHex.put("orange", "#FF7500");
        colorToHex.put("pink", "#FFC5C5");
        colorToHex.put("red", "#FF0000");
        colorToHex.put("violet", "#C500C5");
        colorToHex.put("white", "#FFFFFF");
        colorToHex.put("yellow", "#FFFF00");
    }

    private boolean ready(){
        if(playerAndNumberPickerList.isEmpty())
            return false;

        boolean emptyText = existsEmptyTextField();
        boolean emptyColor = existsEmptyColorPicker();

            
        return !emptyText && !emptyColor && !hasDuplicateName();
            
    }

    private boolean existsEmptyTextField() {
        boolean hasEmpty = false;
        for(TextField tf : textFieldList) {
            if(tf.getText().isEmpty()) {
                hasEmpty = true;
                tf.setStyle(textFieldErrorStyleText);
            }
        }
        return hasEmpty;
    }

    private boolean hasDuplicateName() {
        boolean hasDuplicate;
        
        for(TextField textField : textFieldList) {
            hasDuplicate = textFieldList.stream()
                .filter(tf -> !tf.equals(textField))
                .anyMatch(tf -> tf.getText().equals(textField.getText()));
            if(hasDuplicate)
                return true;
        }
        return false;
    }

    private boolean existsEmptyColorPicker() {
        boolean hasEmpty = false;
        for(ComboBox<String> cb : colorPickerList) {
            if(cb.getSelectionModel().getSelectedItem() == null) {
                hasEmpty = true;
                cb.setStyle(colorPickerErrorStyleText);
            }
        }
        return hasEmpty;
    }

    private ArrayList<Player> getPlayers(){
        ArrayList<Player> players = new ArrayList<Player>();
        String name = "";
        String color = "";
        for(Parent p : playerAndNumberPickerList) {
            name = "";
            color = "";
            for(Node n : p.getChildrenUnmodifiable()) {
                if(n instanceof TextField) {
                    name = ((TextField)n).getText();
                }
                if(n instanceof ComboBox) {
                    color = (String)((ComboBox)n).getSelectionModel().getSelectedItem();
                }
            }
            if(!name.isEmpty() && color != null && !color.isEmpty()){
                players.add(new Player(name, color));
            }
        }

        return players;
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
