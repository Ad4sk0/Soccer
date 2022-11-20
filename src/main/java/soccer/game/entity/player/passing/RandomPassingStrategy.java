package soccer.game.entity.player.passing;

import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;

import java.util.Random;

public class RandomPassingStrategy implements PassingStrategy {

    Random random = new Random();
    GamePlayer player;

    public RandomPassingStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handlePassing() {
        if (!shouldPass()) return;
        GamePlayer passTargetPlayer = getAnyPassTargetInTheFront();
        if (passTargetPlayer != null) {
            player.pass(passTargetPlayer, getPassPower());
        }
    }

    private boolean shouldPass() {
        return random.nextInt(0, 1000) > 997;
    }

    private GamePlayer getRandomPassTarget() {
        int idx = random.nextInt(player.getMyTeamPlayers().size());
        return player.getMyTeamPlayers().get(idx);
    }

    private GamePlayer getAnyPassTargetInTheFront() {
        for (GamePlayer teamMate : player.getMyTeamPlayers()) {
            if (player.isInLeftTeam() && teamMate.getX() > player.getX()) {
                return teamMate;
            }
            if (player.isInRightTeam() && teamMate.getX() < player.getX()) {
                return teamMate;
            }
        }
        return null;
    }


    private int getPassPower() {
        return Ball.FAST_PASS_SPEED;
    }
}
