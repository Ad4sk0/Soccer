package soccer.game.entity.player.animation;

import soccer.game.entity.player.GamePlayer;

public class GoToBasePositionAnimation implements Animation {

    GamePlayer gamePlayer;

    public GoToBasePositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToStartingPositionAndMarkAnimationAsReady(gamePlayer);
    }
}
