package soccer.game.entity;

import soccer.game.entity.player.GamePlayer;
import soccer.game.team.GameTeam;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import java.awt.geom.Point2D;

import static soccer.models.playingfield.PlayingField.FIELD_WIDTH_HALF;

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

    public static boolean isBehindOffsideLine(GameTeam oppositeGameTeam, double x) {
        if (oppositeGameTeam.isLeftSiteTeam()) {
            return x < FIELD_WIDTH_HALF && x < oppositeGameTeam.getLastPlayerForOffsideLine();
        } else if (oppositeGameTeam.isRightSiteTeam()) {
            return x > FIELD_WIDTH_HALF && x > oppositeGameTeam.getLastPlayerForOffsideLine();
        }
        throw new IllegalStateException("isBehindOffsideLine requested however team has no field site assigned");
    }

    public static boolean isOnOffside(GamePlayer gamePlayer) {
        return isBehindOffsideLine(gamePlayer.getOppositeTeam(), gamePlayer.getX());
    }
}
