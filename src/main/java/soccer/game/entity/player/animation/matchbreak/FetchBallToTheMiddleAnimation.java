package soccer.game.entity.player.animation.matchbreak;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;
import soccer.game.team.TeamRole;

public class FetchBallToTheMiddleAnimation implements Animation {
    private final GamePlayer gamePlayer;
    Animation animation;

    public FetchBallToTheMiddleAnimation(GamePlayer gamePlayer) {
        if (!gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
            throw new IllegalStateException("Player performing FetchBallToTheMiddleAnimation should have START_FROM_MIDDLE_1 role");
        }
        animation = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (!gamePlayer.hasBall()) {
            AnimationUtils.goForBall(gamePlayer);
        } else {
            animation.handleMovement();
        }
    }
}
