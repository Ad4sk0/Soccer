package soccer.game.entity.player.animation;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.PlayerState;
import soccer.game.entity.player.movement.MovementUtils;
import soccer.game.match.GameMatch;
import soccer.game.team.TeamRole;
import soccer.utils.Position;

public class AnimationUtils {

    private AnimationUtils() {
    }

    public static boolean isReady(GamePlayer player) {
        return player.getPlayerState() == PlayerState.IS_ANIMATION_READY;
    }

    public static boolean areAllPlayersAnimationReady(GameMatch match) {
        for (GamePlayer player : match.getAllPlayingPlayers()) {
            if (player.getPlayerState() == PlayerState.IS_PERFORMING_ANIMATION) {
                return false;
            }
        }
        return true;
    }

    public static boolean areAllOtherPlayersAnimationReady(GamePlayer gamePlayer) {
        for (GamePlayer otherGamePlayer : gamePlayer.getMatch().getAllPlayingPlayers()) {
            if (!gamePlayer.equals(otherGamePlayer) && otherGamePlayer.getPlayerState() == PlayerState.IS_PERFORMING_ANIMATION) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStartingFromTheMiddleTeam(GamePlayer gamePlayer) {
        return gamePlayer.getMatch().getStartFromTheMiddleTeamSite() == gamePlayer.getFieldSite();
    }

    public static boolean isStartingFromTheMiddlePlayer1(GamePlayer gamePlayer) {
        return isStartingFromTheMiddleTeam(gamePlayer) && gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1);
    }

    public static boolean isStartingFromTheMiddlePlayer2(GamePlayer gamePlayer) {
        return isStartingFromTheMiddleTeam(gamePlayer) && gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_2);
    }

    public static boolean isStartingFromTheMiddle(GamePlayer gamePlayer) {
        return isStartingFromTheMiddlePlayer1(gamePlayer) || isStartingFromTheMiddlePlayer2(gamePlayer);
    }

    public static void goForBall(GamePlayer gamePlayer) {
        MovementUtils.sprintTowardsBall(gamePlayer);
        if (gamePlayer.isBallInRange()) {
            gamePlayer.takePossessionOfTheBall();
        }
    }

    public static void goToPosition(GamePlayer gamePlayer, Position targetPosition) {
        if (gamePlayer.getPosition().equals(targetPosition)) {
            gamePlayer.stop();
        }
        MovementUtils.sprintTowardsPosition(gamePlayer, targetPosition);
    }

    public static void goToPositionAndMarkAnimationAsReady(GamePlayer gamePlayer, Position targetPosition) {
        if (gamePlayer.getPlayerState().equals(PlayerState.IS_ANIMATION_READY)) {
            return;
        }
        if (gamePlayer.getPosition().equals(targetPosition)) {
            stopAndMarkAnimationAsReady(gamePlayer);
        } else {
            goToPosition(gamePlayer, targetPosition);
        }
    }

    public static void goToBasePositionAndMarkAnimationAsReady(GamePlayer gamePlayer) {
        goToPositionAndMarkAnimationAsReady(gamePlayer, gamePlayer.getBasePosition());
    }

    public static void stopAndMarkAnimationAsReady(GamePlayer gamePlayer) {
        gamePlayer.stop();
        gamePlayer.setPlayerState(PlayerState.IS_ANIMATION_READY);
    }
}
