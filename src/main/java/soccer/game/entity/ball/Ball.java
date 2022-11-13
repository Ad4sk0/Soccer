package soccer.game.entity.ball;

import org.jboss.logging.Logger;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.player.GamePlayer;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;
import soccer.utils.Vector2d;

public class Ball extends MovingEntity {
    public static final double LOSE_SPEED_PER_FRAME_FACTOR = 0.1;
    public static final int BALL_SIZE = 6;
    public static final int SLOW_PASS_SPEED = 40;
    public static final int MEDIUM_PASS_SPEED = 60;
    public static final int FAST_PASS_SPEED = 90;
    Position animationTarget;
    GamePlayer owner;
    GamePlayer previousOwner;
    GameMatch match;
    Logger logger = Logger.getLogger(getClass());

    public Ball(GameMatch match) {
        super(BALL_SIZE);
        this.match = match;
    }

    public void hit(Vector2d direction, int speed) {
        setDirection(direction);
        setCurrentSpeed(speed);
    }

    public void tackle(Position position) {
        setPosition(position);
    }

    @Override
    public void makeMove() {

        // MOVE - Make actual move - based on direction and speed
        move();

        // Naturally reduce speed
        loseSpeed();

        // Validate move
        handleBallEvents();

        // For Animations
        if (match.getGameState() != GameState.PLAYING && position.equals(animationTarget)) {
            stop();
            animationTarget = null;
        }
    }

    private void loseSpeed() {
        if (currentSpeed > 0) {
            currentSpeed = Math.max(0, currentSpeed - LOSE_SPEED_PER_FRAME_FACTOR);
        }
    }

    @Override
    public float getMaxRealSpeedInPxPerFrame() {
        // 0 - no move
        // ~ 50km/h - max shoot speed - 13,89 m/s - 138 ,89 px/s - 2,315 px/s/fps
        return 2.315f;
    }

    private void handleBallEvents() {
        // GOAL POST
        if (MoveValidator.isGoalPostHit(this)) {
            logger.debug("POST hit");
            bounce();
        }

        // GOAL CORNER or OUT - only in playing state
        if (match.getGameState() == GameState.PLAYING
                && (MoveValidator.isGoalLineExceeded(this)
                || MoveValidator.isCornerLineExceeded(this)
                || MoveValidator.isHorizontalSideLineExceeded(this))) {
            stop();
        }
    }

    public void bounceOnX() {
        reverseOnX();
        undoMove();
    }

    public void bounceOnY() {
        reverseOnY();
        undoMove();
    }

    private void bounce() {
        bounceOnX();
    }

    public boolean isFree() {
        return owner == null;
    }

    public boolean isInTheMiddle() {
        return position.equals(PlayingField.CENTRE_CIRCLE_POSITION);
    }

    public boolean isOnCurrentCornerPosition() {
        return position.equals(match.getCornerPerformingPosition());
    }

    public boolean isOutOfField() {
        return MoveValidator.isHorizontalSideLineExceeded(this) || MoveValidator.isVerticalSideLineExceeded(this);
    }

    public GamePlayer getOwner() {
        return owner;
    }

    public void setOwner(GamePlayer owner) {
        this.owner = owner;
    }

    public GamePlayer getPreviousOwner() {
        return previousOwner;
    }

    public void setPreviousOwner(GamePlayer previousOwner) {
        this.previousOwner = previousOwner;
    }

    @Override
    public double getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setAnimationTarget(Position animationTarget) {
        this.animationTarget = animationTarget;
    }

    public void correctBallPosition(Position newPosition) {
        this.position = newPosition;
    }
}
