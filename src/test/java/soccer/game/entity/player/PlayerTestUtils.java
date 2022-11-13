package soccer.game.entity.player;

import soccer.app.entities.player.Player;
import soccer.game.GameState;
import soccer.game.entity.MovingEntity;
import soccer.game.entity.ball.Ball;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

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
        Player player = mock(Player.class);
        Ball ball = mock(Ball.class);
        when(gameMatch.getGameState()).thenReturn(gameState);
        when(player.getSpeed()).thenReturn(MovingEntity.MAX_ENTITY_SPEED);
        when(player.getAcceleration()).thenReturn(MovingEntity.MAX_ENTITY_SPEED);
        GamePlayer gamePlayer = new GamePlayer(player, gameMatch, ball);
        gamePlayer.setInitialPosition(new Position(PlayingField.CENTRE_CIRCLE_POSITION));
        return gamePlayer;
    }
}
