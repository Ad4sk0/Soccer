package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;

import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {

    Random random = new Random();
    GamePlayer player;

    public RandomMoveStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleMovement() {
        if (player.hasBall()) {
            MovementUtils.directTowardsOppositeGoal(player);
            player.jog();
        } else {
            MovementUtils.sprintTowardsDynamicBasePosition(player);
        }
    }

    @Override
    public boolean isPlayingStrategy() {
        return true;
    }

    private boolean shouldMove() {
        return random.nextInt(0, 1000) > 100;
    }

    private double getRandomDirection() {
        return random.nextDouble(-1, 1);
    }
}
