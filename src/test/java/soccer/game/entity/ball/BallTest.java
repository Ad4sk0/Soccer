package soccer.game.entity.ball;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.MovingEntity;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.PlayingField;
import soccer.utils.GeomUtils;
import soccer.utils.Position;
import soccer.utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertTrue;

class BallTest {

    final int ballSpeed = 50;
    Ball ball;
    Position initPosition = PlayingField.CENTRE_CIRCLE_POSITION;

    static List<Arguments> getGoalPosts() {
        List<Arguments> args = new ArrayList<>();
        for (Position pos : PlayingField.getGoalPosts()) {
            args.add(Arguments.of(pos));
        }
        return args;
    }

    static List<Arguments> getDifferentPositions() {

        List<Arguments> args = new ArrayList<>();

        // LEFT
        args.add(Arguments.of(new Position(0, 0)));
        args.add(Arguments.of(new Position(0, PlayingField.FIELD_HEIGHT / 2)));
        args.add(Arguments.of(new Position(0, PlayingField.FIELD_HEIGHT)));

        // RIGHT
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH, 0)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH, PlayingField.FIELD_HEIGHT / 2)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 2, PlayingField.FIELD_HEIGHT)));

        // UP
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 4, 0)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 2, 0)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 2 + PlayingField.FIELD_WIDTH / 4, 0)));

        // DOWN
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 4, PlayingField.FIELD_HEIGHT)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 2, PlayingField.FIELD_HEIGHT)));
        args.add(Arguments.of(new Position(PlayingField.FIELD_WIDTH / 2 + PlayingField.FIELD_WIDTH / 4, PlayingField.FIELD_HEIGHT)));

        return args;
    }

    @BeforeEach
    void initBallInTheMiddle() {
        GameMatch gameMatch = mock(GameMatch.class);
        when(gameMatch.getGameState()).thenReturn(GameState.PLAYING);
        ball = new Ball(gameMatch);
        ball.setInitialPosition(initPosition);
    }

    @Test
    void shouldInitInTheMiddle() {
        assertTrue(ball.getPosition().equals(initPosition));
    }

    @Test
    void shouldMove() {
        Position shootTarget = PlayingField.RIGHT_GOAL_POSITION;
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), shootTarget);
        ball.hit(direction, ballSpeed);
        makeNNumberOfMoves(50);
        assertNotEquals(ball.getPosition(), initPosition);
    }

    @Test
    void shouldConvertPxPerSecToKmPerSec() {
        ball.hit(new Vector2d(0, 0), MovingEntity.MAX_ENTITY_SPEED);
        double expected = 50.0; // Max Speed Of Ball
        assertEquals(expected, ball.getCurrentPieceKmPerHour(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("getDifferentPositions")
    void shouldCalculatePositionCorrectly(Position position) {
        int movesNumber = 10;
        int initialBallSpeed = 50;
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), position);

        // Put ball in motion
        ball.hit(direction, initialBallSpeed);

        // Calculate expected position
        double currentSpeed = initialBallSpeed;
        Position currentPosition = new Position(initPosition);
        for (int i = 0; i < movesNumber; i++) {
            ball.makeMove();
            double speedFactor = currentSpeed / (MovingEntity.MAX_ENTITY_SPEED / ball.getMaxRealSpeedInPxPerFrame());
            double expectedX = currentPosition.getX() + direction.getX() * speedFactor;
            double expectedY = currentPosition.getY() + direction.getY() * speedFactor;
            currentSpeed -= Ball.LOSE_SPEED_PER_FRAME_FACTOR;
            assertEquals(expectedX, ball.getX(), 0.001);
            assertEquals(expectedY, ball.getY(), 0.001);
            currentPosition.setX(expectedX);
            currentPosition.setY(expectedY);
        }
    }

    @Test
    void shouldStop() {
        Position shootTarget = PlayingField.RIGHT_GOAL_POSITION;
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), shootTarget);
        Position stopPosition;
        ball.hit(direction, ballSpeed);
        makeNNumberOfMoves(50);
        ball.stop();
        stopPosition = new Position(ball.getPosition());
        makeNNumberOfMoves(50);
        assertEquals(stopPosition, ball.getPosition());
    }

    @Test
    void shouldStopAfterGoal() {
        if (ball.match.getGameState() != GameState.PLAYING) {
            throw new IllegalArgumentException("Ball will stop only in playing state. Unable to conduct test");
        }
        Position shootTarget = PlayingField.RIGHT_GOAL_POSITION;
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), shootTarget);
        ball.hit(direction, MovingEntity.MAX_ENTITY_SPEED);
        while (!MoveValidator.isGoalLineExceeded(ball)) {
            makeNNumberOfMoves(1);
        }
        Position stopPosition = new Position(ball.getPosition());
        makeNNumberOfMoves(50);
        assertEquals(stopPosition, ball.getPosition());
    }

    @ParameterizedTest
    @MethodSource("getGoalPosts")
    void shouldBounceFromGoalPosts(Position post) {

        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), post);
        ball.hit(direction, MovingEntity.MAX_ENTITY_SPEED);

        double prevX = ball.getX();
        makeNNumberOfMoves(1);
        double currX;

        boolean ballReversed = false;
        boolean ballDidntMadeMoveForward = false;

        while (true) {
            currX = ball.getX();
            if (!ballReversed) {
                if (post.isOnLeftSite()) {
                    ballReversed = currX > prevX;
                } else {
                    ballReversed = currX < prevX;
                }
            }
            if (!ballDidntMadeMoveForward) {
                ballDidntMadeMoveForward = currX == prevX;
            }

            if (ballDidntMadeMoveForward && ballReversed) return;

            if (ball.getCurrentSpeed() == 0) {
                fail("Ball stopped before hit goal post");
            }

            // Move the Ball
            prevX = currX;
            makeNNumberOfMoves(1);
        }
    }

    @Test
    void shouldStopAfterCorner() {
        if (ball.match.getGameState() != GameState.PLAYING) {
            throw new IllegalArgumentException("Ball will stop only in playing state. Unable to conduct test");
        }
        Position shootTarget = new Position(0, PlayingField.FIELD_HEIGHT / 4);
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), shootTarget);
        Position stopPosition;
        ball.hit(direction, MovingEntity.MAX_ENTITY_SPEED);
        while (!MoveValidator.isCornerLineExceeded(ball)) {
            if (ball.getCurrentSpeed() == 0) {
                fail("Ball stopped before corner");
            }
            makeNNumberOfMoves(1);
        }
        stopPosition = new Position(ball.getPosition());
        makeNNumberOfMoves(50);
        assertEquals(stopPosition, ball.getPosition());
    }

    @Test
    void shouldStopAfterOut() {
        if (ball.match.getGameState() != GameState.PLAYING) {
            throw new IllegalArgumentException("Ball will stop only in playing state. Unable to conduct test");
        }
        Position shootTarget = new Position(PlayingField.FIELD_HEIGHT / 2, 0);
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), shootTarget);
        Position stopPosition;
        ball.hit(direction, MovingEntity.MAX_ENTITY_SPEED);
        while (!MoveValidator.isHorizontalSideLineExceeded(ball)) {
            if (ball.getCurrentSpeed() == 0) {
                fail("Ball stopped before out");
            }
            makeNNumberOfMoves(1);
        }
        stopPosition = new Position(ball.getPosition());
        makeNNumberOfMoves(50);
        assertEquals(stopPosition, ball.getPosition());
    }

    @Test
    void shouldTackle() {
        int movesNumber = 100;
        double y = 100;
        Position position = new Position(0, y);
        for (int i = 0; i < movesNumber; i++) {
            position.setY(i);
            ball.tackle(position);
            assertEquals(position, ball.getPosition());
        }
    }

    @Test
    void shouldAnimateTowardsMiddle() {
        ball.hit(new Vector2d(-1, 0), MovingEntity.MAX_ENTITY_SPEED);
        makeNNumberOfMoves(500);
        assertFalse(ball.isInTheMiddle());
        when(ball.match.getGameState()).thenReturn(GameState.GOAL_ANIMATION);
        Position targetPosition = PlayingField.CENTRE_CIRCLE_POSITION;
        ball.setAnimationTarget(targetPosition);
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), targetPosition);
        ball.hit(direction, MovingEntity.MAX_ENTITY_SPEED);
        makeNNumberOfMoves(800);
        assertEquals(targetPosition, ball.getPosition());
        assertTrue(ball.isInTheMiddle());
    }

    @Test
    void shouldAnimateTowardsCorner() {
        Position targetPosition = PlayingField.LEFT_UPPER_CORNER;
        when(ball.match.getGameState()).thenReturn(GameState.CORNER_ANIMATION);
        when(ball.match.getCornerPerformingPosition()).thenReturn(targetPosition);
        ball.setAnimationTarget(targetPosition);
        Vector2d direction = GeomUtils.calculateDirectionTowardsPosition(ball.getPosition(), targetPosition);
        Position playerMock = new Position(initPosition);
        while (!playerMock.equals(targetPosition)) {
            playerMock.addVector(direction);
            ball.tackle(playerMock);
        }
        assertEquals(targetPosition, ball.getPosition());
        assertTrue(ball.isOnCurrentCornerPosition());
    }

    @Test
    void shouldThrowErrorIfSpeedSetToIncorrectValue() {
        Vector2d direction = new Vector2d(0, 0);
        assertThrows(IllegalArgumentException.class, () -> ball.hit(direction, -1));
        assertThrows(IllegalArgumentException.class, () -> ball.hit(direction, 101));
    }

    @Test
    void shouldBounceOnX() {
        ball.hit(new Vector2d(1, 0), MovingEntity.MAX_ENTITY_SPEED);
        makeNNumberOfMoves(10);
        ball.bounceOnX();
        makeNNumberOfMoves(30);
        assertTrue(ball.getX() < initPosition.getX()); //
    }

    @Test
    void shouldBounceOnY() {
        ball.hit(new Vector2d(0, -1), MovingEntity.MAX_ENTITY_SPEED);
        makeNNumberOfMoves(10);
        ball.bounceOnY();
        makeNNumberOfMoves(30);
        assertTrue(ball.getY() > initPosition.getY());
    }

    private void makeNNumberOfMoves(int movesNumber) {
        for (int i = 0; i < movesNumber; i++) {
            ball.makeMove();
        }
    }
}