package soccer.game.entity.player;

import soccer.app.entities.player.Player;
import soccer.game.GameState;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.ball.Ball;
import soccer.game.match.GameMatch;
import soccer.game.team.GameTeam;
import soccer.models.playingfield.PlayingField;
import soccer.models.positions.PlayingPosition;
import soccer.utils.Position;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerTestUtils {

    private PlayerTestUtils() {
    }

    public static void makeNNumberOfGamePlayerMoves(GamePlayer gamePlayer, int n) {
        for (int i = 0; i < n; i++) {
            gamePlayer.makeMove();
        }
    }

    public static GamePlayer prepareGamePlayerForAnimationTest(GameState gameState) {
        GameMatch gameMatch = mock(GameMatch.class);
        GameTeam gameTeam = mock(GameTeam.class);
        GameTeam gameTeam2 = mock(GameTeam.class);
        Player player = mock(Player.class);
        Ball ball = mock(Ball.class);

        when(gameMatch.getGameState()).thenReturn(gameState);
        when(gameTeam.getOppositeTeam()).thenReturn(gameTeam2);
        when(gameTeam2.isRightSiteTeam()).thenReturn(true);
        when(player.getSpeed()).thenReturn(MovingEntity.MAX_ENTITY_SPEED);
        when(player.getAcceleration()).thenReturn(MovingEntity.MAX_ENTITY_SPEED);

        GamePlayer gamePlayer = new GamePlayer(player, gameTeam, gameMatch, ball);
        gamePlayer.setInitialPosition(new Position(PlayingField.CENTRE_CIRCLE_POSITION));
        gamePlayer.setStartingPosition(new Position(PlayingField.CENTRE_CIRCLE_POSITION));
        gamePlayer.setBasePosition(new Position(PlayingField.CENTRE_CIRCLE_POSITION));
        gamePlayer.setPlayingPosition(PlayingPosition.CM);
        gamePlayer.setMyTeamPlayers(new ArrayList<>());
        return gamePlayer;
    }
}
