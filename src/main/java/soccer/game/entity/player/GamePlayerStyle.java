package soccer.game.entity.player;

public enum GamePlayerStyle {
    SUPER_DEFENSIVE(-100),
    DEFENSIVE(-50),
    NEUTRAL(0),
    OFFENSIVE(50),
    SUPPER_OFFENSIVE(100);

    final int positionCorrection;

    GamePlayerStyle(int positionCorrection) {
        this.positionCorrection = positionCorrection;
    }
}
