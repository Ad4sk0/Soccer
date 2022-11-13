package soccer.game.entity.player;

import org.jboss.logging.Logger;
import soccer.app.entities.player.Player;
import soccer.app.entities.player.PlayerStats;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.animation.corner.CornerAnimation;
import soccer.game.entity.player.animation.endmatch.GoToLockerRoomAnimation;
import soccer.game.entity.player.animation.goal.AfterGoalAnimation;
import soccer.game.entity.player.animation.matchbreak.MoveToOtherHalfAfterHalfAnimation;
import soccer.game.entity.player.animation.resumebygk.ResumeByGkAnimation;
import soccer.game.entity.player.animation.startfrommiddle.StartFromMiddleAnimation;
import soccer.game.entity.player.movement.GoalkeeperMoveStrategy;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.entity.player.movement.NoMoveStrategy;
import soccer.game.entity.player.movement.RandomMoveStrategy;
import soccer.game.entity.player.passing.NoPassStrategy;
import soccer.game.entity.player.passing.PassingStrategy;
import soccer.game.entity.player.passing.RandomPassingStrategy;
import soccer.game.entity.player.shooting.NoShootStrategy;
import soccer.game.entity.player.shooting.RandomShootingStrategy;
import soccer.game.entity.player.shooting.ShootingStrategy;
import soccer.game.match.GameMatch;
import soccer.game.team.TeamRole;
import soccer.models.playingfield.FieldSite;
import soccer.models.positions.PlayingPosition;
import soccer.utils.GeomUtils;
import soccer.utils.Position;
import soccer.utils.Vector2d;

import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePlayer extends MovingEntity {

    // 0 - no move
    // 4.5 km/h - Walk Speed
    // 12km/h - Jogging speed
    // 30km/h - Max sprint speed - 8,3(3) m/s - 83,3(3) px/s - 1,39 px/s/fps
    public static final int BASE_WALK_SPEED = 15;
    public static final int BASE_JOG_SPEED = 40;
    private static final boolean CAN_TAKE_BALL_FROM_FRIENDLY_PLAYER = false;
    private final Random random = new Random();
    private final Ball ball;
    @JsonbTransient
    private final GameMatch match;
    private final Logger logger = Logger.getLogger(getClass());
    private final double maxSpeed;
    Player player;
    ArrayList<TeamRole> roles = new ArrayList<>();
    private FieldSite fieldSite;
    private PlayingPosition playingPosition;
    private int number;
    private Position basePosition;
    private Position resumeByFriendlyGkPosition;
    private Position resumeByOppositeGkPosition;
    private Position cornerOnFriendlySitePosition;
    private Position cornerOnOppositeSitePosition;
    private ShootingStrategy shootingStrategy;
    private MoveStrategy moveStrategy;
    private PassingStrategy passingStrategy;
    private MoveStrategy previousPlayingMoveStrategy;
    private PassingStrategy previousPlayingPassingStrategy;
    private ShootingStrategy previousPlayingShootingStrategy;
    private boolean hasBall = false;
    private Position ballRelativePosition;
    private boolean isAwaitingPass = false;
    private PlayerState playerState = PlayerState.IS_IDLE;
    private List<GamePlayer> myTeamPlayers;
    private List<GamePlayer> opposedTeamPlayers;
    private GameState previousGameState;
    private boolean isGoalkeeper = false;
    private double energy;
    private double endurance;
    private MoveState moveState = MoveState.STOP;
    private double defaultEnergyLoss;

    public GamePlayer(Player player, GameMatch match, Ball ball) {
        super(12);
        this.ball = ball;
        this.match = match;
        this.player = player;
        if (player.getAssignedPosition() != null) {
            this.playingPosition = player.getAssignedPosition().getPosition();
        }
        this.number = player.getNumber();
        this.maxSpeed = player.getSpeed();
        this.energy = 100;
        setBasePlayerStrategies();
        this.previousGameState = GameState.PLAYING;
        if (playingPosition == PlayingPosition.GK) {
            this.isGoalkeeper = true;
        }
        calculateDefaultEnergyLoss();
    }

    @Override
    public void stop() {
        super.stop();
        moveState = MoveState.STOP;
    }

    private double calculateMaxSpeedLoss() {
        return (100 - energy) * 0.2;
    }

    private double getCurrentMaxSpeed() {
        return maxSpeed - calculateMaxSpeedLoss();
    }

    public double getSprintSpeed() {
        return getCurrentMaxSpeed();
    }

    public double getJogSpeed() {
        return Math.min(BASE_JOG_SPEED, getCurrentMaxSpeed());
    }

    public double getWalkSpeed() {
        return Math.min(BASE_WALK_SPEED, getCurrentMaxSpeed());
    }

    private double calculateAccelerationValue(double delta) {
        final int FPS = 60;
        return delta / FPS * player.getAcceleration() * 0.01;
    }

    public void handleSpeed() {
        double targetSpeed = getTargetSpeed();
        double delta = targetSpeed - currentSpeed;
        double accelerationValue = calculateAccelerationValue(delta);
        increaseOrDecreaseSpeed(accelerationValue);
    }

    private void increaseOrDecreaseSpeed(double accelerationValue) {
        currentSpeed = Math.min(currentSpeed + accelerationValue, getCurrentMaxSpeed());
    }

    private double getTargetSpeed() {
        switch (moveState) {
            case SPRINT -> {
                return getSprintSpeed();
            }
            case JOG -> {
                return getJogSpeed();
            }
            case WALK -> {
                return getWalkSpeed();
            }
            case STOP -> {
                return 0;
            }
        }
        throw new IllegalStateException("Unable to handle move state " + moveState);
    }

    public void sprint() {
        moveState = MoveState.SPRINT;
    }

    public void jog() {
        moveState = MoveState.JOG;
    }

    public void walk() {
        moveState = MoveState.WALK;
    }

    public void calculateDefaultEnergyLoss() {
        int planedFramesNumber = GameMatch.PLANNED_MATCH_DURATION_IN_SEC * 60;
        double enduranceFactor = 1 - (player.getEndurance() - 50) / 100.0; // 0 = 1.5, 50 = 1, 100 = 0.5
        defaultEnergyLoss = 100.0 / planedFramesNumber * (enduranceFactor);
    }

    public void handleEnergyLoss() {
        switch (moveState) {
            case SPRINT -> energy -= defaultEnergyLoss;
            case JOG -> energy -= defaultEnergyLoss / 2;
            case WALK -> energy -= defaultEnergyLoss / 4;
            case STOP -> energy -= defaultEnergyLoss / 8;
            default -> throw new IllegalStateException("Unable to handle move state " + moveState);
        }
    }

    private void setBasePlayerStrategies() {
        if (playingPosition == PlayingPosition.GK) {
            shootingStrategy = new NoShootStrategy(this);
            moveStrategy = new GoalkeeperMoveStrategy(this);
        } else {
            shootingStrategy = new RandomShootingStrategy(this);
            moveStrategy = new RandomMoveStrategy(this);
        }
        passingStrategy = new RandomPassingStrategy(this);

        previousPlayingMoveStrategy = moveStrategy;
        previousPlayingPassingStrategy = passingStrategy;
        previousPlayingShootingStrategy = shootingStrategy;
    }

    private void setBasePlayerAnimationStrategies() {
        shootingStrategy = new NoShootStrategy(this);
        moveStrategy = new NoMoveStrategy(this);
        passingStrategy = new NoPassStrategy(this);
    }

    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }

    @Override
    public void makeMove() {

        // Reset previous values
        velocity.setTo0();

        // What happened to the player
        handlePlayerEvents();

        // Change Player Strategies based on match events
        handleMatchEvents();

        // Change Player States
        handlePlayerState();

        // Let movement strategy choose direction and speed
        considerMoving();

        // Adjust speed to current move state
        handleSpeed();

        // Make Move
        move();

        // Correct the move if is invalid
        correctMove();

        handleEnergyLoss();

        // After the move is done - check if ball is in range and update ball position
        interactWithBall();

        // Player is awaiting incoming pass
        handleIncomingPass();

        if (!hasBall) return;

        handleBallDefence();

        considerPassing();

        considerShooting();

        driveTheBall();
    }

    @Override
    public float getMaxRealSpeedInPxPerFrame() {
        // 0 - no move
        // ~ 5km/h - walk
        // ~ 30km/h - max sprint speed - 8,3(3) m/s - 83,3(3) px/s - 1,39 px/s/fps
        return 1.39f;
    }

    private void handleMatchEvents() {
        if (match.getGameState().equals(previousGameState)) return;

        // Save playing strategy and leave ball for animation
        if (match.getGameState() != GameState.PLAYING && previousGameState == GameState.PLAYING) {
            previousPlayingMoveStrategy = moveStrategy;
            previousPlayingPassingStrategy = passingStrategy;
            previousPlayingShootingStrategy = shootingStrategy;
        }

        // Set do nothing for all players first
        if (match.getGameState() != GameState.PLAYING) {
            setBasePlayerAnimationStrategies();
            leaveBall();
            playerState = PlayerState.IS_PERFORMING_ANIMATION;
        }

        // Change move strategy based on game state
        switch (match.getGameState()) {
            case PLAYING -> resetToPreviousPlayingStrategy();
            case START_FROM_THE_MIDDLE_ANIMATION -> startAnimation(new StartFromMiddleAnimation(this));
            case GOAL_ANIMATION -> startAnimation(new AfterGoalAnimation(this));
            case RESUME_BY_GK_ANIMATION -> startAnimation(new ResumeByGkAnimation(this));
            case CORNER_ANIMATION -> startAnimation(new CornerAnimation(this));
            case BREAK -> startAnimation(new MoveToOtherHalfAfterHalfAnimation(this));
            case END -> startAnimation(new GoToLockerRoomAnimation(this));
        }

        previousGameState = match.getGameState();
    }

    private void startAnimation(MoveStrategy moveStrategy) {
        playerState = PlayerState.IS_PERFORMING_ANIMATION;
        setMoveStrategy(moveStrategy);
    }

    private void resetToPreviousPlayingStrategy() {
        moveStrategy = previousPlayingMoveStrategy;
        passingStrategy = previousPlayingPassingStrategy;
        shootingStrategy = previousPlayingShootingStrategy;
    }

    private void handlePlayerState() {
        // TODO
    }

    private void handleBallDefence() {
        // TODO
    }

    private void considerMoving() {
        moveStrategy.handleMovement();
    }

    private void correctMove() {
        // Player can go out of field during animations
        if (match.getGameState() != GameState.PLAYING) return;

        // Don't go out of field during playing
        if (MoveValidator.isHorizontalSideLineExceeded(this) || MoveValidator.isVerticalSideLineExceeded(this)) {
            undoMove();
        }
    }

    private void handlePlayerEvents() {
        // Ball Loss
        if (justLostTheBall()) {
            hasBall = false;
        }
    }

    private void handleIncomingPass() {
        if (playerState != PlayerState.IS_AWAITING_PASS) return;

        if (!ball.isFree()) {
            playerState = PlayerState.IS_PLAYING;
        }
    }

    private void considerPassing() {
        if (match.getGameState() != GameState.PLAYING) return;
        if (!hasBall) return;
        passingStrategy.handlePassing();
    }

    public void considerShooting() {
        if (match.getGameState() != GameState.PLAYING) return;
        if (!hasBall) return;
        shootingStrategy.handleShooting();
    }

    private void tryTackleBallFromOpponent() {
        if (random.nextInt(0, 100) > 95) {
            takePossessionOfTheBall();
        }
    }

    private void interactWithBall() {
        if (match.getGameState() != GameState.PLAYING) {
            return;
        }

        if (ball.getOwner() != this && ball.getPreviousOwner() == this) {
            return;
        }

        // Tackle ball
        if (isBallInRange()) {
            if (ball.isFree()) {
                takePossessionOfTheBall();
            } else if (isOppositeTeam(ball.getOwner())) {
                tryTackleBallFromOpponent();
            } else if (CAN_TAKE_BALL_FROM_FRIENDLY_PLAYER) {
                takePossessionOfTheBall();
            }
        }
    }

    public void pass(GamePlayer otherPlayer, int ballSpeed) {
        logger.debug("Player " + number + " passes to " + otherPlayer.getNumber());
        kickTowardsTarget(otherPlayer.getPosition(), ballSpeed);
        otherPlayer.setPlayerState(PlayerState.IS_AWAITING_PASS);
    }

    public void shoot(Position target, int ballSpeed) {
        kickTowardsTarget(target, ballSpeed);
    }

    private void performKick(Vector2d kickDirection, int ballSpeed) {
        ball.hit(kickDirection, ballSpeed);
        hasBall = false;
        ball.setOwner(null);
        ball.setPreviousOwner(this);
    }

    public void kickTowardsTarget(Position target, int ballSpeed) {
        if (!hasBall) {
            throw new UnsupportedOperationException("Kick requested by player " + number + " however player has no ball. Possible Error");
        }
        Vector2d targetDirection = GeomUtils.calculateDirectionTowardsPosition(position, target);
        performKick(targetDirection, ballSpeed);
    }

    public void takePossessionOfTheBall() {
        logger.debug("Player " + number + " takes possession of the ball");
        ball.stop();
        hasBall = true;
        ballRelativePosition = new Position(ball.getX() - position.getX(), ball.getY() - position.getY());
        ball.setOwner(this);
        ball.setPreviousOwner(this);
    }

    private void leaveBall() {
        if (!hasBall) return;
        ball.stop();
        hasBall = false;
        ballRelativePosition = null;
        ball.setOwner(null);
        ball.setPreviousOwner(this);
    }

    private void driveTheBall() {
        if (!hasBall) return;
        correctBallPositionToFrontOfPlayer(ballRelativePosition);
        Position ballNewPosition = Position.add(position, ballRelativePosition);
        ball.tackle(ballNewPosition);
    }

    private void correctBallPositionToFrontOfPlayer(Position ballRelativePosition) {
        // Do nothing if player doesn't move
        if (direction.length() == 0) {
            return;
        }

        // Distance from player to ball (It can be smaller)
        double dist = 1;

        // Determine the angle in which the player moves
        double targetAngle = GeomUtils.calculateAngleTowardsPosition(position, Position.add(position, new Position(direction.getX(), direction.getY())));

        // Move the ball into front of the player - with max angle value at one frame
        double prevAngle = GeomUtils.calculateAngleTowardsPosition(position, ball.getPosition());
        double maxAngleChange = Math.PI / 180;
        double angleDiff = Math.abs(targetAngle - prevAngle);

        // Calculate new ball angle relative to player position
        double angle = prevAngle;
        if (targetAngle > prevAngle) {
            angle += Math.min(maxAngleChange, angleDiff);
        } else {
            angle -= Math.min(maxAngleChange, angleDiff);
        }

        // Calculate new ball relative position - on the player radius and correct angle
        double newX = Math.cos(angle) * dist;
        double newY = Math.sin(angle) * dist;

        ballRelativePosition.setX(newX);
        ballRelativePosition.setY(newY);
    }

    public void turn(Vector2d direction) {
        setDirection(direction);
    }

    public void turnTowardsPosition(Position targetPosition) {
        turn(GeomUtils.calculateDirectionTowardsPosition(position, targetPosition));
    }

    public boolean isBallInRange() {
        double dist = position.getDistance(ball.getPosition());
        return dist < size + ball.getSize();
    }

    private boolean justLostTheBall() {
        return hasBall && ball.getOwner() != this || ball.getOwner() == this && !hasBall;
    }

    private boolean isOppositeTeam(GamePlayer player) {
        return player.getFieldSite() != fieldSite;
    }

    private boolean isFriendlyTeam(GamePlayer player) {
        return player.getFieldSite() == fieldSite;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayingPosition getPlayingPosition() {
        return playingPosition;
    }

    public void setPlayingPosition(PlayingPosition playingPosition) {
        this.playingPosition = playingPosition;
    }

    public FieldSite getFieldSite() {
        return fieldSite;
    }

    public void setFieldSite(FieldSite fieldSite) {
        if (fieldSite == FieldSite.LEFT) {
            direction = new Vector2d(1, random.nextDouble(-1, 1));
        } else {
            direction = new Vector2d(-1, random.nextDouble(-1, 1));
        }
        this.fieldSite = fieldSite;
    }

    public boolean hasBall() {
        return hasBall;
    }

    public void setHasBall(boolean hasBall) {
        this.hasBall = hasBall;
    }

    public List<GamePlayer> getMyTeamPlayers() {
        return myTeamPlayers;
    }

    public void setMyTeamPlayers(List<GamePlayer> myTeamPlayers) {
        // Keep reference to other team players only
        List<GamePlayer> myTeamPlayersFiltered = new ArrayList<>();
        for (GamePlayer gamePlayer : myTeamPlayers) {
            if (!gamePlayer.equals(this)) {
                myTeamPlayersFiltered.add(gamePlayer);
            }
        }
        this.myTeamPlayers = myTeamPlayersFiltered;
    }

    public List<GamePlayer> getOpposedTeamPlayers() {
        return opposedTeamPlayers;
    }

    public void setOpposedTeamPlayers(List<GamePlayer> opposedTeamPlayers) {
        this.opposedTeamPlayers = opposedTeamPlayers;
    }

    public Position getBasePosition() {
        return basePosition;
    }

    public void setBasePosition(Position basePosition) {
        this.basePosition = basePosition;
    }

    public boolean isAwaitingPass() {
        return isAwaitingPass;
    }

    public void setAwaitingPass(boolean isAwaitingPass) {
        this.isAwaitingPass = isAwaitingPass;
    }

    public GameMatch getMatch() {
        return match;
    }

    public Position getResumeByFriendlyGkPosition() {
        return resumeByFriendlyGkPosition;
    }

    public void setResumeByFriendlyGkPosition(Position resumeByFriendlyGkPosition) {
        this.resumeByFriendlyGkPosition = resumeByFriendlyGkPosition;
    }

    public Position getResumeByOppositeGkPosition() {
        return resumeByOppositeGkPosition;
    }

    public void setResumeByOppositeGkPosition(Position resumeByOpositeGkPosition) {
        this.resumeByOppositeGkPosition = resumeByOpositeGkPosition;
    }

    public Position getCornerOnFriendlySitePosition() {
        return cornerOnFriendlySitePosition;
    }

    public void setCornerOnFriendlySitePosition(Position cornerOnFriendlySitePosition) {
        this.cornerOnFriendlySitePosition = cornerOnFriendlySitePosition;
    }

    public Position getCornerOnOppositeSitePosition() {
        return cornerOnOppositeSitePosition;
    }

    public void setCornerOnOppositeSitePosition(Position cornerOnOppositeSitePosition) {
        this.cornerOnOppositeSitePosition = cornerOnOppositeSitePosition;
    }

    public void addRole(TeamRole role) {
        this.roles.add(role);
    }

    public boolean hasRole(TeamRole role) {
        for (TeamRole myRole : roles) {
            if (role.equals(myRole)) {
                return true;
            }
        }
        return false;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public boolean isGoalkeeper() {
        return isGoalkeeper;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public double getEnergy() {
        return energy;
    }

    public ShootingStrategy getShootingStrategy() {
        return shootingStrategy;
    }

    public void setShootingStrategy(ShootingStrategy shootingStrategy) {
        this.shootingStrategy = shootingStrategy;
    }

    public PassingStrategy getPassingStrategy() {
        return passingStrategy;
    }

    public void setPassingStrategy(PassingStrategy passingStrategy) {
        this.passingStrategy = passingStrategy;
    }

    public String getName() {
        return this.player.getName();
    }

    public PlayerStats getStats() {
        return this.player.getStats();
    }

    public int getOverall() {
        return this.player.getOverall();
    }

    public Ball getBall() {
        return ball;
    }

    public boolean isInLeftTeam() {
        return fieldSite == FieldSite.LEFT;
    }

    public boolean isInRightTeam() {
        return fieldSite == FieldSite.RIGHT;
    }
}
