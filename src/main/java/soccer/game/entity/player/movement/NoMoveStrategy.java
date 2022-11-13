package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;

public class NoMoveStrategy implements MoveStrategy {

    GamePlayer player;

    public NoMoveStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleMovement() {
        player.stop();
    }
}
