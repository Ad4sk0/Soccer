package soccer.game.entity.player;

import soccer.game.entity.player.movement.MoveStrategy;

public class TestMoveStrategy implements MoveStrategy {
    @Override
    public void handleMovement() {

    }

    @Override
    public boolean isPlayingStrategy() {
        return true;
    }
}
