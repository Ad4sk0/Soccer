package soccer.game.entity.player.animation.endmatch;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.models.playingfield.PlayingField;

public class GoToLockerRoomAnimation implements Animation {

    GamePlayer gamePlayer;

    public GoToLockerRoomAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, PlayingField.END_MATCH_TARGET_POSITION);
    }
}
