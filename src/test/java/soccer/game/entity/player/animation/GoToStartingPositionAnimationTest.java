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

class GoToBasePositionAnimationTest {

    GamePlayer gamePlayer;
    Position basePosition;

    @BeforeEach
    void setUp() {
        gamePlayer = PlayerTestUtils.prepareGamePlayerForAnimationTest(GameState.GOAL);
        basePosition = new Position(50, 50);
        gamePlayer.setBasePosition(basePosition);
    }

    @Test
    void shouldGoToBasePosition() {
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 500);
        assertEquals(basePosition, gamePlayer.getPosition());
    }

    @Test
    void shouldIgnoreBallDuringAnimation() {
        Position ballPosition = new Position(300, PlayingField.FIELD_HEIGHT_HALF);
        basePosition = new Position(200, PlayingField.FIELD_HEIGHT_HALF);
        gamePlayer.setBasePosition(basePosition);
        when(gamePlayer.getBall().getPosition()).thenReturn(ballPosition);
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 500);
        assertEquals(basePosition, gamePlayer.getPosition());
        assertFalse(gamePlayer.hasBall());
        verify(gamePlayer.getBall(), never()).tackle(any(Position.class));
    }

}