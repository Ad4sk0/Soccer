package soccer.game.entity.player.animation.corner;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.team.TeamRole;

public class CornerAnimation implements MoveStrategy {
    GamePlayer gamePlayer;
    MoveStrategy moveStrategy;

    public CornerAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isPerformingCornerKick()) {
            moveStrategy = new CornerPerformerAnimation(gamePlayer);
        } else {
            moveStrategy = new GoToCornerPositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        moveStrategy.handleMovement();
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
