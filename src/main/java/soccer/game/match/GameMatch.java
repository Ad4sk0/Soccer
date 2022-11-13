package soccer.game.match;

import org.jboss.logging.Logger;
import soccer.app.entities.match.Match;
import soccer.game.GameState;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.events.*;
import soccer.game.team.GameTeam;
import soccer.models.playingfield.FieldSite;
import soccer.models.playingfield.PlayingField;
import soccer.models.playingfield.PlayingFieldUtils;
import soccer.utils.Position;

import javax.enterprise.context.Dependent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class GameMatch {

    public static final int MATCH_HALF_DURATION_IN_MIN = 3;
    public static final int PLANNED_MATCH_DURATION_IN_SEC = MATCH_HALF_DURATION_IN_MIN * 2 * 60;
    public static final int FPS = 60;
    private final Match match;
    private final Logger logger = Logger.getLogger(getClass());
    private final List<GamePlayer> allPlayingPlayers = new ArrayList<>();
    private final MatchEventsTransmitter matchEventsTransmitter = new MatchEventsTransmitter();
    private final MatchEventsHandler matchEventsHandler = new MatchEventsHandler(this);
    private final MatchEventsChecker matchEventsChecker = new MatchEventsChecker(this);
    private GameState gameState;
    private List<GameTeam> gameTeams;
    private GameTeam leftSiteTeam;
    private GameTeam rightSiteTeam;
    private Ball ball;
    private MatchHalf currentHalf = MatchHalf.FIRST_HALF;
    private FieldSite lastGoalScoredBy;
    private FieldSite resumeByGkFieldSite;
    private FieldSite cornerPerformingFieldSite;
    private Position cornerPerformingPosition;
    private FieldSite startFromTheMiddleTeamSite;
    private int currentHalfDurationInFrames = 0;
    private int currentHalfAdditionalTimeInMinutes = 0;
    private boolean paused = true;
    private boolean matchInitialized = false;

    public GameMatch(Match match) {
        this.match = match;
        produceEvent(EventTypes.MATCH_CREATED);
    }

    public void init() {
        if (matchInitialized) {
            throw new IllegalStateException("Match already initialized");
        }
        updateStartFromTheMiddleTeamSite();
        prepareBall();
        registerTeams();
        prepareTeams();
        registerPlayers();
        preparePlayers();
        initializePositions();
        produceEvent(EventTypes.MATCH_INITIALIZED);
        matchInitialized = true;
    }

    private void updateStartFromTheMiddleTeamSite() {
        startFromTheMiddleTeamSite = FieldSite.LEFT;
        if (gameState == GameState.GOAL_ANIMATION && ball.getPosition().isBehindRightLine()) {
            startFromTheMiddleTeamSite = FieldSite.RIGHT;
        }
    }

    private void prepareBall() {
        ball = new Ball(this);
        initBallPosition();
    }

    private void initBallPosition() {
        ball.setInitialPosition(new Position(PlayingField.FIELD_WIDTH / 2, PlayingField.FIELD_HEIGHT / 2));
    }

    private void registerTeams() {
        gameTeams = new ArrayList<>();
        gameTeams.add(new GameTeam(match.getTeamHome()));
        gameTeams.add(new GameTeam(match.getTeamAway()));
        leftSiteTeam = gameTeams.get(0);
        rightSiteTeam = gameTeams.get(1);
    }

    private void prepareTeams() {
        for (GameTeam team : gameTeams) {
            team.prepareForMatch(this, ball);
        }
    }

    private void registerPlayers() {
        for (GameTeam gameTeam : gameTeams) {
            allPlayingPlayers.addAll(gameTeam.getPlayingPlayers());
        }
    }

    private void preparePlayers() {
        if (gameTeams.size() != 2) {
            throw new UnsupportedOperationException("Error: team size is not equal to 2 while initializing players for match");
        }
        // Initialize playing sites
        for (int i = 0; i < gameTeams.size(); i++) {
            for (GamePlayer player : gameTeams.get(i).getPlayingPlayers()) {
                if (i == 0) {
                    player.setFieldSite(FieldSite.LEFT);
                    player.setMyTeamPlayers(gameTeams.get(0).getPlayingPlayers());
                    player.setOpposedTeamPlayers(gameTeams.get(1).getPlayingPlayers());
                } else {
                    player.setFieldSite(FieldSite.RIGHT);
                    player.setMyTeamPlayers(gameTeams.get(1).getPlayingPlayers());
                    player.setOpposedTeamPlayers(gameTeams.get(0).getPlayingPlayers());
                }
            }
        }
    }

    private void initializePositions() {
        for (GameTeam team : gameTeams) {
            team.initPlayersPositions();
        }
    }

    public void produceEvent(EventTypes eventType) {
        matchEventsTransmitter.receiveEvent(new Event(eventType));
    }

    public void produceEvent(Event event) {
        matchEventsTransmitter.receiveEvent(event);
    }

    public void startMatch() {
        match.setMatchStartedTimeActual(LocalDateTime.now());
        produceEvent(EventTypes.MATCH_START);
        paused = false;
    }

    public void update() {
        if (isPaused()) {
            return;
        }
        handleMatchTime();
        // The order is important: player -> ball -> events
        for (GamePlayer player : allPlayingPlayers) {
            player.makeMove();
        }
        ball.makeMove();
        matchEventsChecker.checkMatchEvents();
    }

    private boolean isEndOfHalfTime() {
        return getCurrentHalfDurationInSeconds() >= (MATCH_HALF_DURATION_IN_MIN * 60) + currentHalfAdditionalTimeInMinutes * 60;
    }

    private void handleMatchTime() {
        if (gameState == GameState.BREAK || gameState == GameState.END) {
            return;
        }
        if (isEndOfHalfTime()) {
            // END OF HALF
            if (currentHalf == MatchHalf.FIRST_HALF) {
                produceEvent(EventTypes.END_FIRST_HALF);
                currentHalf = MatchHalf.SECOND_HALF;
                currentHalfDurationInFrames = 0;
                currentHalfAdditionalTimeInMinutes = 0;
                changePlayerSites();
            }
            // END OF MATCH
            else {
                produceEvent(EventTypes.END_SECOND_HALF);
            }
            return;
        }
        currentHalfDurationInFrames++;
    }

    public int getCurrentHalfDurationInSeconds() {
        return currentHalfDurationInFrames / FPS;
    }

    private void changePlayerSites() {
        GameTeam temp = leftSiteTeam;
        leftSiteTeam = rightSiteTeam;
        rightSiteTeam = temp;

        for (GamePlayer player : allPlayingPlayers) {
            if (player.getFieldSite() == FieldSite.LEFT) {
                player.setFieldSite(FieldSite.RIGHT);
            } else {
                player.setFieldSite(FieldSite.LEFT);
            }

            // TODO Change all positions
            player.setBasePosition(PlayingFieldUtils.moveXToOtherHalf(player.getBasePosition()));
            player.setResumeByFriendlyGkPosition(PlayingFieldUtils.moveXToOtherHalf(player.getResumeByFriendlyGkPosition()));
            player.setResumeByOppositeGkPosition(PlayingFieldUtils.moveXToOtherHalf(player.getResumeByOppositeGkPosition()));
            player.setCornerOnFriendlySitePosition(PlayingFieldUtils.moveXToOtherHalf(player.getCornerOnFriendlySitePosition()));
            player.setCornerOnOppositeSitePosition(PlayingFieldUtils.moveXToOtherHalf(player.getCornerOnOppositeSitePosition()));
        }
        startFromTheMiddleTeamSite = FieldSite.LEFT;
    }

    public void handleGoal() {
        updateLastGoalFieldSite();
        updateStartFromTheMiddleTeamSite();
        // Increment Score
        if (getLastGoalScoredBy() == FieldSite.LEFT) {
            getLeftSiteTeam().incrementScore();
        } else {
            getRightSiteTeam().incrementScore();
        }
        logger.debug(String.format("Current result: %d:%d", getLeftSiteTeam().getScore(), getRightSiteTeam().getScore()));
    }

    private void updateLastGoalFieldSite() {
        if (gameState != GameState.GOAL_ANIMATION) {
            logger.error("Update last goal field site requested however game state is not equal to goal. Possible error");
            return;
        }
        if (ball.getPosition().isBehindLeftLine()) {
            lastGoalScoredBy = FieldSite.RIGHT;
        } else if (ball.getPosition().isBehindRightLine()) {
            lastGoalScoredBy = FieldSite.LEFT;
        } else {
            throw new IllegalStateException("Unable to determine last goal scored site. Ball is not behind vertical line. Possible error");
        }

    }

    public void handleCorner() {
        if (gameState != GameState.CORNER_ANIMATION) {
            logger.error("Handle corner requested however game state is not equal to corner. Possible error");
            return;
        }
        determineCornerPerformingSite();
        determineCornerPerformingPosition();
    }

    private void determineCornerPerformingSite() {
        if (gameState != GameState.CORNER_ANIMATION) {
            logger.error("Determine corner site requested however game state is not equal to corner. Possible error");
            return;
        }
        if (ball.getPosition().isBehindLeftLine()) {
            cornerPerformingFieldSite = FieldSite.RIGHT;
        } else if (ball.getPosition().isBehindRightLine()) {
            cornerPerformingFieldSite = FieldSite.LEFT;
        } else {
            throw new IllegalStateException("Unable to determine corner site. Ball is not behind line. Possible error");
        }
    }

    private void determineCornerPerformingPosition() {
        if (gameState != GameState.CORNER_ANIMATION) {
            logger.error("Determine corner position requested however game state is not equal to corner. Possible error");
            return;
        }
        if (ball.getPosition().isBehindLeftLine() && ball.getY() < PlayingField.UPPER_POST_HEIGHT) {
            cornerPerformingPosition = PlayingField.LEFT_UPPER_CORNER;
        } else if (ball.getPosition().isBehindLeftLine() && ball.getY() > PlayingField.BOTTOM_POST_HEIGHT) {
            cornerPerformingPosition = PlayingField.LEFT_BOTTOM_CORNER;
        } else if (ball.getPosition().isBehindRightLine() && ball.getY() < PlayingField.UPPER_POST_HEIGHT) {
            cornerPerformingPosition = PlayingField.RIGHT_UPPER_CORNER;
        } else if (ball.getPosition().isBehindRightLine() && ball.getY() > PlayingField.BOTTOM_POST_HEIGHT) {
            cornerPerformingPosition = PlayingField.RIGHT_BOTTOM_CORNER;
        } else {
            throw new IllegalStateException("Unable to determine corner position. Ball is not behind line. Possible error");
        }
    }

    public void handleCornerPerformed() {
        cornerPerformingFieldSite = null;
        cornerPerformingPosition = null;
    }

    public void handleResumeByGk() {
        if (gameState != GameState.RESUME_BY_GK_ANIMATION) {
            logger.error("Handle resume by GK requested however game state is not equal to RESUME_BY_GK. Possible error");
            return;
        }
        if (ball.getPosition().isBehindLeftLine()) {
            resumeByGkFieldSite = FieldSite.LEFT;
        } else if (ball.getPosition().isBehindRightLine()) {
            resumeByGkFieldSite = FieldSite.RIGHT;
        } else {
            throw new IllegalStateException("Unable to determine resume by GK site. Ball is not behind vertical line. Possible error");
        }
    }

    public String getMatchDurationString() {
        int elapsedSeconds = getCurrentHalfDurationInSeconds();
        if (currentHalf == MatchHalf.SECOND_HALF) {
            elapsedSeconds += MATCH_HALF_DURATION_IN_MIN * 60;
        }
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        String secondsString = String.valueOf(seconds);
        String minutesString = String.valueOf(minutes);
        if (secondsString.length() == 1) {
            secondsString = "0" + secondsString;
        }
        if (minutesString.length() == 1) {
            minutesString = "0" + minutesString;
        }
        return minutesString + ":" + secondsString;
    }


    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public FieldSite getResumeByGkFieldSite() {
        return resumeByGkFieldSite;
    }

    public FieldSite getLastGoalScoredBy() {
        return lastGoalScoredBy;
    }

    public Ball getBall() {
        return ball;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        logger.debug("NEW GAME STATE: " + gameState);
        this.gameState = gameState;
    }

    public FieldSite getCornerPerformingFieldSite() {
        return cornerPerformingFieldSite;
    }

    public Position getCornerPerformingPosition() {
        return cornerPerformingPosition;
    }

    public List<GameTeam> getGameTeams() {
        return gameTeams;
    }

    public List<GamePlayer> getAllPlayingPlayers() {
        return allPlayingPlayers;
    }

    public GameTeam getLeftSiteTeam() {
        return leftSiteTeam;
    }

    public GameTeam getRightSiteTeam() {
        return rightSiteTeam;
    }

    public FieldSite getStartFromTheMiddleTeamSite() {
        return startFromTheMiddleTeamSite;
    }

    public MatchHalf getCurrentHalf() {
        return currentHalf;
    }

    public LocalDateTime getMatchStartedTimeActual() {
        return match.getMatchStartedTimeActual();
    }

    public void registerMatchEventsListener(MatchEventsListener matchEventsChecker) {
        matchEventsTransmitter.addListener(matchEventsChecker);
    }
}
