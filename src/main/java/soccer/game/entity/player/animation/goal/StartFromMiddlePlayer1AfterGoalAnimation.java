package soccer.game.entity.player.animation.goal;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.GamePlayerState;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.animation.GoToStartFromMiddlePositionAnimation;
import soccer.game.team.TeamRole;
import soccer.models.playingfield.PlayingFieldUtils;

public class StartFromMiddlePlayer1AfterGoalAnimation implements Animation {

    private final GamePlayer gamePlayer;
    Animation goToStartFromTheMiddlePositionAnimation;

    public StartFromMiddlePlayer1AfterGoalAnimation(GamePlayer gamePlayer) {
        if (!gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
            throw new IllegalStateException("Player performing FetchBallToTheMiddleAnimation should have START_FROM_MIDDLE_1 role");
        }
        goToStartFromTheMiddlePositionAnimation = new GoToStartFromMiddlePositionAnimation(gamePlayer);
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (!gkMadeFirstPassAndIsReady()) {
            AnimationUtils.goToPosition(gamePlayer, PlayingFieldUtils.getStartingFromMiddlePos1(gamePlayer));
        } else if (gkMadeFirstPassAndIsReady() && !gamePlayer.hasBall()) {
            AnimationUtils.goForBall(gamePlayer);
        } else {
            goToStartFromTheMiddlePositionAnimation.handleMovement();
        }
    }

    private boolean gkMadeFirstPassAndIsReady() {
        for (GamePlayer teamMate : gamePlayer.getMyTeamPlayers()) {
            if (teamMate.isGoalkeeper() && teamMate.getPlayerState() == GamePlayerState.IS_ANIMATION_READY) {
                return true;
            }
            if (teamMate.isGoalkeeper()) {
                return false;
            }
        }
        return false;
    }


//    @Override
//    public void handleMovement() {
//
//        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer) && !gamePlayer.getBall().isInTheMiddle()) {
//            if (gamePlayer.hasBall()) {
//                placeBallInTheMiddle();
//            } else if (gkMadeFirstPassAndIsReady()) {
//                AnimationUtils.goForBall(gamePlayer);
//            } else {
//                AnimationUtils.goToPosition(gamePlayer, targetPosition);
//            }
//            return;
//        }
//
//        if (AnimationUtils.isStartingFromTheMiddlePlayer1(gamePlayer) && gamePlayer.hasBall()) {
//            gamePlayer.leaveBall();
//        }
//
//        AnimationUtils.goToPositionAndMarkAnimationAsReady(gamePlayer, targetPosition);
//    }
//
//    private void placeBallInTheMiddle() {
//        if (!gamePlayer.hasBall()) {
//            throw new IllegalStateException("Place ball in the middle requested however player has no ball. Possible error");
//        }
//        if (!gamePlayer.getBall().isInTheMiddle()) {
//            AnimationUtils.goToPosition(gamePlayer, PlayingField.CENTRE_CIRCLE_POSITION);
//        }
//        if (gamePlayer.getBall().isInTheMiddle()) {
//            gamePlayer.leaveBall();
//        }
//    }
}
