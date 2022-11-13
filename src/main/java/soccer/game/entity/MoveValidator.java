package soccer.game.entity;

import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import java.awt.geom.Point2D;

public class MoveValidator {

    private MoveValidator() {
    }

    public static boolean isVerticalSideLineExceeded(MovingEntity entity) {
        return entity.getX() < 0 || entity.getX() > PlayingField.FIELD_WIDTH;
    }

    public static boolean isHorizontalSideLineExceeded(MovingEntity entity) {
        return entity.getY() < 0 || entity.getY() > PlayingField.FIELD_HEIGHT;
    }

    public static boolean isGoalLineExceeded(MovingEntity entity) {
        return entity.getY() > PlayingField.UPPER_POST_HEIGHT &&
                entity.getY() < PlayingField.BOTTOM_POST_HEIGHT &&
                isVerticalSideLineExceeded(entity);
    }

    public static boolean isCornerLineExceeded(MovingEntity entity) {
        return (entity.getY() < PlayingField.UPPER_POST_HEIGHT ||
                entity.getY() > PlayingField.BOTTOM_POST_HEIGHT) &&
                isVerticalSideLineExceeded(entity);
    }

    public static boolean isGoalPostHit(MovingEntity entity) {
        for (Position goalPost : PlayingField.getGoalPosts()) {
            double dist = Point2D.distance(entity.getX(), entity.getY(), goalPost.getX(), goalPost.getY());
            if (dist < entity.getSize() / 2.0) {
                return true;
            }
        }
        return false;
    }
}
