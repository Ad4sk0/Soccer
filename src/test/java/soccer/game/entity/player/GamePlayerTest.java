package soccer.game.entity.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.player.Player;
import soccer.game.GameState;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.movement.GoalkeeperMoveStrategy;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.entity.player.movement.RandomMoveStrategy;
import soccer.game.entity.player.passing.RandomPassingStrategy;
import soccer.game.entity.player.shooting.NoShootStrategy;
import soccer.game.entity.player.shooting.RandomShootingStrategy;
import soccer.game.match.GameMatch;
import soccer.game.team.GameTeam;
import soccer.models.playingfield.PlayingField;
import soccer.models.positions.PlayingPosition;
import soccer.utils.Position;
import soccer.utils.Vector2d;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GamePlayerTest {

    GamePlayer gamePlayer;
    GamePlayer secondGamePlayer;
    int playerNumber = 5;
    int playerSpeed = 100;
    int playerEndurance = 100;
    int playerAcceleration = 100;
    @Mock
    GameMatch gameMatch;
    @Mock
    Player player;
    @Mock
    Ball ball;
    @Mock
    GameTeam gameTeam;

    @BeforeEach
    void initPlayer() {
        FormationPosition formationPosition = new FormationPosition();
        formationPosition.setPosition(PlayingPosition.CF);
        MockitoAnnotations.openMocks(this);
        when(gameMatch.getGameState()).thenReturn(GameState.PLAYING);
        when(gameMatch.getLeftSiteTeam()).thenReturn(gameTeam);
        when(gameMatch.getRightSiteTeam()).thenReturn(gameTeam);
        when(gameTeam.getOppositeTeam()).thenReturn(gameTeam);
        when(gameTeam.isRightSiteTeam()).thenReturn(true);
        when(player.getNumber()).thenReturn(playerNumber);
        when(player.getAssignedPosition()).thenReturn(formationPosition);
        when(player.getSpeed()).thenReturn(playerSpeed);
        when(player.getEndurance()).thenReturn(playerEndurance);
        when(player.getAcceleration()).thenReturn(playerAcceleration);
        when(ball.getPosition()).thenReturn(new Position(-20, -20));
        createNewPlayer();
    }

    void createNewPlayer() {
        gamePlayer = new GamePlayer(player, gameTeam, gameMatch, ball);
        gamePlayer.setBasePosition(PlayingField.CENTRE_CIRCLE_POSITION);
        gamePlayer.setMoveStrategy(new TestMoveStrategy());
        gamePlayer.setPassingStrategy(new TestPassStrategy());
        gamePlayer.setShootingStrategy(new TestShootStrategy());
        gamePlayer.setInitialPosition(gamePlayer.getBasePosition());
    }

    void createSecondPlayer() {
        secondGamePlayer = new GamePlayer(player, gameTeam, gameMatch, ball);
        Position secondPlayerPosition = new Position(gamePlayer.getPosition());
        secondPlayerPosition.setX(secondPlayerPosition.getX() + 300);
        secondGamePlayer.setBasePosition(secondPlayerPosition);
        secondGamePlayer.setMoveStrategy(new TestMoveStrategy());
        secondGamePlayer.setPassingStrategy(new TestPassStrategy());
        secondGamePlayer.setShootingStrategy(new TestShootStrategy());
        secondGamePlayer.setInitialPosition(secondGamePlayer.getBasePosition());
    }

    @Test
    void shouldHaveTheSameNumberAsPlayerEntity() {
        assertEquals(playerNumber, gamePlayer.getNumber());
    }

    @Test
    void shouldCorrectlyCalculateSprintSpeed() {
        assertEquals(playerSpeed, gamePlayer.getSprintSpeed()); // Assuming energy = 100
        double expectedPeaceKmPerH = 30;
        gamePlayer.sprint();
        makeNNumberOfMoves(400);
        assertEquals(expectedPeaceKmPerH, gamePlayer.getCurrentPieceKmPerHour(), 0.1);
    }

    @Test
    void shouldCorrectlyCalculateJoggingSpeed() {
        assertEquals(40, GamePlayer.BASE_JOG_SPEED);
        double expectedPeaceKmPerH = 12;
        gamePlayer.jog();
        makeNNumberOfMoves(400);
        assertEquals(expectedPeaceKmPerH, gamePlayer.getCurrentPieceKmPerHour(), 0.1);
    }

    @Test
    void shouldCorrectlyCalculateWalkingSpeed() {
        assertEquals(15, GamePlayer.BASE_WALK_SPEED);
        double expectedPeaceKmPerH = 4.5;
        gamePlayer.walk();
        makeNNumberOfMoves(400);
        assertEquals(expectedPeaceKmPerH, gamePlayer.getCurrentPieceKmPerHour(), 0.1);
    }

    @Test
    void shouldCalculateMaxSpeedBasedOnPlayerSpeedProperty() {
        int playerSpeedProp = 50;
        when(player.getSpeed()).thenReturn(playerSpeedProp);
        createNewPlayer();
        assertEquals(playerSpeedProp, gamePlayer.getMaxSpeed());
        double expectedPeaceKmPerH = 15;
        gamePlayer.sprint();
        makeNNumberOfMoves(400);
        assertEquals(expectedPeaceKmPerH, gamePlayer.getCurrentPieceKmPerHour(), 0.1);
    }

    @Test
    void testEnergyLose() {
        when(player.getEndurance()).thenReturn(75);
        Map<String, Double> energyLosses = new HashMap<>(Map.of(
                "sprint", 0d,
                "jog", 0d,
                "walk", 0d
        ));
        for (String key : energyLosses.keySet()) {
            double initEnergyLevel = gamePlayer.getEnergy();
            switch (key) {
                case "sprint" -> gamePlayer.sprint();
                case "jog" -> gamePlayer.jog();
                case "walk" -> gamePlayer.walk();
            }
            makeNNumberOfMoves(100);
            energyLosses.put(key, initEnergyLevel - gamePlayer.getEnergy());
        }
        assertTrue(energyLosses.get("sprint") > energyLosses.get("jog"));
        assertTrue(energyLosses.get("jog") > energyLosses.get("walk"));
        assertTrue(energyLosses.get("walk") > 0);
    }

    @Test
    void shouldAccelerate() {
        gamePlayer.sprint();
        double prevSpeed = gamePlayer.getCurrentSpeed();
        for (int i = 0; i < 5; i++) {
            makeNNumberOfMoves(10);
            assertTrue(prevSpeed < gamePlayer.getCurrentSpeed());
            prevSpeed = gamePlayer.getCurrentSpeed();
        }
    }

    @Test
    void shouldTurn() {
        Vector2d targetDirection = new Vector2d(1, 1);
        gamePlayer.turn(targetDirection);
        assertEquals(gamePlayer.getDirection(), targetDirection);
    }

    @Test
    void shouldTurnTowardsPosition() {
        Position targetPosition = gamePlayer.getPosition();
        targetPosition.setX(targetPosition.getX() + 100);
        gamePlayer.turnTowardsPosition(targetPosition);
        Vector2d expected = new Vector2d(1, 0);
        assertEquals(expected.getX(), gamePlayer.getDirection().getX(), 0.01);
        assertEquals(expected.getY(), gamePlayer.getDirection().getY(), 0.01);
    }

    @Test
    void ShouldDetectBallInRangeAndTakePossessionOfTheBall() {
        ball = new Ball(gameMatch);
        createNewPlayer();
        Position ballPosition = new Position(gamePlayer.getX() + 100, gamePlayer.getY());
        ball.setInitialPosition(ballPosition);
        gamePlayer.turnTowardsPosition(ballPosition);
        gamePlayer.sprint();
        assertFalse(gamePlayer.hasBall());
        for (int i = 0; i < 500; i++) {
            gamePlayer.makeMove();
            ball.makeMove();
            if (gamePlayer.hasBall()) {
                break;
            }
        }
        assertTrue(gamePlayer.hasBall());
    }

    @Test
    void shouldPassAccurately() {
        ball = new Ball(gameMatch);
        createNewPlayer();
        createSecondPlayer();
        Position secondPlayerInitialPosition = new Position(secondGamePlayer.getPosition());
        ball.setInitialPosition(gamePlayer.getPosition());
        gamePlayer.makeMove(); // Get ball
        gamePlayer.pass(secondGamePlayer, MovingEntity.MAX_ENTITY_SPEED);
        for (int i = 0; i < 500; i++) {
            gamePlayer.makeMove();
            secondGamePlayer.makeMove();
            ball.makeMove();
            if (secondGamePlayer.hasBall()) {
                break;
            }
        }
        assertTrue(secondGamePlayer.hasBall());
        assertEquals(ball.getOwner(), secondGamePlayer);
        assertEquals(ball.getOwner(), secondGamePlayer);
        // In this test second player should not go for incoming pass
        assertEquals(secondPlayerInitialPosition, secondGamePlayer.getPosition());
    }

    @Test
    void shouldGoForIncomingPass() {
        ball = new Ball(gameMatch);
        createNewPlayer();
        createSecondPlayer();
        Position secondPlayerInitialPosition = new Position(secondGamePlayer.getPosition());
        ball.setInitialPosition(gamePlayer.getPosition());
        secondGamePlayer.setMoveStrategy(new RandomMoveStrategy(secondGamePlayer));
        gamePlayer.makeMove(); // Get ball
        gamePlayer.pass(secondGamePlayer, MovingEntity.MAX_ENTITY_SPEED);
        verify(gameTeam).setPassingTarget(any(GamePlayer.class));
        for (int i = 0; i < 500; i++) {
            gamePlayer.makeMove();
            secondGamePlayer.makeMove();
            ball.makeMove();
            if (secondGamePlayer.hasBall()) {
                break;
            }
        }
        assertTrue(secondGamePlayer.hasBall());
        assertEquals(ball.getOwner(), secondGamePlayer);
        assertNotEquals(secondPlayerInitialPosition, secondGamePlayer.getPosition());
    }

    @Test
    void shouldShotAccurately() {
        ball = new Ball(gameMatch);
        createNewPlayer();
        ball.setInitialPosition(gamePlayer.getPosition());
        gamePlayer.makeMove();
        gamePlayer.shoot(PlayingField.LEFT_BOTTOM_POST, MovingEntity.MAX_ENTITY_SPEED);
        for (int i = 0; i < 500; i++) {
            gamePlayer.makeMove();
            ball.makeMove();
            if (ball.getPosition().getDistance(PlayingField.LEFT_BOTTOM_POST) < ball.getSize()) {
                break;
            }
        }
        assertTrue(ball.getPosition().getDistance(PlayingField.LEFT_BOTTOM_POST) < ball.getSize());
    }

    @Test
    void shouldKickTowardsTargetAccurately() {
        ball = new Ball(gameMatch);
        createNewPlayer();
        ball.setInitialPosition(gamePlayer.getPosition());
        gamePlayer.makeMove();
        gamePlayer.kickTowardsTarget(PlayingField.RIGHT_BOTTOM_CORNER, MovingEntity.MAX_ENTITY_SPEED);
        for (int i = 0; i < 500; i++) {
            gamePlayer.makeMove();
            ball.makeMove();
            if (ball.getPosition().equals(PlayingField.RIGHT_BOTTOM_CORNER)) {
                break;
            }
        }
        assertEquals(PlayingField.RIGHT_BOTTOM_CORNER, ball.getPosition());
    }

    @Test
    void shouldStartInIdleState() {
        assertEquals(GamePlayerState.IS_IDLE, gamePlayer.getPlayerState());
    }

    @Test
    void shouldHandlePlayingMatchState() {
        when(gameMatch.getGameState()).thenReturn(GameState.START_FROM_THE_MIDDLE);
        gamePlayer.setStartingPosition(PlayingField.CENTRE_CIRCLE_POSITION);
        gamePlayer.setMoveStrategy(new RandomMoveStrategy(gamePlayer));
        gamePlayer.makeMove();
        assertNotEquals(MoveStrategy.class, gamePlayer.getMoveStrategy().getClass());
        when(gameMatch.getGameState()).thenReturn(GameState.PLAYING);
        gamePlayer.makeMove();
        assertEquals(RandomMoveStrategy.class, gamePlayer.getMoveStrategy().getClass());
    }

    @Test
    void testDefaultStrategies() {
        gamePlayer = new GamePlayer(player, gameTeam, gameMatch, ball);
        assertFalse(gamePlayer.isGoalkeeper());
        assertEquals(RandomMoveStrategy.class, gamePlayer.getMoveStrategy().getClass());
        assertEquals(RandomPassingStrategy.class, gamePlayer.getPassingStrategy().getClass());
        assertEquals(RandomShootingStrategy.class, gamePlayer.getShootingStrategy().getClass());
    }

    @Test
    void testDefaultGoalKeeperStrategies() {
        FormationPosition formationPosition = new FormationPosition();
        formationPosition.setPosition(PlayingPosition.GK);
        when(player.getAssignedPosition()).thenReturn(formationPosition);
        gamePlayer = new GamePlayer(player, gameTeam, gameMatch, ball);
        assertTrue(gamePlayer.isGoalkeeper());
        assertEquals(GoalkeeperMoveStrategy.class, gamePlayer.getMoveStrategy().getClass());
        assertEquals(RandomPassingStrategy.class, gamePlayer.getPassingStrategy().getClass());
        assertEquals(NoShootStrategy.class, gamePlayer.getShootingStrategy().getClass());
    }

    private void makeNNumberOfMoves(int movesNumber) {
        for (int i = 0; i < movesNumber; i++) {
            gamePlayer.makeMove();
        }
    }
}