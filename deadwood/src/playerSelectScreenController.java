
import java.util.*;

import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import model.*;

public class playerSelectScreenController {
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
    }


    public void handleButtonAction(ActionEvent event) throws Exception{
        Stage stage;
        Parent root;
       
        if(event.getSource()==startGameButton){
            if(ready()){
                //setup players in model
                ActionManager.getInstance().setPlayers(getPlayers());

                //switch scene
                stage = (Stage) startGameButton.getScene().getWindow();
                root = FXMLLoader.load(getClass().getResource("mainGame.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
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
}
