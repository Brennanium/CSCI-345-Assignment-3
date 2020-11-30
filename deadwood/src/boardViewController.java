
import java.util.*;

import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
//import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import model.*;
import model.areas.*;
import model.areas.Set;

public class boardViewController implements PlayerObserver {

    @FXML
    private ImageView boardImageView;
    @FXML
    private Group group;
    
    private ArrayList<ImageView> sceneCardImageViewList = new ArrayList<ImageView>();
    private HashMap<Set,ArrayList<ImageView>> shotTokenImageViewListForSet = new HashMap<Set,ArrayList<ImageView>>();
    private HashMap<Player,ImageView> diceImageViewForPlayer = new HashMap<Player,ImageView>();

    private ActionManager model = ActionManager.getInstance();
    
    public void initialize() {
        model.getCurrentGame().addObserverToPlayers(this);

        Image boardImage = new Image(getClass().getResourceAsStream("resources/board.jpg"));
        boardImageView.setImage(boardImage);
        boardImageView.minWidth(1200);
        boardImageView.minHeight(900);

        //for testing purposes only
        //comment out
        ArrayList<Area> areas = model.getAreas();
        Polygon bounds;
        for(Area a : areas) {
            bounds = a.getPolygon();
            bounds.setFill(Color.TRANSPARENT);
            bounds.setStroke(Color.RED);
            bounds.setStrokeWidth(5);

            group.getChildren().add(bounds);
        }

        initPlayerDice();
        initShotTokens();
        updateSceneCards();
        
    }

    @Override
    public void update(Player player) {
        updateSceneCards();
        updateShotTokens();
        updatePlayerDice();
    }


    private void updatePlayerDice() {
        ArrayList<Player> players = model.getPlayers();
        Role role;
        Set set;
        Rectangle position;
        ImageView diceImageView;

        double onCardX;
        double onCardY;

        for(Player p : players) {
            group.getChildren().remove(diceImageViewForPlayer.get(p));

            role = p.getRole();
            diceImageView = diceImageViewForPlayer.get(p);
            if(p.getCurrentArea() instanceof Set) {
                //if you're on a set and you have a role
                if(role != null){
                    set = (Set)p.getCurrentArea();
                    position = role.getPosition();

                    //if you are on card
                    if(role.checkOnCard()) {
                        onCardX = set.getSceneCardLocation().getX() + role.getPosition().getX();
                        onCardY = set.getSceneCardLocation().getY() + role.getPosition().getY();
                        diceImageView.setLayoutX(onCardX);
                        diceImageView.setLayoutY(onCardY);
                        diceImageView.toFront();
                    } else { //if you are off card
                        diceImageView.setLayoutX(position.getX());
                        diceImageView.setLayoutY(position.getY());
                    }

                    
                } else { //if you're on a set but don't have a role
                    position = getRandomValidDiceLocationForArea(p.getCurrentArea());
                    diceImageView.setLayoutX(position.getX());
                    diceImageView.setLayoutY(position.getY());
                }
            } else { //if you're not on a set
                position = getRandomValidDiceLocationForArea(p.getCurrentArea());
                diceImageView.setLayoutX(position.getX());
                diceImageView.setLayoutY(position.getY());
            }

            diceImageView.minWidth(position.getWidth());
            diceImageView.minHeight(position.getHeight());

            group.getChildren().add(diceImageView);
        }
    }

    private void initPlayerDice() {
        ArrayList<Player> players = model.getPlayers();
        Image diceImage;
        ImageView diceImageView;
        for(Player p : players) {
            diceImage = new Image(getClass().getResourceAsStream("resources/dice/" + p.getImageString()));
            diceImageView = new ImageView(diceImage);

            diceImageViewForPlayer.put(p,diceImageView);
        }
    }

    private void updateSceneCards() {
        group.getChildren().removeAll(sceneCardImageViewList);
        sceneCardImageViewList.clear();

        String sceneCardImageString;
        Rectangle sceneCardBounds;
        Image sceneCardImage;
        ImageView sceneCardImageView;
        for(Set s : model.getSets()){
            sceneCardImageString = s.getSceneCardImageString();
            sceneCardBounds = s.getSceneCardLocation();

            if(sceneCardImageString == null) {
                sceneCardImageString = "resources/CardBack-small.jpg";
            } else {
                sceneCardImageString = "resources/cards/" + sceneCardImageString;
            }

            sceneCardImage = new Image(getClass().getResourceAsStream(sceneCardImageString));
            sceneCardImageView = new ImageView(sceneCardImage);
            sceneCardImageView.setLayoutX(sceneCardBounds.getX());
            sceneCardImageView.setLayoutY(sceneCardBounds.getY());
            sceneCardImageView.minWidth(sceneCardBounds.getWidth());
            sceneCardImageView.minHeight(sceneCardBounds.getHeight());
            sceneCardImageViewList.add(sceneCardImageView);
            group.getChildren().add(sceneCardImageView);
        }
    }

    private void initShotTokens(){
        Image shotTokenImage = new Image(getClass().getResourceAsStream("resources/shot.png"));
        ArrayList<ImageView> shotTokens;
        ImageView shotTokenImageView;
        for(Set s : model.getSets()){
            shotTokens = new ArrayList<ImageView>();
            for(Rectangle r : s.getShotTokenLocations()) {
                shotTokenImageView = new ImageView(shotTokenImage);
                shotTokenImageView.setLayoutX(r.getX());
                shotTokenImageView.setLayoutY(r.getY());
                shotTokenImageView.minWidth(r.getWidth());
                shotTokenImageView.minHeight(r.getHeight());
                
                shotTokens.add(shotTokenImageView);
            }
            shotTokenImageViewListForSet.put(s,shotTokens);
            group.getChildren().addAll(shotTokens);
        }
    }
    
    private void updateShotTokens() {
        ArrayList<ImageView> shotTokenImageViewList;

        int shotTokensToHide;
        for(Set s : model.getSets()){
            shotTokensToHide = s.getMaxShotTokenCount() - s.getShotTokenCount();

            shotTokenImageViewList = shotTokenImageViewListForSet.get(s);
            for(int i = 0; i < shotTokensToHide; i++) {
                shotTokenImageViewList.get(i).setVisible(false);
            }
        }
    }

    private void resetShotTokens() {
        for(Set s : model.getSets()){
            shotTokenImageViewListForSet.get(s).forEach(iv -> iv.setVisible(true));
        }
    }

    private Rectangle getRandomValidDiceLocationForArea(Area area) {
        //dice width 40x40
        //polygon.getBounds()
        //only exclude scene card if scene card is active
        Polygon polygon = area.getPolygon();
        Bounds bounds = polygon.getBoundsInParent();

        //use to get random coordinate in area
        bounds.getMinX();
        bounds.getMaxX();
        bounds.getMinY();
        bounds.getMaxY();

        if(area instanceof Set) {
            Set set = (Set)area;
        }

        return null;
    }
}
