
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
import model.events.*;

public class boardViewController implements PlayerObserver, EventObserver {

    @FXML
    private ImageView boardImageView;
    @FXML
    private Group group;

    private ArrayList<ImageView> sceneCardImageViewList = new ArrayList<ImageView>();
    private HashMap<Set, ArrayList<ImageView>> shotTokenImageViewListForSet = new HashMap<Set, ArrayList<ImageView>>();
    private HashMap<Player, ImageView> diceImageViewForPlayer = new HashMap<Player, ImageView>();

    private ActionManager model = ActionManager.getInstance();

    public void initialize() {
        model.getCurrentGame().addAllPlayersObserver(this);
        model.getCurrentGame().addEventObserver(this);

        Image boardImage = new Image(getClass().getResourceAsStream("resources/board.jpg"));
        boardImageView.setImage(boardImage);
        boardImageView.minWidth(1200);
        boardImageView.minHeight(900);

        // for testing purposes only
        // comment out
        ArrayList<Area> areas = model.getAreas();
        Polygon bounds;
        for (Area a : areas) {
            bounds = a.getPolygon();
            bounds.setFill(Color.TRANSPARENT);
            bounds.setStroke(Color.RED);
            bounds.setStrokeWidth(5);

            group.getChildren().add(bounds);
        }

        updateSceneCards();
        initPlayerDice();
        initShotTokens();

    }

    @Override
    public void update(Player player) {
        //updateSceneCards();
        updateShotTokens();
        if(player == model.getCurrentPlayer() && player.getHasMoved()) {
            updatePlayerDie(player);
        }
    }

    private void updatePlayerDice() {
        for(Player p : model.getPlayers()) {
            updatePlayerDie(p);
        }
    }

    private void updatePlayerDie(Player p) {
        Role role;
        Set set;
        Rectangle position;
        ImageView diceImageView;

        double onCardX;
        double onCardY;

        role = p.getRole();
        diceImageView = diceImageViewForPlayer.get(p);
        if (p.getCurrentArea() instanceof Set) {
            // if you're on a set and you have a role
            if (role != null) {
                set = (Set) p.getCurrentArea();
                position = role.getPosition();

                // if you are on card
                if (role.checkOnCard()) {
                    onCardX = set.getSceneCardLocation().getX() + role.getPosition().getX();
                    onCardY = set.getSceneCardLocation().getY() + role.getPosition().getY();
                    diceImageView.setLayoutX(onCardX);
                    diceImageView.setLayoutY(onCardY);
                } else { // if you are off card
                    diceImageView.setLayoutX(position.getX());
                    diceImageView.setLayoutY(position.getY());
                }

            } else { // if you're on a set but don't have a role
                position = getRandomValidDiceLocationForArea(p.getCurrentArea());
                diceImageView.setLayoutX(position.getX());
                diceImageView.setLayoutY(position.getY());
            }
        } else { // if you're not on a set
            position = getRandomValidDiceLocationForArea(p.getCurrentArea());
            diceImageView.setLayoutX(position.getX());
            diceImageView.setLayoutY(position.getY());
        }
        Image diceImage = new Image(getClass().getResourceAsStream("resources/dice/" + p.getImageString()));
        diceImageView.setImage(diceImage);
        diceImageView.minWidth(position.getWidth());
        diceImageView.minHeight(position.getHeight());
        diceImageView.toFront();
        //group.getChildren().add(diceImageView);
        
    }

    private void initPlayerDice() {
        ArrayList<Player> players = model.getPlayers();
        Image diceImage;
        ImageView diceImageView;
        for (Player p : players) {
            diceImage = new Image(getClass().getResourceAsStream("resources/dice/" + p.getImageString()));
            diceImageView = new ImageView(diceImage);

            diceImageViewForPlayer.put(p, diceImageView);
            group.getChildren().add(diceImageView);
        }
        updatePlayerDice();
    }

    private void updateSceneCards() {
        group.getChildren().removeAll(sceneCardImageViewList);
        sceneCardImageViewList.clear();

        String sceneCardImageString;
        Rectangle sceneCardBounds;
        Image sceneCardImage;
        ImageView sceneCardImageView;
        for (Set s : model.getSets()) {
            sceneCardImageString = s.getSceneCardImageString();
            sceneCardBounds = s.getSceneCardLocation();

            if (sceneCardImageString == null) {
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
            sceneCardImageView.toBack();
            boardImageView.toBack();
        }
    }

    private void initShotTokens() {
        Image shotTokenImage = new Image(getClass().getResourceAsStream("resources/shot.png"));
        ArrayList<ImageView> shotTokens;
        ImageView shotTokenImageView;
        for (Set s : model.getSets()) {
            shotTokens = new ArrayList<ImageView>();
            for (Rectangle r : s.getShotTokenLocations()) {
                shotTokenImageView = new ImageView(shotTokenImage);
                shotTokenImageView.setLayoutX(r.getX());
                shotTokenImageView.setLayoutY(r.getY());
                shotTokenImageView.minWidth(r.getWidth());
                shotTokenImageView.minHeight(r.getHeight());

                shotTokens.add(shotTokenImageView);
            }
            shotTokenImageViewListForSet.put(s, shotTokens);
            group.getChildren().addAll(shotTokens);
        }
    }

    private void updateShotTokens() {
        ArrayList<ImageView> shotTokenImageViewList;

        int shotTokensToHide;
        for (Set s : model.getSets()) {
            shotTokensToHide = s.getMaxShotTokenCount() - s.getShotTokenCount();

            shotTokenImageViewList = shotTokenImageViewListForSet.get(s);
            for (int i = 0; i < shotTokensToHide; i++) {
                shotTokenImageViewList.get(i).setVisible(false);
            }
        }
    }

    private void resetShotTokens() {
        for (Set s : model.getSets()) {
            shotTokenImageViewListForSet.get(s).forEach(iv -> iv.setVisible(true));
        }
    }

    private Rectangle getRandomValidDiceLocationForArea(Area area) {
        // dice width 40x40
        // polygon.getBounds()
        // only exclude scene card if scene card is active

        boolean isValid = false;
        Polygon polygon = area.getPolygon();
        Bounds bounds = polygon.getBoundsInParent();
        Rectangle testBounds = null;

        //use to get random coordinate in area
        double xMin = bounds.getMinX();
        double xMax = (bounds.getMaxX()) - 40;
        double yMin = bounds.getMinY();
        double yMax = (bounds.getMaxY()) - 40;

        double x = 0.0;
        double y = 0.0;
        double w = 40.0;
        double h = 40.0;

        while(!isValid) {
            x = (double) (Math.random() * (xMax - xMin + 1) + xMin);
            y = (double) (Math.random() * (yMax - yMin + 1) + yMin);

            testBounds = new Rectangle(x,y,w,h);

            isValid = area.isValidPlayerCoodinate(testBounds, diceImageViewForPlayer);
        }

        return testBounds;
    }


    public void update(Event event) {
        if(event instanceof EndSceneEvent) {
            updateSceneCards();
            for(Player p : ((EndSceneEvent)event).getAffectedPlayers()){
                updatePlayerDie(p);
            }
        }
        if(event instanceof EndDayEvent) {
            resetShotTokens();
            updateSceneCards();
            updatePlayerDice();
        }
        if(event instanceof EndGameEvent) {
            for(Player p : model.getPlayers()){
                diceImageViewForPlayer.get(p).setVisible(false);
            }
        }

    }
}
