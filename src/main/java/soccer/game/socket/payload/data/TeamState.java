package soccer.game.socket.payload.data;

import soccer.game.entity.player.GamePlayer;
import soccer.game.team.GameTeam;

import java.util.ArrayList;
import java.util.List;

public class TeamState {

    private final List<PlayerState> players = new ArrayList<>();

    public TeamState(GameTeam gameTeam) {
        for (GamePlayer gamePlayer : gameTeam.getPlayingPlayers()) {
            players.add(new PlayerState(gamePlayer));
        }
    }

    public List<PlayerState> getPlayers() {
        return players;
    }
}
