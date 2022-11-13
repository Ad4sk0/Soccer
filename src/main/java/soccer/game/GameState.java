package soccer.game;

public enum GameState {

    INITIALIZING,
    INITIALIZED,
    PLAYING,
    PAUSED,
    END,

    CORNER_ANIMATION_READY, // Remove?

    /**
     * This state is turned on after end of match half-time. Is on until all players are on new initial positions.
     * Following state is START_FROM_THE_MIDDLE_ANIMATION.
     */
    BREAK,

    /**
     * When players all are on initial positions and are ready to perform first pass from the middle.
     * This state is turned on at the very beginning of the match and after goal animations.
     */
    START_FROM_THE_MIDDLE_ANIMATION,

    /**
     * This state is turned on after goal event. Is on until all players are on initial positions.
     * Following state is START_FROM_THE_MIDDLE_ANIMATION.
     */
    GOAL_ANIMATION,

    /**
     * This state is turned on after missed shot. Is on until all players are on resume by gk positions.
     * Following state is START_FROM_THE_MIDDLE_ANIMATION
     */
    RESUME_BY_GK_ANIMATION,

    /**
     * This state is turned on corners. Is on until all players are on corner positions.
     * Following state is PLAYING
     */
    CORNER_ANIMATION,

    /**
     *
     */
    OUT_ANIMATION,
}
