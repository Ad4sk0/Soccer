package soccer.game.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.game.GameState;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import static org.mockito.Mockito.*;

class MatchEventsCheckerTest {

    @Mock
    GameMatch gameMatch;

    @Mock
    Ball ball;

    @Mock
    MatchEventsTransmitter matchEventsTransmitter;

    @Mock
    GamePlayer gamePlayer;

    MatchEventsChecker matchEventsChecker;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(gameMatch.getBall()).thenReturn(ball);
        when(gameMatch.getGameState()).thenReturn(GameState.PLAYING);
        when(gamePlayer.getFieldSite()).thenReturn(FieldSite.LEFT);
        when(ball.getPreviousOwner()).thenReturn(gamePlayer);
        when(ball.getSize()).thenReturn(Ball.BALL_SIZE);
        matchEventsChecker = new MatchEventsChecker(gameMatch);
    }

    @Test
    void shouldRecognizeGoalEvent() {
        when(ball.getX()).thenReturn(-10D);
        when(ball.getY()).thenReturn(PlayingField.FIELD_HEIGHT / 2);
        matchEventsChecker.checkMatchEvents();
        verify(gameMatch).produceEvent(new Event(EventTypes.GOAL));
    }

    @Test
    void shouldRecognizeCornerEvent() {
        double x = -10;
        double y = 10;
        when(ball.getX()).thenReturn(x);
        when(ball.getY()).thenReturn(y);
        when(ball.getPosition()).thenReturn(new Position(x, y));
        matchEventsChecker.checkMatchEvents();
        verify(gameMatch).produceEvent(new Event(EventTypes.CORNER));
        verify(gameMatch, never()).produceEvent(new Event(EventTypes.RESUME_BY_GK));
    }

    @Test
    void shouldRecognizeResumeByGkEvent() {
        double x = PlayingField.FIELD_WIDTH + 10;
        double y = 10;
        when(ball.getX()).thenReturn(x);
        when(ball.getY()).thenReturn(y);
        when(ball.getPosition()).thenReturn(new Position(x, y));
        matchEventsChecker.checkMatchEvents();
        verify(gameMatch).produceEvent(new Event(EventTypes.RESUME_BY_GK));
        verify(gameMatch, never()).produceEvent(new Event(EventTypes.CORNER));
    }

    @Test
    void shouldRecognizeOutEvent() {
        double x = PlayingField.FIELD_WIDTH / 2;
        double y = -10;
        when(ball.getX()).thenReturn(x);
        when(ball.getY()).thenReturn(y);
        when(ball.getPosition()).thenReturn(new Position(x, y));
        matchEventsChecker.checkMatchEvents();
        verify(gameMatch).produceEvent(new Event(EventTypes.OUT));
    }

    @Test
    void shouldRecognizeGoalPostHitEvent() {
        double x = PlayingField.LEFT_UPPER_POST.getX();
        double y = PlayingField.LEFT_UPPER_POST.getY();
        when(ball.getX()).thenReturn(x);
        when(ball.getY()).thenReturn(y);
        when(ball.getPosition()).thenReturn(new Position(x, y));
        matchEventsChecker.checkMatchEvents();
        verify(gameMatch).produceEvent(new Event(EventTypes.GOAL_POST_HITTED));
    }
}