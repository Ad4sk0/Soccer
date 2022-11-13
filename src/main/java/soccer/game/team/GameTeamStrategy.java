package soccer.game.team;

import org.jboss.logging.Logger;
import soccer.app.entities.formation.FormationPosition;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingFieldUtils;
import soccer.utils.Position;

import java.util.List;


public class GameTeamStrategy {
    private final GameTeam team;
    Logger logger = Logger.getLogger(this.getClass());

    public GameTeamStrategy(GameTeam team) {
        this.team = team;
    }

    public void initPlayersPositions() {
        for (GamePlayer gamePlayer : team.getPlayingPlayers()) {
            FormationPosition assignedPosition = gamePlayer.getPlayer().getAssignedPosition();

            gamePlayer.setBasePosition(moveToCorrectFieldSite(gamePlayer, assignedPosition.getBasePosition()));
            gamePlayer.setResumeByFriendlyGkPosition(moveToCorrectFieldSite(gamePlayer, assignedPosition.getResumeByFriendlyGkPosition()));
            gamePlayer.setResumeByOppositeGkPosition(moveToCorrectFieldSite(gamePlayer, assignedPosition.getResumeByOppositeGkPosition()));
            gamePlayer.setCornerOnFriendlySitePosition(moveToCorrectFieldSite(gamePlayer, assignedPosition.getCornerOnFriendlySitePosition()));
            gamePlayer.setCornerOnOppositeSitePosition(moveToCorrectFieldSite(gamePlayer, assignedPosition.getCornerOnOppositeSitePosition()));

            if (AnimationUtils.isStartingFromTheMiddle(gamePlayer)) {
                gamePlayer.setInitialPosition(generatePlayerInitPositionWithBall(gamePlayer));
            } else {
                gamePlayer.setInitialPosition(gamePlayer.getBasePosition());
            }
        }
    }

    private Position moveToCorrectFieldSite(GamePlayer gamePlayer, Position position) {
        if (gamePlayer.getFieldSite() == FieldSite.RIGHT) {
            PlayingFieldUtils.moveXToOtherHalf(position);
        }
        return position;
    }

    private Position generatePlayerInitPositionWithBall(GamePlayer gamePlayer) {
        if (gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
            return new Position(PlayingFieldUtils.getStartingFromMiddlePos1(gamePlayer));
        } else if (gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_2)) {
            return new Position(PlayingFieldUtils.getStartingFromMiddlePos2(gamePlayer));
        }
        throw new IllegalStateException("generatePlayerInitPositionWithBall requested however player has no role assigned. Possible error");
    }

    public void assignPlayerRoles() {
        List<GamePlayer> players = team.getPlayingPlayers();
        GamePlayer mostForwardPlayer = players.get(0);
        GamePlayer mostForwardPlayer2 = players.get(1);
        for (GamePlayer player : players) {
            if (player.getPlayingPosition().getOrder() > mostForwardPlayer.getPlayingPosition().getOrder()) {
                mostForwardPlayer2 = mostForwardPlayer;
                mostForwardPlayer = player;
            } else if (player.getPlayingPosition().getOrder() > mostForwardPlayer2.getPlayingPosition().getOrder()) {
                mostForwardPlayer2 = player;
            }
        }
        // TODO do it better
        mostForwardPlayer.addRole(TeamRole.START_FROM_MIDDLE_1);
        mostForwardPlayer2.addRole(TeamRole.START_FROM_MIDDLE_2);
        mostForwardPlayer.addRole(TeamRole.UPPER_CORNER_TAKER);
        mostForwardPlayer.addRole(TeamRole.BOTTOM_CORNER_TAKER);
    }
}
