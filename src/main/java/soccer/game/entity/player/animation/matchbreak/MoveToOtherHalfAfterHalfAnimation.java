package soccer.game.entity.player.animation.matchbreak;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;

public class MoveToOtherHalfAfterHalfAnimation implements Animation {
    private final Animation animation;

    public MoveToOtherHalfAfterHalfAnimation(GamePlayer gamePlayer) {
        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            animation = new FetchBallToTheMiddleAnimation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            animation = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        } else {
            animation = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        animation.handleMovement();
    }
}
