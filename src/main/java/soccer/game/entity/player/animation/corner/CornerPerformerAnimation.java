package soccer.game.entity.player.animation.corner;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.events.EventTypes;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.models.playingfield.PlayingFieldUtils;
import soccer.utils.Position;

public class CornerPerformerAnimation implements MoveStrategy {
    private final GamePlayer gamePlayer;
    private final Position cornerPosition;
    private boolean ballPassed = false;

    public CornerPerformerAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        cornerPosition = gamePlayer.getMatch().getCornerPerformingPosition();
    }

    @Override
    public void handleMovement() {
        if (ballPassed) {
            AnimationUtils.stopAndMarkAnimationAsReady(gamePlayer);
            gamePlayer.getMatch().produceEvent(EventTypes.CORNER_PERFORMED);
        } else if (isOnCornerPositionWithBall() && AnimationUtils.areAllOtherPlayersAnimationReady(gamePlayer)) {
            performPass();
        } else if (gamePlayer.hasBall()) {
            AnimationUtils.goToPosition(gamePlayer, cornerPosition);
        } else {
            AnimationUtils.goForBall(gamePlayer);
        }
        correctBallPositionBeforeKick();
    }

    private boolean isOnCornerPositionWithBall() {
        return gamePlayer.isOnPosition(cornerPosition) && gamePlayer.hasBall();
    }

    private void correctBallPositionBeforeKick() {
        // Correct ball position to not go out of line
        if (isOnCornerPositionWithBall() && gamePlayer.getBall().isOutOfField()) {
            gamePlayer.getBall().correctBallPosition(cornerPosition);
        }
    }

    private void performPass() {
        Position passTarget = new Position(PlayingField.PENALTY_AREA_WIDTH / 2, PlayingField.FIELD_HEIGHT / 2);
        if (gamePlayer.getFieldSite() == FieldSite.LEFT) {
            PlayingFieldUtils.moveXToOtherHalf(passTarget);
        }
        gamePlayer.kickTowardsTarget(passTarget, Ball.FAST_PASS_SPEED);
        ballPassed = true;
    }
}
