package soccer.game.entity.player.animation;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.movement.MoveStrategy;

public class GoToBasePositionAnimation implements MoveStrategy {

    GamePlayer gamePlayer;

    public GoToBasePositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToBasePositionAndMarkAnimationAsReady(gamePlayer);
    }
}
