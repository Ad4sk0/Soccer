package soccer.models.playingfield;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import soccer.game.entity.player.GamePlayer;
import soccer.utils.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayingFieldUtilsTest {

    GamePlayer gamePlayer;

    @BeforeEach
    void setUp() {
        gamePlayer = mock(GamePlayer.class);
        when(gamePlayer.getFieldSite()).thenReturn(FieldSite.LEFT);
    }

    @ParameterizedTest
    @CsvSource({
            "0," + PlayingField.FIELD_WIDTH,
            PlayingField.FIELD_WIDTH + ",0",
            PlayingField.FIELD_WIDTH / 2 + "," + PlayingField.FIELD_WIDTH / 2,
            PlayingField.FIELD_WIDTH / 2 - 10 + "," + (PlayingField.FIELD_WIDTH / 2 + 10),
            "100," + (PlayingField.FIELD_WIDTH - 100)
    })
    void shouldMoveXToOtherHalf(double initialX, double expectedConvertedX) {
        double actualConvertedX = PlayingFieldUtils.moveXToOtherHalf(initialX);
        assertEquals(expectedConvertedX, actualConvertedX);
        assertEquals(initialX, PlayingFieldUtils.moveXToOtherHalf(actualConvertedX));
    }

    @Test
    void shouldMoveXToOtherHalfGivenPosition() {
        Position initialPosition = new Position(PlayingField.PENALTY_SPOT_LEFT);
        Position expectedPosition = new Position(PlayingField.PENALTY_SPOT_RIGHT);
        Position actualPosition = PlayingFieldUtils.moveXToOtherHalf(initialPosition);
        assertEquals(expectedPosition, actualPosition);
        assertEquals(initialPosition, PlayingFieldUtils.moveXToOtherHalf(actualPosition));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,true",
            "-1,0,false",
            "0,-1,false",
            PlayingField.FIELD_WIDTH / 2 + ",0,false",
            PlayingField.FIELD_WIDTH / 2 - 1 + ",0,true",
            PlayingField.FIELD_WIDTH / 2 + 1 + ",0,false",
    })
    void shouldRecognizeFriendlyTeamHalf(double x, double y, boolean expectedIsOnFriendlyHalf) {
        assertEquals(FieldSite.LEFT, gamePlayer.getFieldSite());
        when(gamePlayer.getPosition()).thenReturn(new Position(x, y));
        assertEquals(expectedIsOnFriendlyHalf, PlayingFieldUtils.isOnFriendlyTeamFieldHalf(gamePlayer));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,false",
            "-1,0,false",
            "0,-1,false",
            PlayingField.FIELD_WIDTH / 2 + ",0,false",
            PlayingField.FIELD_WIDTH / 2 - 1 + ",0,false",
            PlayingField.FIELD_WIDTH / 2 + 1 + ",0,true",
            PlayingField.FIELD_WIDTH + ",0,true",
            PlayingField.FIELD_WIDTH + ",-1,false",
            PlayingField.FIELD_WIDTH + 1 + ",0,false",
    })
    void shouldRecognizeOppositeTeamHalf(double x, double y, boolean expectedIsInFriendlyPenaltyArea) {
        assertEquals(FieldSite.LEFT, gamePlayer.getFieldSite());
        when(gamePlayer.getPosition()).thenReturn(new Position(x, y));
        assertEquals(expectedIsInFriendlyPenaltyArea, PlayingFieldUtils.isOnOppositeTeamFieldHalf(gamePlayer));
    }

    @ParameterizedTest
    @CsvSource({
            "0," + PlayingField.FIELD_HEIGHT / 2 + ",true",
            "-1," + PlayingField.FIELD_HEIGHT / 2 + ",false",
            "0," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",true",
            "0," + (PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT - 1) + ",false",
            "0," + PlayingField.PENALTY_AREA_BOTTOM_LINE_HEIGHT + ",true",
            "0," + (PlayingField.PENALTY_AREA_BOTTOM_LINE_HEIGHT + 1) + ",false",
            PlayingField.PENALTY_AREA_WIDTH + "," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",true",
            PlayingField.PENALTY_AREA_WIDTH + 1 + "," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",false",
            PlayingField.PENALTY_AREA_WIDTH + "," + (PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT - 1) + ",false",
    })
    void shouldRecognizeFriendlyPenaltyArea(double x, double y, boolean expectedIsOnOppositeHalf) {
        assertEquals(FieldSite.LEFT, gamePlayer.getFieldSite());
        when(gamePlayer.getPosition()).thenReturn(new Position(x, y));
        assertEquals(expectedIsOnOppositeHalf, PlayingFieldUtils.isInFriendlyTeamPenaltyArea(gamePlayer));
    }

    @ParameterizedTest
    @CsvSource({
            "0," + PlayingField.FIELD_HEIGHT / 2 + ",false",
            "-1," + PlayingField.FIELD_HEIGHT / 2 + ",false",
            "0," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",false",
            "0," + (PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT - 1) + ",false",
            PlayingField.FIELD_WIDTH + "," + PlayingField.FIELD_WIDTH / 2 + ",true",
            PlayingField.FIELD_WIDTH + 1 + "," + PlayingField.FIELD_WIDTH / 2 + ",false",
            PlayingField.FIELD_WIDTH + "," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",true",
            PlayingField.FIELD_WIDTH + "," + (PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT - 1) + ",false",
            PlayingField.FIELD_WIDTH + "," + PlayingField.PENALTY_AREA_BOTTOM_LINE_HEIGHT + ",true",
            PlayingField.FIELD_WIDTH + "," + (PlayingField.PENALTY_AREA_BOTTOM_LINE_HEIGHT + 1) + ",false",
            PlayingField.FIELD_WIDTH - PlayingField.PENALTY_AREA_WIDTH + "," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",true",
            PlayingField.FIELD_WIDTH - PlayingField.PENALTY_AREA_WIDTH + 1 + "," + PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + ",true",
            PlayingField.FIELD_WIDTH - PlayingField.PENALTY_AREA_WIDTH + "," + (PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT - 1) + ",false",

    })
    void shouldRecognizeOppositePenaltyArea(double x, double y, boolean expectedIsInOppositePenaltyArea) {
        assertEquals(FieldSite.LEFT, gamePlayer.getFieldSite());
        when(gamePlayer.getPosition()).thenReturn(new Position(x, y));
        assertEquals(expectedIsInOppositePenaltyArea, PlayingFieldUtils.isInOppositeTeamPenaltyArea(gamePlayer));
    }
}