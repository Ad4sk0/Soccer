package soccer.models.playingfield;

import soccer.game.entity.player.GamePlayer;
import soccer.utils.Position;

public class PlayingFieldUtils {

    private PlayingFieldUtils() {
    }

    public static double moveXToOtherHalf(double x) {
        return PlayingField.FIELD_WIDTH - x;
    }

    public static Position moveXToOtherHalf(Position position) {
        position.setX(moveXToOtherHalf(position.getX()));
        return position;
    }

    public static boolean isOnFriendlyTeamFieldHalf(GamePlayer player) {
        if (player.getFieldSite() == FieldSite.LEFT) {
            return player.getPosition().isOnLeftSite();
        } else {
            return player.getPosition().isOnRightSite();
        }
    }

    public static boolean isOnOppositeTeamFieldHalf(GamePlayer player) {
        if (player.getFieldSite() == FieldSite.LEFT) {
            return player.getPosition().isOnRightSite();
        } else {
            return player.getPosition().isOnLeftSite();
        }
    }

    public static boolean isInFriendlyTeamPenaltyArea(GamePlayer player) {
        if (player.getFieldSite() == FieldSite.LEFT) {
            return player.getPosition().isInLeftPenaltyArea();
        } else {
            return player.getPosition().isInRightPenaltyArea();
        }
    }

    public static boolean isInOppositeTeamPenaltyArea(GamePlayer player) {
        if (player.getFieldSite() == FieldSite.LEFT) {
            return player.getPosition().isInRightPenaltyArea();
        } else {
            return player.getPosition().isInLeftPenaltyArea();
        }
    }

    public static Position getStartingFromMiddlePos1(GamePlayer gamePlayer) {
        if (gamePlayer.isInLeftTeam()) {
            return PlayingField.STARTING_FROM_MIDDLE_LEFT_TEAM_POS_1;
        } else if (gamePlayer.isInRightTeam()) {
            return PlayingField.STARTING_FROM_MIDDLE_RIGHT_TEAM_POS_1;
        } else {
            throw new IllegalStateException("Game player has no team assigned");
        }
    }

    public static Position getStartingFromMiddlePos2(GamePlayer gamePlayer) {
        if (gamePlayer.isInLeftTeam()) {
            return PlayingField.STARTING_FROM_MIDDLE_LEFT_TEAM_POS_2;
        } else if (gamePlayer.isInRightTeam()) {
            return PlayingField.STARTING_FROM_MIDDLE_RIGHT_TEAM_POS_2;
        } else {
            throw new IllegalStateException("Game player has no team assigned");
        }
    }
}
