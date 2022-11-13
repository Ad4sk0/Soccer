package soccer.game.entity.player.shooting;

import soccer.game.entity.player.GamePlayer;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

public class ShootingUtils {

    // 0 for center, 100 for bottom post, -100 for upper post
    public static Position getShootTarget(GamePlayer player, int correction) {
        Position goalCenter = getOppositeTeamGoalCenter(player);
        double centerY = goalCenter.getY();
        double centerX = goalCenter.getX();
        double corFactor = PlayingField.GOAL_HEIGHT / 200;
        double corValue = (int) (correction * corFactor);
        double shotY = centerY + corValue;
        return new Position(centerX, shotY);
    }

    public static Position getOppositeTeamGoalCenter(GamePlayer player) {
        if (player.getFieldSite().equals(FieldSite.LEFT)) {
            return PlayingField.RIGHT_GOAL_POSITION;
        } else {
            return PlayingField.LEFT_GOAL_POSITION;
        }
    }

    public static Position getOppositeTeamUpperGoalPost(GamePlayer player) {
        if (player.getFieldSite().equals(FieldSite.LEFT)) {
            return PlayingField.RIGHT_UPPER_POST;
        } else {
            return PlayingField.LEFT_UPPER_POST;
        }
    }

    public Position getOppositeTeamBottomGoalPost(GamePlayer player) {
        if (player.getFieldSite().equals(FieldSite.LEFT)) {
            return PlayingField.RIGHT_BOTTOM_POST;
        } else {
            return PlayingField.LEFT_BOTTOM_POST;
        }
    }
}
