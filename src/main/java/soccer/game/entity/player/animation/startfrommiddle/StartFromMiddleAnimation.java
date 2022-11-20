package soccer.game.entity.player.animation.startfrommiddle;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToBasePositionAnimation;

public class StartFromMiddleAnimation implements Animation {

    private Animation animation;

    public StartFromMiddleAnimation(GamePlayer gamePlayer) {
        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            animation = new StartFromMiddlePlayer1Animation(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            animation = new StartFromMiddlePlayer2Animation(gamePlayer);
        } else {
            animation = new GoToBasePositionAnimation(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        animation.handleMovement();
    }
}
