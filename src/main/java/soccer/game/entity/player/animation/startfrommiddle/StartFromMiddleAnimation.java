package soccer.game.entity.player.animation.startfrommiddle;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;
import soccer.game.entity.player.movement.MoveStrategy;

public class StartFromMiddleAnimation implements MoveStrategy {

    MoveStrategy moveStrategy;

    public StartFromMiddleAnimation(GamePlayer gamePlayer) {
        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            moveStrategy = new StartFromMiddlePlayer1Animation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            moveStrategy = new StartFromMiddlePlayer2Animation(gamePlayer);
        } else {
            moveStrategy = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        moveStrategy.handleMovement();
    }
}
