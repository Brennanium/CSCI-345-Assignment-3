
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

public class playerSelectScreenController {
    @FXML
    private ComboBox<Integer> numOfPlayersComboBox;

    @FXML
    private VBox PACPVStack;

    @FXML
    private Button startGameButton;

    private ObservableList<Integer> listOfNums = FXCollections.observableArrayList();

    private ArrayList<Parent> playerAndNumberPickerList = new ArrayList<Parent>();

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
            Parent PACPV;
            for(int i = 1; i <= selection; i++){
                PACPV = makeNewPlayerAndColorPickerView(i);
                playerAndNumberPickerList.add(PACPV);
                PACPVStack.getChildren().add(PACPV);
            }
        }
        else if (selection != null && selection != numOfChoices) {
            if(selection - numOfChoices > 0) {
                Parent PACPV;
                for(int i = numOfChoices + 1; i <= selection; i++){
                    PACPV = makeNewPlayerAndColorPickerView(i);
                    playerAndNumberPickerList.add(PACPV);
                    PACPVStack.getChildren().add(PACPV);
                }
            } else {
                while(selection - playerAndNumberPickerList.size() < 0){
                    PACPVStack.getChildren().remove(
                        playerAndNumberPickerList.remove(playerAndNumberPickerList.size()-1));
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
                root = FXMLLoader.load(getClass().getResource("playerSelectScreen.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    private Parent makeNewPlayerAndColorPickerView(int i){
        HBox container = new HBox();
        container.setPadding(new Insets(20, 0, 5, 0));
        container.setSpacing(5);
        
        Label player = new Label();
        player.setText("Player " + i);

        TextField textField = new TextField();

        ComboBox<String> colorPicker = new ComboBox<String>();
        colorPicker.setItems(colors);
        colorPicker.setPromptText("Select color");

        container.getChildren().addAll(player,textField,colorPicker);
        return container;
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
        return !playerAndNumberPickerList.isEmpty() &&
            !playerAndNumberPickerList.stream()
                .anyMatch((Parent p) -> {
                    for(Node n : p.getChildrenUnmodifiable()) {
                        if(n instanceof TextField) {
                            return ((TextField)n).getText().isEmpty();
                        }
                    }
                    return true;
                });
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
                    name = (String)((ComboBox)n).getSelectionModel().getSelectedItem();
                }
            }
            if(!name.isEmpty() && !color.isEmpty()){
                players.add(new Player(name, color));
            }
        }

        players.forEach(p -> System.out.println(p));

        return players;
    }
}
