package soccer.game.socket.payload.data;

import soccer.game.entity.ball.Ball;

public class BallState {

    private final double x;
    private final double y;

    public BallState(Ball ball) {
        x = ball.getX();
        y = ball.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
