package soccer.game.entity.player.animation.startfrommiddle;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.animation.AnimationUtils;
import soccer.game.entity.player.movement.MoveStrategy;
import soccer.game.team.TeamRole;

public class StartFromMiddlePlayer1Animation implements MoveStrategy {

    private final GamePlayer gamePlayer;
    private boolean firstPassMade = false;

    public StartFromMiddlePlayer1Animation(GamePlayer gamePlayer) {
        if (!gamePlayer.hasRole(TeamRole.START_FROM_MIDDLE_1)) {
            throw new IllegalStateException("Player performing StartFromMiddlePlayer1Animation should have START_FROM_MIDDLE_1 role");
        }
        this.gamePlayer = gamePlayer;
    }

    @Override
    public void handleMovement() {
        if (firstPassMade) {
            return;
        }
        if (!gamePlayer.hasBall()) {
            AnimationUtils.goForBall(gamePlayer);
        } else {
            makeFirstPass();
            AnimationUtils.stopAndMarkAnimationAsReady(gamePlayer);
        }
    }

    private void makeFirstPass() {
        gamePlayer.pass(getSecondPlayerStartingFromMiddle(), Ball.FAST_PASS_SPEED);
        firstPassMade = true;
    }

    private GamePlayer getSecondPlayerStartingFromMiddle() {
        for (GamePlayer teamMate : gamePlayer.getMyTeamPlayers()) {
            if (teamMate.hasRole(TeamRole.START_FROM_MIDDLE_2)) {
                return teamMate;
            }
        }
        throw new UnsupportedOperationException("Cannot start from the middle. Second player for start needed");
    }
}
