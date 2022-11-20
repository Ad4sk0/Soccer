package soccer.game.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.app.entities.match.Match;
import soccer.game.GameState;
import soccer.game.entity.ball.Ball;
import soccer.game.match.GameMatch;
import soccer.utils.Position;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MatchEventsHandlerTest {

    @Mock
    Match match;

    @Mock
    Ball ball;

    @Mock
    MatchEventsTransmitter matchEventsTransmitter;

    @Mock
    GameMatch gameMatch;
    @InjectMocks
    MatchEventsHandler matchEventsHandler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(ball.getPosition()).thenReturn(new Position());
    }

    @Test
    void shouldHandlePreparationForMatchStartReadyEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.PREPARATION_FOR_MATCH_START_ALL_PLAYERS_READY));
        verify(gameMatch).setGameState(GameState.START_FROM_THE_MIDDLE);
    }

    @Test
    void shouldHandleGoalAnimationAllPlayersReadyEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.GOAL_ANIMATION_ALL_PLAYERS_READY));
        verify(gameMatch).setGameState(GameState.START_FROM_THE_MIDDLE);
    }

    @Test
    void shouldHandleFirstPassPerformedEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.FIRST_PASS_FROM_THE_MIDDLE_PERFORMED));
        verify(gameMatch).setGameState(GameState.PLAYING);
    }

    @Test
    void shouldHandleCornerAllPlayersReadyEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.CORNER_ALL_PLAYERS_READY));
        verify(gameMatch).setGameState(GameState.CORNER_ANIMATION_READY);
    }

    @Test
    void shouldHandleCornerPerformedEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.CORNER_PERFORMED));
        verify(gameMatch).setGameState(GameState.PLAYING);
    }

    @Test
    void shouldHandleEndSecondHalfEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.END_SECOND_HALF));
        verify(gameMatch).setGameState(GameState.END);
    }


    @Test
    void shouldHandleGoalEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.GOAL));
        verify(gameMatch).setGameState(GameState.GOAL);
        verify(gameMatch).handleGoal();
    }

    @Test
    void shouldHandleCornerEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.CORNER));
        verify(gameMatch).setGameState(GameState.CORNER);
        verify(gameMatch).handleCorner();
    }

    @Test
    void shouldHandleResumeByGKEvent() {
        matchEventsHandler.receiveEvent(new Event(EventTypes.RESUME_BY_GK));
        verify(gameMatch).setGameState(GameState.RESUME_BY_GK);
        verify(gameMatch).handleResumeByGk();
    }
}