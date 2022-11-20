package soccer.game.entity.player;

import org.jboss.logging.Logger;
import soccer.app.entities.player.Player;
import soccer.app.entities.player.PlayerStats;
import soccer.game.GameState;
import soccer.game.entity.MoveValidator;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.corner.CornerAnimation;
import soccer.game.entity.player.animation.endmatch.GoToLockerRoomAnimation;
import soccer.game.entity.player.animation.goal.AfterGoalAnimation;
import soccer.game.entity.player.animation.matchbreak.MoveToOtherHalfAfterHalfAnimation;
import soccer.game.entity.player.animation.resumebygk.ResumeByGkAnimation;
import soccer.game.entity.player.animation.startfrommiddle.StartFromMiddleAnimation;
import soccer.game.entity.player.movement.AwaitingPassMoveStrategy;
import soccer.game.entity.player.movement.GoalkeeperMoveStrategy;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.entity.player.movement.RandomMoveStrategy;
import soccer.game.entity.player.passing.PassingStrategy;
import soccer.game.entity.player.passing.RandomPassingStrategy;
import soccer.game.entity.player.shooting.NoShootStrategy;
import soccer.game.entity.player.shooting.RandomShootingStrategy;
import soccer.game.entity.player.shooting.ShootingStrategy;
import soccer.game.match.GameMatch;
import soccer.game.team.GameTeam;
import soccer.game.team.TeamRole;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.models.positions.PlayingPosition;
import soccer.utils.GeomUtils;
import soccer.utils.Position;
import soccer.utils.Vector2d;

import javax.json.bind.annotation.JsonbTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static soccer.game.GameState.PLAYING;
import static soccer.game.entity.player.GamePlayerState.IS_AWAITING_PASS;
import static soccer.game.entity.player.GamePlayerState.IS_PERFORMING_ANIMATION;

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
    private final GameTeam myTeam;
    private final GameTeam oppositeTeam;
    Player player;
    ArrayList<TeamRole> roles = new ArrayList<>();
    Animation animation;
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
    private PassingStrategy previousPassingStrategy;
    private ShootingStrategy previousShootingStrategy;
    private boolean hasBall = false;
    private Position ballRelativePosition;
    private boolean isAwaitingPass = false;
    private GamePlayerState playerState = GamePlayerState.IS_IDLE;
    private GamePlayerState previousPlayingPlayerState = GamePlayerState.IS_IDLE;
    private GamePlayerState previousPlayerState = GamePlayerState.IS_IDLE;
    private List<GamePlayer> myTeamPlayers;
    private List<GamePlayer> opposedTeamPlayers;
    private GameState previousGameState;
    private boolean isGoalkeeper = false;
    private double energy;
    private double endurance;
    private MoveState moveState = MoveState.STOP;
    private double defaultEnergyLoss;
    private GamePlayerStyle gamePlayerStyle;
    private Position dynamicBasePosition;
    private Position startingPosition;

    public GamePlayer(Player player, GameTeam gameTeam, GameMatch match, Ball ball) {
        super(12);
        this.player = player;
        this.myTeam = gameTeam;
        this.oppositeTeam = gameTeam.getOppositeTeam();
        this.ball = ball;
        this.match = match;
        if (player.getAssignedPosition() != null) {
            this.playingPosition = player.getAssignedPosition().getPosition();
        }
        this.number = player.getNumber();
        this.maxSpeed = player.getSpeed();
        this.energy = 100;
        setBasePlayerStrategies();
        if (playingPosition == PlayingPosition.GK) {
            this.isGoalkeeper = true;
        }
        calculateDefaultEnergyLoss();
        this.gamePlayerStyle = GamePlayerStyle.NEUTRAL;
    }

    @Override
    public void makeMove() {

        // Reset previous values
        velocity.setTo0();

        // What happened in match
        handleGameStateChange();

        // What happened to the player
        handlePlayerEvents();

        // Do some logic based on current player state
        handlePlayerState();

        // Let movement strategy choose direction and speed
        considerMoving();

        // Adjust speed to current move state
        handleSpeed();

        // Make actual move
        move();

        // Lose some energy in each move
        handleEnergyLoss();

        // Move ball as the players moves
        adjustDynamicBasePosition();

        if (match.getGameState() == PLAYING) {
            correctMove();
            interactWithBall();
        }

        if (match.getGameState() == PLAYING && hasBall) {
            handleBallDefence();
            considerPassing();
            considerShooting();
        }

        if (hasBall) {
            driveTheBall();
        }
    }

    private void adjustDynamicBasePosition() {
        if (isGoalkeeper) return;

        double newDynamicBaseX = basePosition.getX() + gamePlayerStyle.positionCorrection;
        double newDynamicBaseY = basePosition.getY();

        // Change X based on Ball position
        double correctionX = calculateDynamicPositionXChangeValueBasedOnBallPosition();
        if (correctionX > 0) {
            newDynamicBaseX = Math.min(playingPosition.getDynamicPositionMaxX(), newDynamicBaseX + correctionX);
        } else {
            newDynamicBaseX = Math.max(playingPosition.getDynamicPositionMinX(), newDynamicBaseX + correctionX);
        }

        // Don't change behind offside line
        if (MoveValidator.isBehindOffsideLine(oppositeTeam, newDynamicBaseX)) {
            newDynamicBaseX = oppositeTeam.getLastPlayerForOffsideLine();
        }

        // Change Y based on Ball position
        if (myTeam.isDefending()) {
            double correctionY = calculateDynamicPositionYChangeValueBasedOnBallPosition();
            if (correctionY > 0) {
                newDynamicBaseY = Math.min(playingPosition.getDynamicPositionMaxY(), newDynamicBaseY + correctionY);
            } else {
                newDynamicBaseY = Math.max(playingPosition.getDynamicPositionMinY(), newDynamicBaseY + correctionY);
            }
        }

        dynamicBasePosition.setX(newDynamicBaseX);
        dynamicBasePosition.setY(newDynamicBaseY);
    }

    private double calculateDynamicPositionYChangeValueBasedOnBallPosition() {
        double distToBall = ball.getY() - getY();
        double factor = Math.abs(distToBall) / PlayingField.FIELD_WIDTH;
        return distToBall * factor;
    }

    private double calculateDynamicPositionXChangeValueBasedOnBallPosition() {
        double maxDist = playingPosition.getDynamicPositionMaxX() - playingPosition.getDynamicPositionMinX();
        double distToBall = ball.getX() - getX();
        double factor = Math.abs(distToBall) / maxDist;
        return distToBall * factor;
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
        previousPassingStrategy = passingStrategy;
        previousShootingStrategy = shootingStrategy;
    }


    public MoveStrategy getMoveStrategy() {
        return moveStrategy;
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = moveStrategy;
    }


    @Override
    public float getMaxRealSpeedInPxPerFrame() {
        // 0 - no move
        // ~ 5km/h - walk
        // ~ 30km/h - max sprint speed - 8,3(3) m/s - 83,3(3) px/s - 1,39 px/s/fps
        return 1.39f;
    }

    private void handleGameStateChange() {
        if (match.getGameState().equals(previousGameState)) return;

        switch (match.getGameState()) {
            case PLAYING -> resetToPreviousPlayerState();
            case START_FROM_THE_MIDDLE -> startAnimation(new StartFromMiddleAnimation(this));
            case GOAL -> startAnimation(new AfterGoalAnimation(this));
            case RESUME_BY_GK -> startAnimation(new ResumeByGkAnimation(this));
            case CORNER -> startAnimation(new CornerAnimation(this));
            case BREAK -> startAnimation(new MoveToOtherHalfAfterHalfAnimation(this));
            case END -> startAnimation(new GoToLockerRoomAnimation(this));
        }
        previousGameState = match.getGameState();
    }

    private void startAnimation(Animation animation) {
        changePlayerState(IS_PERFORMING_ANIMATION);
        leaveBall();
        this.animation = animation;
    }


    private void handlePlayerState() {
        if (match.getGameState() != PLAYING) return;
        switch (playerState) {
            case IS_AWAITING_PASS -> {
                if (!(moveStrategy instanceof AwaitingPassMoveStrategy)) {
                    changeMoveStrategy(new AwaitingPassMoveStrategy(this));
                }
            }
        }
    }

    private void handleBallDefence() {
        // TODO
    }

    private void considerMoving() {
        if (match.getGameState() == PLAYING) {
            moveStrategy.handleMovement();
        } else {
            animation.handleMovement();
        }
    }

    private void correctMove() {
        // Player can go out of field during animations
        if (match.getGameState() != PLAYING) return;

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

        // Handle incoming pass
        if (isPassingTarget() && match.getGameState() == PLAYING) {
            changePlayerState(IS_AWAITING_PASS);
            myTeam.setPassingTarget(null);
        }

        // Not wait for ball anymore
        if (playerState == IS_AWAITING_PASS && !ball.isFree()) {
            resetToPreviousPlayerState();
        }

        // Not wait for ball anymore
        if (moveStrategy instanceof AwaitingPassMoveStrategy && !ball.isFree()) {
            resetToPreviousMoveStrategy();
        }
    }

    private void considerPassing() {
        if (match.getGameState() != PLAYING) return;
        if (!hasBall) return;
        passingStrategy.handlePassing();
    }

    public void considerShooting() {
        if (match.getGameState() != PLAYING) return;
        if (!hasBall) return;
        shootingStrategy.handleShooting();
    }

    private void tryTackleBallFromOpponent() {
        if (random.nextInt(0, 100) > 95) {
            takePossessionOfTheBall();
        }
    }

    private void interactWithBall() {
        if (match.getGameState() != PLAYING) return;

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

    public void pass(GamePlayer targetPlayer, int ballSpeed) {
        logger.debug("Player " + number + " passes to " + targetPlayer.getNumber());
        kickTowardsTarget(targetPlayer.getPosition(), ballSpeed);
        myTeam.setPassingTarget(targetPlayer);
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

    public void leaveBall() {
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
        this.dynamicBasePosition = new Position(basePosition);
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

    public GamePlayerState getPlayerState() {
        return playerState;
    }

    public void changePlayerState(GamePlayerState newPlayerState) {
        if (newPlayerState == playerState) {
            logger.warn("Player " + number + ": Change player state requested " + newPlayerState + " however new player state is equal to current player state. Ignoring");
            return;
        }
        if (playerState.isPlayingState) {
            previousPlayingPlayerState = playerState;
        }
        playerState = newPlayerState;
    }

    private void resetToPreviousPlayerState() {
        logger.debug("Player: " + number + ": Resetting playerState to previous previousPlayingPlayerState: " + previousPlayingPlayerState + " from " + playerState);
        playerState = previousPlayerState;
    }

    private void resetToPreviousMoveStrategy() {
        logger.debug("Player: " + number + ": Resetting moveStrategy to previous previousPlayingMoveStrategy: " + previousPlayingMoveStrategy + " from " + moveStrategy);
        moveStrategy = previousPlayingMoveStrategy;
    }

    public void changeMoveStrategy(MoveStrategy newMoveStrategy) {
        if (newMoveStrategy.getClass() == moveStrategy.getClass()) {
            logger.warn("Change move strategy requested " + newMoveStrategy + " however new move strategy is equal to current move strategy. Ignoring");
            return;
        }
        if (moveStrategy.isPlayingStrategy()) {
            previousPlayingMoveStrategy = moveStrategy;
        }
        moveStrategy = newMoveStrategy;
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

    public GameTeam getGameTeam() {
        return myTeam;
    }

    public GameTeam getOppositeTeam() {
        return oppositeTeam;
    }

    public Position getDynamicBasePosition() {
        return dynamicBasePosition;
    }

    public boolean isPassingTarget() {
        return myTeam.getPassingTarget() != null && myTeam.getPassingTarget().equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return number == that.number && fieldSite == that.fieldSite && playingPosition == that.playingPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldSite, playingPosition, number);
    }

    public Position getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(Position startingPosition) {
        this.startingPosition = startingPosition;
    }
}
