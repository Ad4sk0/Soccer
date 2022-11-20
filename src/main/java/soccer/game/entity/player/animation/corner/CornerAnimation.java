package soccer.game.entity.player.animation.corner;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.team.TeamRole;

public class CornerAnimation implements Animation {
    GamePlayer gamePlayer;
    Animation animation;

    public CornerAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isPerformingCornerKick()) {
            animation = new CornerPerformerAnimation(gamePlayer);
        } else {
            animation = new GoToCornerPositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        animation.handleMovement();
    }

    private boolean isPerformingCornerKick() {
        if (gamePlayer.getMatch().getCornerPerformingFieldSite() != gamePlayer.getFieldSite()) return false;
        TeamRole roleNeeded = TeamRole.UPPER_CORNER_TAKER;
        if (gamePlayer.getMatch().getCornerPerformingPosition().isOnBottomHalf()) {
            roleNeeded = TeamRole.BOTTOM_CORNER_TAKER;
        }
        return gamePlayer.hasRole(roleNeeded);
    }
}
