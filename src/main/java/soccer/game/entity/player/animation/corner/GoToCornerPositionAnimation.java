package soccer.game.entity.player.animation.corner;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.utils.Position;

public class GoToCornerPositionAnimation implements MoveStrategy {

    GamePlayer gamePlayer;
    Position targetPosition;

    public GoToCornerPositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isInAttackingTeam()) {
            targetPosition = gamePlayer.getCornerOnOppositeSitePosition();
        } else if (isInDefendingTeam()) {
            targetPosition = gamePlayer.getCornerOnFriendlySitePosition();
        } else {
            throw new IllegalStateException("Unable to determine corner performing site for player " + gamePlayer.getNumber());
        }
    }

    private boolean isInAttackingTeam() {
        return gamePlayer.getMatch().getCornerPerformingFieldSite() == gamePlayer.getFieldSite();
    }

    private boolean isInDefendingTeam() {
        return gamePlayer.getMatch().getCornerPerformingFieldSite() != gamePlayer.getFieldSite();
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, targetPosition);
    }
}
