package soccer.game.entity.player.animation.startfrommiddle;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.Animation;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.events.EventTypes;
import soccer.game.team.TeamRole;

import java.util.ArrayList;
import java.util.Random;

public class StartFromMiddlePlayer2Animation implements Animation {

    private final GamePlayer gamePlayer;
    private final Random random = new Random();

    public StartFromMiddlePlayer2Animation(GamePlayer gamePlayer) {
        if (!gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_2)) {
            throw new IllegalStateException("Player performing StartFromMiddlePlayer2Animation should have START_FROM_MIDDLE_2 role");
        }
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (gamePlayer.isBallInRange() && !gamePlayer.hasBall()) {
            gamePlayer.takePossessionOfTheBall();
        }
        if (gamePlayer.hasBall()) {
            gamePlayer.pass(getRandomPlayerForFirstPass(), Ball.FAST_PASS_SPEED);
            AnimationUtils.stopAndMarkAnimationAsReady(gamePlayer);
            gamePlayer.getMatch().produceEvent(EventTypes.FIRST_PASS_FROM_THE_MIDDLE_PERFORMED);
        }
    }

    private GamePlayer getRandomPlayerForFirstPass() {
        ArrayList<GamePlayer> candidates = new ArrayList<>();
        for (GamePlayer teamMate : gamePlayer.getMyTeamPlayers()) {
            if (!teamMate.hasRole(TeamRole.START_FROM_MIDDLE_1) && !teamMate.hasRole(TeamRole.START_FROM_MIDDLE_2) && !teamMate.isGoalkeeper()) {
                candidates.add(teamMate);
            }
        }
        int idx = random.nextInt(candidates.size());
        return candidates.get(idx);
    }
}
