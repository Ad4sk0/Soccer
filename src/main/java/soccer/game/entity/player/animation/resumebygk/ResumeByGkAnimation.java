package soccer.game.entity.player.animation.resumebygk;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.movement.MoveStrategy;

public class ResumeByGkAnimation implements MoveStrategy {
    private final GamePlayer gamePlayer;
    private final MoveStrategy moveStrategy;

    public ResumeByGkAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isGkOfResumingTeam()) {
            moveStrategy = new GkResumeGameAnimation(gamePlayer);
        } else {
            moveStrategy = new GoToResumeByGkPositionAnimation(gamePlayer);
        }
    }

    private boolean isGkOfResumingTeam() {
        return gamePlayer.isGoalkeeper() && gamePlayer.getMatch().getResumeByGkFieldSite() == gamePlayer.getFieldSite();
    }

    @Override
    public void handleMovement() {
        moveStrategy.handleMovement();
    }
}
