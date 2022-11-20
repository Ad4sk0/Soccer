package soccer.game.entity.player.animation.resumebygk;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;

public class GoToResumeByGkPositionAnimation implements Animation {

    GamePlayer gamePlayer;

    public GoToResumeByGkPositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, gamePlayer.getResumeByFriendlyGkPosition());
    }
}
