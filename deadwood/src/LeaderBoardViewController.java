import java.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.*;

public class LeaderBoardViewController {

    @FXML
    private Label leaderboardLabel;
    @FXML
    private VBox playerScore;

    public void initialize() throws Exception {
        ArrayList<Player> player = ActionManager.getInstance().getPlayers();
        Collections.sort(player, new Comparator<Player>(){
            public int compare(Player p1, Player p2){
                return p2.getCurrentScore() - p1.getCurrentScore();
            }
        });

        int i = 1;
        for(Player p : player) {
            String pl = String.format(
                "%d. %s \t Score: %d%n", i, p.getName(), p.getCurrentScore());

            Label players = new Label(pl);
            players.setFont(new Font("System", 16));
            playerScore.getChildren().add(players);

            i++;
        }
    }
}
