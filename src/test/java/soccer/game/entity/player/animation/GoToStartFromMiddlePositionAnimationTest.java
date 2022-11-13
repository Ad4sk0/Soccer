package soccer.game.entity.player.animation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.game.GameState;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.PlayerTestUtils;
import soccer.game.team.TeamRole;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingFieldUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class GoToStartFromMiddlePositionAnimationTest {

    GamePlayer gamePlayer;

    @BeforeEach
    void setUp() {
        gamePlayer = PlayerTestUtils.prepareGamePlayerForAnimationTest(GameState.GOAL_ANIMATION);
    }

    @Test
    void shouldGoToStartFromTheMiddlePosition1LeftTeam() {
        shouldGoToStartFromTheMiddlePosition1(FieldSite.LEFT);
    }

    @Test
    void shouldGoToStartFromTheMiddlePosition2LeftTeam() {
        shouldGoToStartFromTheMiddlePosition2(FieldSite.LEFT);
    }

    @Test
    void shouldGoToStartFromTheMiddlePosition1RightTeam() {
        shouldGoToStartFromTheMiddlePosition1(FieldSite.RIGHT);
    }

    @Test
    void shouldGoToStartFromTheMiddlePosition2RightTeam() {
        shouldGoToStartFromTheMiddlePosition2(FieldSite.RIGHT);
    }

    void shouldGoToStartFromTheMiddlePosition1(FieldSite fieldSite) {
        gamePlayer.addRole(TeamRole.START_FROM_MIDDLE_1);
        setGamePlayerSite(fieldSite);
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 300);
        assertEquals(PlayingFieldUtils.getStartingFromMiddlePos1(gamePlayer), gamePlayer.getPosition());
    }

    void shouldGoToStartFromTheMiddlePosition2(FieldSite fieldSite) {
        gamePlayer.addRole(TeamRole.START_FROM_MIDDLE_2);
        setGamePlayerSite(fieldSite);
        PlayerTestUtils.makeNNumberOfGamePlayerMoves(gamePlayer, 300);
        assertEquals(PlayingFieldUtils.getStartingFromMiddlePos2(gamePlayer), gamePlayer.getPosition());
    }


    private void setGamePlayerSite(FieldSite fieldSite) {
        when(gamePlayer.getMatch().getStartFromTheMiddleTeamSite()).thenReturn(fieldSite);
        gamePlayer.setFieldSite(fieldSite);
    }

}