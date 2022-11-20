package soccer.game.entity.player.animation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.game.GameState;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.PlayerTestUtils;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertFalse;

class GoToStartingPositionAnimationTest {

    GamePlayer gamePlayer;
    Position startingPosition;

    @BeforeEach
    void setUp() {
        gamePlayer = PlayerTestUtils.prepareGamePlayerForAnimationTest(GameState.GOAL);
        startingPosition = new Position(PlayingField.CENTRE_CIRCLE_POSITION);
        gamePlayer.setStartingPosition(startingPosition);
    }

    @Test
    void shouldGoToStartingPosition() {
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 500);
        assertEquals(startingPosition, gamePlayer.getPosition());
    }

    @Test
    void shouldIgnoreBallDuringAnimation() {
        Position ballPosition = new Position(300, PlayingField.FIELD_HEIGHT_HALF);
        startingPosition = new Position(200, PlayingField.FIELD_HEIGHT_HALF);
        gamePlayer.setStartingPosition(startingPosition);
        when(gamePlayer.getBall().getPosition()).thenReturn(ballPosition);
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 500);
        assertEquals(startingPosition, gamePlayer.getPosition());
        assertFalse(gamePlayer.hasBall());
        verify(gamePlayer.getBall(), never()).tackle(any(Position.class));
    }

}