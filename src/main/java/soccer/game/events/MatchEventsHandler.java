package soccer.game.events;

import org.jboss.logging.Logger;
import soccer.game.GameState;
import soccer.game.match.GameMatch;


public class MatchEventsHandler implements MatchEventsListener {

    private final Logger logger = Logger.getLogger(getClass());
    GameMatch gameMatch;

    public MatchEventsHandler(GameMatch gameMatch) {
        this.gameMatch = gameMatch;
        gameMatch.registerMatchEventsListener(this);
    }

    @Override
    public void receiveEvent(Event event) {
        handleEvents(event);
    }

    private void handleEvents(Event event) {

        switch (event.getEventType()) {
            case MATCH_CREATED: {
                handleMatchCreated();
                break;
            }
            case MATCH_INITIALIZED: {
                handleMatchInitialized();
                break;
            }
            case MATCH_START: {
                handleMatchStart();
                break;
            }

            case PREPARATION_FOR_MATCH_START_ALL_PLAYERS_READY: {
                handlePreparationForMatchStartAllPlayersReady();
                break;
            }
            case FIRST_PASS_FROM_THE_MIDDLE_PERFORMED: {
                handleFirstPassFromTheMiddlePerformed();
                break;
            }

            case GOAL: {
                handleGoalEvent();
                break;
            }
            case GOAL_ANIMATION_ALL_PLAYERS_READY: {
                handleAllPlayersReadyForStartFromMiddle();
                break;
            }

            case CORNER: {
                handleCorner();
                break;
            }
            case CORNER_ALL_PLAYERS_READY: {
                handleCornerAllPlayersReady();
                break;
            }
            case CORNER_PERFORMED: {
                handleCornerPerformed();
                break;
            }
            case RESUME_BY_GK: {
                handleResumeByGk();
                break;
            }
            case RESUME_BY_GK_PERFORMED: {
                handleResumeByGkPerformed();
                break;
            }
            case END_FIRST_HALF: {
                handleEndFirstHalf();
                break;
            }
            case END_SECOND_HALF: {
                handleEndSecondHalf();
                break;
            }
            default:
                logger.warn("Unhandled event: " + event);
        }
    }

    private void handleResumeByGkPerformed() {
        gameMatch.setGameState(GameState.PLAYING);
    }

    private void handleMatchStart() {
        gameMatch.setGameState(GameState.START_FROM_THE_MIDDLE);
    }

    private void handleMatchCreated() {
        gameMatch.setGameState(GameState.INITIALIZING);
    }

    private void handleMatchInitialized() {
        gameMatch.setGameState(GameState.INITIALIZED);
    }

    private void handleEndFirstHalf() {
        gameMatch.setGameState(GameState.BREAK);
    }

    private void handleEndSecondHalf() {
        gameMatch.setGameState(GameState.END);
    }

    private void handleFirstPassFromTheMiddlePerformed() {
        gameMatch.setGameState(GameState.PLAYING);
    }

    private void handlePreparationForMatchStartAllPlayersReady() {
        gameMatch.setGameState(GameState.START_FROM_THE_MIDDLE);
    }

    private void handleCornerAllPlayersReady() {
        gameMatch.setGameState(GameState.CORNER_ANIMATION_READY);

    }

    private void handleCornerPerformed() {
        gameMatch.setGameState(GameState.PLAYING);
        gameMatch.handleCornerPerformed();
    }

    private void handleGoalEvent() {
        gameMatch.setGameState(GameState.GOAL);
        gameMatch.handleGoal();
    }

    private void handleAllPlayersReadyForStartFromMiddle() {
        logger.debug("Resuming game");
        gameMatch.setGameState(GameState.START_FROM_THE_MIDDLE);
    }

    private void handleCorner() {
        gameMatch.setGameState(GameState.CORNER);
        gameMatch.handleCorner();
    }

    private void handleResumeByGk() {
        gameMatch.setGameState(GameState.RESUME_BY_GK);
        gameMatch.handleResumeByGk();
    }


}
