package soccer.game.socket.payload.data;

import soccer.app.entities.player.PlayerStats;
import soccer.game.entity.player.GamePlayer;
import soccer.models.positions.PlayingPosition;

public class PlayerStateInitial {
    private final String name;
    private final int number;
    private final PlayingPosition position;
    private final PlayerStats playerStats;
    private final int overall;

    public PlayerStateInitial(GamePlayer gamePlayer) {
        this.name = gamePlayer.getName();
        this.number = gamePlayer.getNumber();
        this.position = gamePlayer.getPlayingPosition();
        this.playerStats = gamePlayer.getStats();
        this.overall = gamePlayer.getOverall();
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public PlayingPosition getPosition() {
        return position;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public int getOverall() {
        return overall;
    }
}
