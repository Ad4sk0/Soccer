package soccer.game.entity.player.animation.resumebygk;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.movement.MoveStrategy;

public class GoToResumeByGkPositionAnimation implements MoveStrategy {

    GamePlayer gamePlayer;

    public GoToResumeByGkPositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, gamePlayer.getResumeByFriendlyGkPosition());
    }
}
