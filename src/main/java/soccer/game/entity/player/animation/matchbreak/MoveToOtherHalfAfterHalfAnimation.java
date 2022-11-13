package soccer.game.entity.player.animation.matchbreak;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;
import soccer.game.entity.player.movement.MoveStrategy;

public class MoveToOtherHalfAfterHalfAnimation implements MoveStrategy {
    private final MoveStrategy moveStrategy;

    public MoveToOtherHalfAfterHalfAnimation(GamePlayer gamePlayer) {
        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            moveStrategy = new FetchBallToTheMiddleAnimation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            moveStrategy = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        } else {
            moveStrategy = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        moveStrategy.handleMovement();
    }
}
