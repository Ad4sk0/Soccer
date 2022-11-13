package soccer.game.entity.player.animation.goal;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.models.playingfield.PlayingField;

public class GkPassToTheMiddleAfterGoalAnimation implements MoveStrategy {

    private final GamePlayer gamePlayer;
    private boolean ballKickedToTheMiddle = false;

    public GkPassToTheMiddleAfterGoalAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (ballKickedToTheMiddle) {
            AnimationUtils.goToBasePositionAndMarkAnimationAsReady(gamePlayer);
        } else if (gamePlayer.hasBall()) {
            kickToTheMiddle();
            ballKickedToTheMiddle = true;
        } else {
            AnimationUtils.goForBall(gamePlayer);
        }
    }

    private void kickToTheMiddle() {
        gamePlayer.kickTowardsTarget(PlayingField.CENTRE_CIRCLE_POSITION, Ball.FAST_PASS_SPEED);
        gamePlayer.getBall().setAnimationTarget(PlayingField.CENTRE_CIRCLE_POSITION);
    }
}
