package soccer.game.entity;


import soccer.utils.Position;
import soccer.utils.Vector2d;

public abstract class MovingEntity {
    public static final int MAX_ENTITY_SPEED = 100;
    private static final int FPS = 60;
    protected final int size;
    protected Position position = new Position();
    protected Vector2d direction = new Vector2d();
    protected Vector2d velocity = new Vector2d();
    protected double currentSpeed;
    private boolean initialPositionSet = false;

    protected MovingEntity(int size) {
        this.size = size;
    }

    public abstract void makeMove();

    protected void move() {
        calculateVelocity();
        position.addVector(velocity);
    }

    private void calculateVelocity() {
        velocity = Vector2d.multiply(direction, normalizeSpeedFactor(currentSpeed));
    }

    public abstract float getMaxRealSpeedInPxPerFrame();

    private double normalizeSpeedFactor(double speed) {
        // Normalize speed values from 0 to 100 to values reflecting real speed of entities:
        double factor = MAX_ENTITY_SPEED / getMaxRealSpeedInPxPerFrame();
        return speed / factor;
    }

    public void undoMove() {
        position.subtractVector(velocity);
    }

    protected void reverseOnX() {
        direction.reverseX();
    }

    protected void reverseOnY() {
        direction.reverseY();
    }

    public void stop() {
        direction.setTo0();
        currentSpeed = 0;
    }

    public double getCurrentPiecePxPerSec() {
        return normalizeSpeedFactor(currentSpeed) * FPS; // pixels per second
    }

    public double getCurrentPieceMeterPerSec() {
        // 1px = 0,1m
        return getCurrentPiecePxPerSec() / 10;
    }

    public double getCurrentPieceKmPerHour() {
        return getCurrentPieceMeterPerSec() * 3600 / 1000;
    }

    public Position getPosition() {
        return position;
    }

    protected void setPosition(Position position) {
        this.position = position;
    }

    public void setInitialPosition(Position position) {
        if (initialPositionSet) {
            throw new IllegalStateException("Position already set");
        }
        initialPositionSet = true;
        this.position = new Position(position);
    }

    public Vector2d getDirection() {
        return direction;
    }

    protected void setDirection(Vector2d direction) {
        this.direction = direction;
    }

    public Vector2d getVelocity() {
        return velocity;
    }

    public int getSize() {
        return size;
    }

    public double getCurrentSpeed() {
        return currentSpeed;
    }

    protected void setCurrentSpeed(double currentSpeed) {
        if (currentSpeed < 0 || currentSpeed > MAX_ENTITY_SPEED) {
            throw new IllegalArgumentException("Speed has to be in range from 0 to " + MAX_ENTITY_SPEED);
        }
        this.currentSpeed = currentSpeed;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public boolean isOnPosition(Position kickFromPosition) {
        return position.equals(kickFromPosition);
    }
}
