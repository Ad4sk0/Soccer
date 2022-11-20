package soccer.game.entity.player.movement;

public interface MoveStrategy {
    void handleMovement();

    boolean isPlayingStrategy();
}
