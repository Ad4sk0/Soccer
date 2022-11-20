package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;


public class AwaitingPassMoveStrategy implements MoveStrategy {

    GamePlayer gamePlayer;

    public AwaitingPassMoveStrategy(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        MovementUtils.directTowardsBall(gamePlayer);
        gamePlayer.sprint();
    }

    @Override
    public boolean isPlayingStrategy() {
        return false;
    }
}