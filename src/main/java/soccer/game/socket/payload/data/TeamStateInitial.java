package soccer.game.socket.payload.data;

import soccer.app.entities.team.ShirtSet;
import soccer.game.entity.player.GamePlayer;
import soccer.game.team.GameTeam;

import java.util.ArrayList;
import java.util.List;

public class TeamStateInitial {

    private final String name;
    private final String shirtColor;
    private final String numberColor;
    private final String goalkeeperShirtColor;
    private final String goalkeeperNumberColor;
    List<PlayerStateInitial> players = new ArrayList<>();

    public TeamStateInitial(GameTeam gameTeam) {
        for (GamePlayer gamePlayer : gameTeam.getAllPlayers()) {
            players.add(new PlayerStateInitial(gamePlayer));
        }
        ShirtSet shirtSet = gameTeam.getShirtSet();
        this.name = gameTeam.getName();
        this.shirtColor = shirtSet.getShirtColorAsHexString();
        this.numberColor = shirtSet.getNumberColorAsHexString();
        this.goalkeeperShirtColor = shirtSet.getGoalkeeperShirtColorAsHexString();
        this.goalkeeperNumberColor = shirtSet.getGoalkeeperNumberColorAsHexString();
    }

    public String getName() {
        return name;
    }

    public String getShirtColor() {
        return shirtColor;
    }

    public String getNumberColor() {
        return numberColor;
    }

    public String getGoalkeeperShirtColor() {
        return goalkeeperShirtColor;
    }

    public String getGoalkeeperNumberColor() {
        return goalkeeperNumberColor;
    }

    public List<PlayerStateInitial> getPlayers() {
        return players;
    }
}
