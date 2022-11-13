package soccer.utils;

public class GeomUtils {

    private GeomUtils() {
    }

    public static Vector2d calculateDirectionTowardsPosition(Position initialPosition, Position targetPosition) {
        double angle = calculateAngleTowardsPosition(initialPosition, targetPosition);
        double dx = Math.cos(angle);
        double dy = Math.sin(angle);
        return new Vector2d(dx, dy);
    }

    public static double calculateAngleTowardsPosition(Position initialPosition, Position targetPosition) {
        // atan2 is counter clockwise on coordinate axis. however in the game y is reverted, thus the result will be reverted as well.
        //       1.57
        //        |
        // 3.14 -- -- 0.00
        //        |
        //	    -1.57
        return Math.atan2(targetPosition.getY() - initialPosition.getY(), targetPosition.getX() - initialPosition.getX());
    }
}
