package soccer.game.events;

import org.jboss.logging.Logger;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.PlayerState;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.FieldSite;

public class MatchEventsChecker implements MatchEventsListener {

    GameMatch gameMatch;
    Event previousEvent;
    int debugCounter = 0;

    Logger logger = Logger.getLogger(this.getClass());

    public MatchEventsChecker(GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        gameMatch.registerMatchEventsListener(this);
    }

    private void transmitEvent(Event event) {
        this.gameMatch.produceEvent(event);
    }

    private void considerTransmittingEvent(Event event) {
        if (previousEvent == null || !previousEvent.equals(event)) {
            transmitEvent(event);
            previousEvent = event;
            return;
        }
        debugCounter++;
        if (debugCounter > 100000) {
            logger.error("POSSIBLE ERROR: Unable to transmit event: " + event);
            debugCounter = 0;
        }
    }

    private boolean areAllPlayersAnimationReady() {
        for (GamePlayer player : gameMatch.getAllPlayingPlayers()) {
            if (player.getPlayerState() != PlayerState.IS_ANIMATION_READY) {
                return false;
            }
        }
        return true;
    }

    private boolean areAllPlayersReadyForStartFromTheMiddle() {
        return gameMatch.getGameState() == GameState.GOAL_ANIMATION && gameMatch.getBall().isInTheMiddle() && areAllPlayersAnimationReady();
    }

    private boolean areAllPlayersReadyAfterBreak() {
        return gameMatch.getGameState() == GameState.BREAK && areAllPlayersAnimationReady();
    }

    private boolean areAllPlayersReadyToStartCorner() {
        return gameMatch.getGameState() == GameState.CORNER_ANIMATION && gameMatch.getBall().isOnCurrentCornerPosition() && areAllPlayersAnimationReady();
    }

    private boolean isGoal() {
        return MoveValidator.isGoalLineExceeded(gameMatch.getBall());
    }

    private boolean isGoalPostHit() {
        return MoveValidator.isGoalPostHit(gameMatch.getBall());
    }

    private FieldSite getCornerSite() {
        if (gameMatch.getBall().getPosition().isBehindLeftLine()) {
            return FieldSite.LEFT;
        }
        if (gameMatch.getBall().getPosition().isBehindRightLine()) {
            return FieldSite.RIGHT;
        }
        logger.error("Corner site requested however ball is not behind any vertical line");
        return null;
    }

    private boolean isBehindVerticalLine() {
        if (!MoveValidator.isCornerLineExceeded(gameMatch.getBall())) {
            return false;
        }
        if (gameMatch.getBall().getPreviousOwner() == null) {
            throw new UnsupportedOperationException("Previous owner of the ball cannot be null. Unable to determine corner");
        }
        return true;
    }

    private boolean isCorner() {
        return isBehindVerticalLine() && getLastTouchBallSite() == getCornerSite();
    }

    private boolean isResumeByGk() {
        return isBehindVerticalLine() && getLastTouchBallSite() != getCornerSite();
    }

    private FieldSite getLastTouchBallSite() {
        if (gameMatch.getBall().getOwner() != null) {
            return gameMatch.getBall().getOwner().getFieldSite();
        }
        return gameMatch.getBall().getPreviousOwner().getFieldSite();
    }

    private boolean isOut() {
        return MoveValidator.isHorizontalSideLineExceeded(gameMatch.getBall());
    }

    public void checkMatchEvents() {

        if (areAllPlayersReadyForStartFromTheMiddle()) {
            considerTransmittingEvent(new Event(EventTypes.GOAL_ANIMATION_ALL_PLAYERS_READY));
        }

        if (areAllPlayersReadyAfterBreak()) {
            considerTransmittingEvent(new Event(EventTypes.GOAL_ANIMATION_ALL_PLAYERS_READY));
        }

        if (areAllPlayersReadyToStartCorner()) {
            considerTransmittingEvent(new Event(EventTypes.CORNER_ALL_PLAYERS_READY));
        }

        // Check further events only in playing state
        if (gameMatch.getGameState() != GameState.PLAYING) return;

        if (isGoal()) {
            considerTransmittingEvent(new Event(EventTypes.GOAL));
        }

        if (isGoalPostHit()) {
            considerTransmittingEvent(new Event(EventTypes.GOAL_POST_HITTED));
        }

        if (isCorner()) {
            considerTransmittingEvent(new Event(EventTypes.CORNER));
        }

        if (isResumeByGk()) {
            considerTransmittingEvent(new Event(EventTypes.RESUME_BY_GK));
        }

        if (isOut()) {
            Event event = new Event(EventTypes.OUT);
            considerTransmittingEvent(event);
        }

        // Event Shoot with info like distance, player, shot style etc.

        // Event Odbiór piłki

        // Event Pass
    }

    @Override
    public void receiveEvent(Event event) {
        previousEvent = event;
    }
}
