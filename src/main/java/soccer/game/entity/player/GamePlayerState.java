package soccer.game.entity.player;

public enum GamePlayerState {
    IS_IDLE(true),
    IS_LOOKING_FOR_POSITION(true),
    IS_ATTACKING(true),
    IS_DEFENDING(true),

    IS_PERFORMING_ANIMATION(false),
    IS_ANIMATION_READY(false),
    IS_AWAITING_PASS(false);

    final boolean isPlayingState;

    GamePlayerState(boolean isPlayingState) {
        this.isPlayingState = isPlayingState;
    }
}
