package soccer.game.entity.player.animation.resumebygk;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.events.EventTypes;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

public class GkResumeGameAnimation implements Animation {

    private final GamePlayer gamePlayer;
    Position kickFromPosition;
    private boolean ballPassed = false;

    public GkResumeGameAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        kickFromPosition = gamePlayer.getResumeByFriendlyGkPosition();
    }

    @Override
    public void handleMovement() {
        if (ballPassed) {
            AnimationUtils.stopAndMarkAnimationAsReady(gamePlayer);
            gamePlayer.getMatch().produceEvent(EventTypes.RESUME_BY_GK_PERFORMED);
        } else if (isOnResumeByGkPositionWithBall() && AnimationUtils.areAllOtherPlayersAnimationReady(gamePlayer)) {
            performPass();
        } else if (gamePlayer.hasBall()) {
            AnimationUtils.goToPosition(gamePlayer, kickFromPosition);
        } else {
            AnimationUtils.goForBall(gamePlayer);
        }
    }

    private void performPass() {
        Position passTarget = PlayingField.CENTRE_CIRCLE_POSITION;
        passTarget.setX(passTarget.getX());
        passTarget.setY(passTarget.getY() + 10);
        gamePlayer.kickTowardsTarget(passTarget, Ball.FAST_PASS_SPEED);
        ballPassed = true;
    }

    private boolean isOnResumeByGkPositionWithBall() {
        return gamePlayer.isOnPosition(kickFromPosition) && gamePlayer.hasBall();
    }
}
