package soccer.game.entity.player.animation.resumebygk;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;

public class ResumeByGkAnimation implements Animation {
    private final GamePlayer gamePlayer;
    private final Animation animation;

    public ResumeByGkAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isGkOfResumingTeam()) {
            animation = new GkResumeGameAnimation(gamePlayer);
        } else {
            animation = new GoToResumeByGkPositionAnimation(gamePlayer);
        }
    }

    private boolean isGkOfResumingTeam() {
        return gamePlayer.isGoalkeeper() && gamePlayer.getMatch().getResumeByGkFieldSite() == gamePlayer.getFieldSite();
    }

    @Override
    public void handleMovement() {
        animation.handleMovement();
    }
}
