package soccer.game.entity.player.animation.goal;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;
import soccer.game.entity.player.movement.MoveStrategy;

public class AfterGoalAnimation implements MoveStrategy {
    private final GamePlayer gamePlayer;
    private final MoveStrategy moveStrategy;

    public AfterGoalAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (isGkOfStartingTeam()) {
            moveStrategy = new GkPassToTheMiddleAfterGoalAnimation(gamePlayer);
        } else if (isStartingFromMiddle()) {
            moveStrategy = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        } else {
            moveStrategy = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    private boolean isStartingFromMiddle() {
        return AnimationUtils.isStartingFromTheMiddle(gamePlayer);
    }

    private boolean isGkOfStartingTeam() {
        return gamePlayer.isGoalkeeper() && gamePlayer.getMatch().getLastGoalScoredBy() != gamePlayer.getFieldSite();
    }

    @Override
    public void handleMovement() {
        moveStrategy.handleMovement();
    }
}
