import java.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.*;

public class LeaderBoardViewController implements PlayerObserver {

    @FXML
    private Label leaderboardLabel;
    @FXML
    private VBox playerScore;

    private ActionManager model = ActionManager.getInstance();

    public void initialize() throws Exception {
        ArrayList<Player> players = model.getPlayers();
        Collections.sort(players, new Comparator<Player>(){
            public int compare(Player p1, Player p2){
                return p2.getCurrentScore() - p1.getCurrentScore();
            }
        });

        int i = 1;
        for(Player p : players) {
            String pl = String.format(
                "%d. %s \t Score: %d%n", i, p.getName(), p.getCurrentScore());

            Label player = new Label(pl);
            player.setFont(new Font("System", 16));
            playerScore.getChildren().add(player);

            i++;
        }
        
        model.getCurrentGame().addAllPlayersObserver(this);
    }

    @Override
    public void update(Player player) {
        playerScore.getChildren().clear();

        ArrayList<Player> players = ActionManager.getInstance().getPlayers();
        Collections.sort(players, new Comparator<Player>(){
            public int compare(Player p1, Player p2){
                return p2.getCurrentScore() - p1.getCurrentScore();
            }
        });

        int i = 1;
        for(Player p : players) {
            String pl = String.format(
                "%d. %s \t Score: %d%n", i, p.getName(), p.getCurrentScore());

            Label playerLabel = new Label(pl);
            playerLabel.setFont(new Font("System", 16));
            playerScore.getChildren().add(playerLabel);

            i++;
        }
    }
}
