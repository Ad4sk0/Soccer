package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

public class MovementUtils {

    private MovementUtils() {
    }

    public static void directTowardsOppositeGoal(GamePlayer gamePlayer) {
        if (gamePlayer.getFieldSite().equals(FieldSite.LEFT)) {
            gamePlayer.turnTowardsPosition(PlayingField.RIGHT_GOAL_POSITION);
        } else {
            gamePlayer.turnTowardsPosition(PlayingField.LEFT_GOAL_POSITION);
        }
    }

    public static void directTowardsPosition(GamePlayer gamePlayer, Position target) {
        gamePlayer.turnTowardsPosition(target);
    }

    public static void directTowardsBasePosition(GamePlayer gamePlayer) {
        gamePlayer.turnTowardsPosition(gamePlayer.getBasePosition());
    }

    public static void directTowardsBall(GamePlayer gamePlayer) {
        gamePlayer.turnTowardsPosition(gamePlayer.getBall().getPosition());
    }

    public static void sprintTowardsBall(GamePlayer gamePlayer) {
        directTowardsBall(gamePlayer);
        gamePlayer.sprint();
    }

    public static void sprintTowardsPosition(GamePlayer gamePlayer, Position targetPosition) {
        directTowardsPosition(gamePlayer, targetPosition);
        gamePlayer.sprint();
    }
}
