package soccer.game.entity.player.animation.goal;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;

public class AfterGoalAnimation implements Animation {
    private final GamePlayer gamePlayer;
    private final Animation animation;

    public AfterGoalAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isGkOfStartingTeam()) {
            animation = new GkPassToTheMiddleAfterGoalAnimation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            animation = new StartFromMiddlePlayer1AfterGoalAnimation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            animation = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        } else {
            animation = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    private boolean isGkOfStartingTeam() {
        return gamePlayer.isGoalkeeper() && gamePlayer.getMatch().getLastGoalScoredBy() != gamePlayer.getFieldSite();
    }

    @Override
    public void handleMovement() {
        animation.handleMovement();
    }
}
