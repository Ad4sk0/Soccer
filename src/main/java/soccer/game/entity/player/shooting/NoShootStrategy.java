package soccer.game.entity.player.shooting;

import soccer.game.entity.player.GamePlayer;

public class NoShootStrategy implements ShootingStrategy {

    GamePlayer player;

    public NoShootStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handleShooting() {
    }
}
