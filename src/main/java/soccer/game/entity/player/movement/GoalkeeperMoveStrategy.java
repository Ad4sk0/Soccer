package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;

public class GoalkeeperMoveStrategy implements MoveStrategy {

    GamePlayer player;

    public GoalkeeperMoveStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleMovement() {
        player.stop();
    }
}
