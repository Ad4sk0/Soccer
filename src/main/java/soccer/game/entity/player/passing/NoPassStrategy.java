package soccer.game.entity.player.passing;

import soccer.game.entity.player.GamePlayer;

public class NoPassStrategy implements PassingStrategy {

    GamePlayer player;

    public NoPassStrategy(GamePlayer player) {
        this.player = player;
    }

    @Override
    public void handlePassing() {
    }

}
