package soccer.game.entity.player.animation;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.GamePlayerState;
import soccer.game.entity.player.movement.MovementUtils;
import soccer.game.match.GameMatch;
import soccer.game.team.GameTeam;
import soccer.game.team.TeamRole;
import soccer.models.playingfield.FieldSite;
import soccer.utils.Position;

public class AnimationUtils {

    private AnimationUtils() {
    }

    public static boolean areAllOtherPlayersAnimationReady(GamePlayer gamePlayer) {
        for (GamePlayer otherGamePlayer : gamePlayer.getMatch().getAllPlayingPlayers()) {
            if (!gamePlayer.equals(otherGamePlayer) && otherGamePlayer.getPlayerState() == GamePlayerState.IS_PERFORMING_ANIMATION) {
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

    public static GamePlayer getStartingFromTheMiddlePlayer1(GameMatch gameMatch) {
        GameTeam startingFromMiddleTeam;
        if (gameMatch.getStartFromTheMiddleTeamSite() == FieldSite.LEFT) {
            startingFromMiddleTeam = gameMatch.getLeftSiteTeam();
        } else if (gameMatch.getStartFromTheMiddleTeamSite() == FieldSite.RIGHT) {
            startingFromMiddleTeam = gameMatch.getRightSiteTeam();
        } else {
            throw new IllegalStateException("Cannot determine starting from the middle team. Possible Error");
        }
        for (GamePlayer gamePlayer : startingFromMiddleTeam.getPlayingPlayers()) {
            if (gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
                return gamePlayer;
            }
        }
        throw new IllegalStateException("Cannot determine starting from the middle player. Possible Error");
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
        if (gamePlayer.getPlayerState().equals(GamePlayerState.IS_ANIMATION_READY)) {
            return;
        }
        if (gamePlayer.getPosition().equals(targetPosition)) {
            stopAndMarkAnimationAsReady(gamePlayer);
        } else {
            goToPosition(gamePlayer, targetPosition);
        }
    }

    public static void goToStartingPositionAndMarkAnimationAsReady(GamePlayer gamePlayer) {
        goToPositionAndMarkAnimationAsReady(gamePlayer, gamePlayer.getStartingPosition());
    }

    public static void stopAndMarkAnimationAsReady(GamePlayer gamePlayer) {
        gamePlayer.stop();
        gamePlayer.changePlayerState(GamePlayerState.IS_ANIMATION_READY);
    }
}
