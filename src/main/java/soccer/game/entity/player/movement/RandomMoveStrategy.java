package soccer.game.entity.player.movement;

import soccer.game.entity.player.GamePlayer;
import soccer.game.entity.player.PlayerState;

import java.util.Random;

public class RandomMoveStrategy implements MoveStrategy {

    Random random = new Random();
    GamePlayer player;

    public RandomMoveStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleMovement() {

        if (player.getPlayerState() == PlayerState.IS_AWAITING_PASS) {
            MovementUtils.directTowardsBall(player);
            player.sprint();
        }

        if (!shouldMove()) return;
        chooseMove();
    }

    private boolean shouldMove() {
        return random.nextInt(0, 1000) > 100;
    }

    private void chooseMove() {

        // Attack
        if (player.hasBall()) {
            MovementUtils.directTowardsOppositeGoal(player);
            player.jog();
        }

        // Change X direction
        else if (random.nextInt(0, 1000) > 998) {
            player.getDirection().setX(getRandomDirection());
        }

        // Change Y direction
        else if (random.nextInt(0, 1000) > 997) {
            player.getDirection().setY(getRandomDirection());
        }

        // Prefer moves towards base position
        else if (random.nextInt(0, 1000) > 994) {
            MovementUtils.directTowardsBasePosition(player);
            player.walk();
        }

        // Prefer moves towards ball
        else if (random.nextInt(0, 1000) > 995) {
            MovementUtils.directTowardsBall(player);
            player.jog();
        }
    }

    private double getRandomDirection() {
        return random.nextDouble(-1, 1);
    }
}
