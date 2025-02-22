package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;

public class GoalkeeperMoveStrategy implements MoveStrategy {

    GamePlayer gamePlayer;

    public GoalkeeperMoveStrategy(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        MovementUtils.sprintTowardsDynamicBasePosition(gamePlayer);
    }

    @Override
    public boolean isPlayingStrategy() {
        return true;
    }
}
