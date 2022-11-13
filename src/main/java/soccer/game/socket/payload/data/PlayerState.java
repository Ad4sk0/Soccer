package soccer.game.socket.payload.data;

import soccer.game.entity.player.GamePlayer;
import soccer.models.positions.PlayingPosition;

public class PlayerState {

    private final double x;
    private final double y;

    private final int number;

    private final PlayingPosition position;

    public PlayerState(GamePlayer gamePlayer) {
        x = gamePlayer.getX();
        y = gamePlayer.getY();
        number = gamePlayer.getNumber();
        position = gamePlayer.getPlayingPosition();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getNumber() {
        return number;
    }

    public PlayingPosition getPosition() {
        return position;
    }
}
