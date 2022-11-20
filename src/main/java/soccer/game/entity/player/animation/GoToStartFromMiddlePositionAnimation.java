package soccer.game.entity.player.animation;

import soccer.game.entity.player.GamePlayer;
import soccer.models.playingfield.PlayingFieldUtils;
import soccer.utils.Position;

public class GoToStartFromMiddlePositionAnimation implements Animation {
    GamePlayer gamePlayer;
    Position targetPosition;

    public GoToStartFromMiddlePositionAnimation(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer)) {
            targetPosition = PlayingFieldUtils.getStartingFromMiddlePos1(gamePlayer);
        } else if (AnimationUtils.isStartingFromTheMiddlePlayer2(gamePlayer)) {
            targetPosition = PlayingFieldUtils.getStartingFromMiddlePos2(gamePlayer);
        }
    }

    @Override
    public void handleMovement() {
        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, targetPosition);
    }
}
