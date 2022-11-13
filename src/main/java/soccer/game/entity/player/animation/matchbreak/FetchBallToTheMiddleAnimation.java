package soccer.game.entity.player.animation.matchbreak;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.team.TeamRole;

public class FetchBallToTheMiddleAnimation implements MoveStrategy {
    private final GamePlayer gamePlayer;
    MoveStrategy moveStrategy;

    public FetchBallToTheMiddleAnimation(GamePlayer gamePlayer) {
        if (!gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
            throw new IllegalStateException("Player performing FetchBallToTheMiddleAnimation should have START_FROM_MIDDLE_1 role");
        }
        moveStrategy = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (!gamePlayer.hasBall()) {
            AnimationUtils.goForBall(gamePlayer);
        } else {
            moveStrategy.handleMovement();
        }
    }
}
