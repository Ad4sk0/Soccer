package soccer.game.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.app.entities.match.Match;
import soccer.app.entities.team.Team;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.MovingEntity;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.utils.GeomUtils;
import soccer.utils.Position;
import soccer.utils.Vector2d;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;

class GameMatchTest {

    @Mock
    Match match;
    Team team1;
    Team team2;

    GameMatch gameMatch;

    private static Stream<Arguments> provideTestCasesForShouldHandleCorner() {
        return Stream.of(
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(0, PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.RIGHT,
                        PlayingField.LEFT_UPPER_CORNER
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(0, PlayingField.FIELD_HEIGHT / 2 + PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.RIGHT,
                        PlayingField.LEFT_BOTTOM_CORNER
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(PlayingField.FIELD_WIDTH, PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.LEFT,
                        PlayingField.RIGHT_UPPER_CORNER
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(PlayingField.FIELD_WIDTH, PlayingField.FIELD_HEIGHT / 2 + PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.LEFT,
                        PlayingField.RIGHT_BOTTOM_CORNER
                )
        );
    }

    private static Stream<Arguments> provideTestCasesForShouldHandleResumeByGk() {
        return Stream.of(
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(0, PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.LEFT
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(0, PlayingField.FIELD_HEIGHT / 2 + PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.LEFT
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(PlayingField.FIELD_WIDTH, PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.RIGHT
                ),
                Arguments.of(
                        GeomUtils.calculateDirectionTowardsPosition(PlayingField.CENTRE_CIRCLE_POSITION, new Position(PlayingField.FIELD_WIDTH, PlayingField.FIELD_HEIGHT / 2 + PlayingField.FIELD_HEIGHT / 4)),
                        FieldSite.RIGHT
                )
        );
    }

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        team1 = createTeam();
        team2 = createTeam();
        when(match.getTeamHome()).thenReturn(team1);
        when(match.getTeamAway()).thenReturn(team2);
        gameMatch = new GameMatch(match);
        gameMatch.init();
    }

    private Team createTeam() {
        Team team = new Team();
        team.initDefault();
        return team;
    }

    @Test
    void shouldInitialize() {
        assertEquals(GameState.INITIALIZED, gameMatch.getGameState());
        assertNotNull(gameMatch.getBall());
        assertEquals(2, gameMatch.getGameTeams().size());
        assertEquals(team1, gameMatch.getLeftSiteTeam().getTeam());
        assertEquals(team2, gameMatch.getRightSiteTeam().getTeam());
        assertEquals(22, gameMatch.getAllPlayingPlayers().size());
        assertEquals(MatchHalf.FIRST_HALF, gameMatch.getCurrentHalf());
        assertTrue(gameMatch.isPaused());
    }

    @Test
    void shouldStart() {
        gameMatch.startMatch();
        assertFalse(gameMatch.isPaused());
        gameMatch.update();
        assertEquals(GameState.START_FROM_THE_MIDDLE_ANIMATION, gameMatch.getGameState());
    }

    @Test
    void shouldUpdateTime() {
        gameMatch.startMatch();
        assertFalse(gameMatch.isPaused());
        makeNNumberOfUpdates(60);
        assertEquals(1, gameMatch.getCurrentHalfDurationInSeconds());
        makeNNumberOfUpdates(60);
        assertEquals(2, gameMatch.getCurrentHalfDurationInSeconds());
        makeNNumberOfUpdates(60);
        assertEquals(3, gameMatch.getCurrentHalfDurationInSeconds());
        assertEquals("00:03", gameMatch.getMatchDurationString());
    }

    @Test
    void shouldChangeHalf() {
        assumeTrue(GameMatch.MATCH_HALF_DURATION_IN_MIN == 3);
        gameMatch.startMatch();
        makeNNumberOfUpdates(GameMatch.MATCH_HALF_DURATION_IN_MIN * 60 * 60 + 1);
        assertEquals(MatchHalf.SECOND_HALF, gameMatch.getCurrentHalf());
        assertEquals(team2, gameMatch.getLeftSiteTeam().getTeam());
        assertEquals(team1, gameMatch.getRightSiteTeam().getTeam());
        assertEquals(0, gameMatch.getCurrentHalfDurationInSeconds());
    }

    @ParameterizedTest
    @ValueSource(strings = {"leftSite", "rightSite"})
    void shouldHandleGoal(String site) {
        int ballXDirection = -1;
        if (site.equals("rightSite")) {
            ballXDirection = 1;
        }
        gameMatch.getBall().hit(new Vector2d(ballXDirection, 0), MovingEntity.MAX_ENTITY_SPEED);
        int maxMovesN = 1000;
        int movesN = 0;
        while (!MoveValidator.isGoalLineExceeded(gameMatch.getBall())) {
            gameMatch.getBall().makeMove();
            movesN++;
            if (movesN > maxMovesN) {
                fail("Unable to move ball inside " + site + " goal");
            }
        }
        assertEquals(0, gameMatch.getLeftSiteTeam().getScore());
        assertEquals(0, gameMatch.getRightSiteTeam().getScore());
        gameMatch.setGameState(GameState.GOAL_ANIMATION);
        gameMatch.handleGoal();
        if (site.equals("leftSite")) {
            assertEquals(FieldSite.RIGHT, gameMatch.getLastGoalScoredBy());
            assertEquals(FieldSite.LEFT, gameMatch.getStartFromTheMiddleTeamSite());
            assertEquals(0, gameMatch.getLeftSiteTeam().getScore());
            assertEquals(1, gameMatch.getRightSiteTeam().getScore());
        } else {
            assertEquals(FieldSite.LEFT, gameMatch.getLastGoalScoredBy());
            assertEquals(FieldSite.RIGHT, gameMatch.getStartFromTheMiddleTeamSite());
            assertEquals(1, gameMatch.getLeftSiteTeam().getScore());
            assertEquals(0, gameMatch.getRightSiteTeam().getScore());
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForShouldHandleCorner")
    void shouldHandleCorner(Vector2d ballDirection, FieldSite expectedFieldSite, Position expectedCornerPosition) {
        gameMatch.getBall().hit(ballDirection, MovingEntity.MAX_ENTITY_SPEED);
        int maxMovesN = 1000;
        int movesN = 0;
        while (!MoveValidator.isCornerLineExceeded(gameMatch.getBall())) {
            gameMatch.getBall().makeMove();
            movesN++;
            if (movesN > maxMovesN) {
                fail("Unable to move ball into corner ");
            }
        }
        gameMatch.setGameState(GameState.CORNER_ANIMATION);
        gameMatch.handleCorner();
        assertEquals(expectedFieldSite, gameMatch.getCornerPerformingFieldSite());
        assertEquals(expectedCornerPosition, gameMatch.getCornerPerformingPosition());
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForShouldHandleResumeByGk")
    void shouldHandleResumeByGk(Vector2d ballDirection, FieldSite expectedFieldSite) {
        gameMatch.getBall().hit(ballDirection, MovingEntity.MAX_ENTITY_SPEED);
        int maxMovesN = 1000;
        int movesN = 0;
        while (!MoveValidator.isCornerLineExceeded(gameMatch.getBall())) {
            gameMatch.getBall().makeMove();
            movesN++;
            if (movesN > maxMovesN) {
                fail("Unable to move ball into corner ");
            }
        }
        gameMatch.setGameState(GameState.RESUME_BY_GK_ANIMATION);
        gameMatch.handleResumeByGk();
        assertEquals(expectedFieldSite, gameMatch.getResumeByGkFieldSite());
    }

    @Test
    void shouldHandleCornerPerformed() {
        gameMatch.handleCornerPerformed();
        assertNull(gameMatch.getCornerPerformingFieldSite());
        assertNull(gameMatch.getCornerPerformingPosition());
    }

    private void makeNNumberOfUpdates(int movesNumber) {
        for (int i = 0; i < movesNumber; i++) {
            gameMatch.update();
        }
    }
}