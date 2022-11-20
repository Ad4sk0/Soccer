package soccer.game.entity.player.shooting;

import soccer.game.entity.player.GamePlayer;
import soccer.models.playingfield.PlayingFieldUtils;
import soccer.utils.Position;

import java.util.Random;

public class RandomShootingStrategy implements ShootingStrategy {

    Random random = new Random();
    GamePlayer player;

    public RandomShootingStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleShooting() {
        if (!shouldShoot()) return;
        chooseShootStyle();
        Position shootTarget = ShootingUtils.getShootTarget(player, 80);
        int shootPower = getShootPower();
        player.shoot(shootTarget, shootPower);
        player.stop();
    }

    private boolean shouldShoot() {

        // Shot in penalty area
        if (PlayingFieldUtils.isInOppositeTeamPenaltyArea(player)) {
            return true;
        }

//        // Randomly shot on opposite team field half
//        if (PlayingFieldUtils.isOnOppositeTeamFieldHalf(player)) {
//            return random.nextInt(0, 1000) > 998;
//        }
        return false;
    }

    private void chooseShootStyle() {
        // TODO
    }

    private int getShootPower() {
        return player.getPlayer().getShotPower();
    }

}
