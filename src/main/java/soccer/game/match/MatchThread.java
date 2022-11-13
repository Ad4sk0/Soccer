package soccer.game.match;

import org.jboss.logging.Logger;
import soccer.app.entities.match.Match;
import soccer.game.socket.payload.Payload;
import soccer.game.socket.payload.StateMessage;
import soccer.game.socket.payload.StateMessageHeader;
import soccer.game.socket.payload.data.MatchState;
import soccer.game.socket.payload.data.MatchStateInitial;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MatchThread extends Thread {

    private static final int FPS_TARGET = 60;
    private static final long FRAME_TARGET_DURATION = 1000000000 / FPS_TARGET;
    private final ConcurrentLinkedQueue<Session> sessionsConnected;
    private final Match match;
    private final GameMatch gameMatch;
    private final Logger logger = Logger.getLogger(getClass());
    private boolean running = true;


    public MatchThread(Match match) {
        String matchTitle = match.getTeamHomeName() + " vs. " + match.getTeamAway().getName();
        logger.debug("Match thread started for match " + matchTitle);
        this.match = match;
        this.gameMatch = new GameMatch(match);
        this.gameMatch.init();
        sessionsConnected = new ConcurrentLinkedQueue<>();
    }

    public void addConnection(Session session) {
        if (sessionsConnected.contains(session)) {
            logger.error("Trying to add the same session multiple times. Ignoring. " + session);
            return;
        }
        sessionsConnected.add(session);
    }

    public void removeConnections(Session session) {
        sessionsConnected.remove(session);
    }

    @Override
    public void run() {
        // Count FPS
        int framesN = 0;
        long fpsCounterTime = 0;
        long fpsWaitingTime = 0;

        while (running) {
            // Start Frame
            long frameStartTime = System.nanoTime();

            // Update Game
            gameMatch.update();

            // Send state to all connections
            sendMatchStateToAllConnections();

            // End Frame
            long frameEndTime = System.nanoTime();
            framesN++;

            // Limit FPS
            limitFps(frameStartTime, frameEndTime);

            // Count FPS
            long frameRealTime = System.nanoTime() - frameStartTime;
            fpsCounterTime += frameRealTime;
            long waitingTime = frameRealTime - (frameEndTime - frameStartTime);
            fpsWaitingTime += waitingTime;
            if (fpsCounterTime > 1000000000) {
                long avgWaitingTime = fpsWaitingTime / framesN;

                if (framesN < 50) {
                    logger.warn("Low FPS: " + framesN + " (avg waiting time: " + nanoToMs(avgWaitingTime) + " " +
                            "ms.)");
                }

                fpsCounterTime = 0;
                framesN = 0;
                fpsWaitingTime = 0;
            }
        }
    }

//    private String timeString(long timeNano) {
//        return nanoToMs(timeNano) + "ms. (" + timeNano + " ns.)";
//    }

    private void limitFps(long frameStartedTime, long frameEndTime) {
        long frameDuration = frameEndTime - frameStartedTime;
        long timeToWait = FRAME_TARGET_DURATION - frameDuration;
        timeToWait = Math.max(timeToWait, 0);
        long wakeUpTime = System.nanoTime() + timeToWait;
        //logger.debug("FRAME DURATION: " + timeString(frameDuration) + ". TIME TO WAIT: " + timeString(timeToWait));
        while (System.nanoTime() < wakeUpTime) {
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                logger.debug("Thread for match with id " + match.getId() + " interrupted: " + e.getMessage());
                running = false;
                this.interrupt();
                break;
            }
        }
    }

    private long nanoToMs(long value) {
        return value / 1000000;
    }

    public void sentInitialMatchState(Session session) {
        StateMessage matchStateInitial = new StateMessage(StateMessageHeader.MATCH_STATE_INITIAL_HEADER, new MatchStateInitial(gameMatch));
        sentMessage(session, matchStateInitial);
        StateMessage matchState = new StateMessage(StateMessageHeader.MATCH_STATE_HEADER, new MatchState(gameMatch));
        sentMessage(session, matchState);
    }

    private void sendMatchStateToAllConnections() {
        if (sessionsConnected.isEmpty()) {
            return;
        }
        Payload msg = new StateMessage(StateMessageHeader.MATCH_STATE_HEADER, new MatchState(gameMatch));
        sendMessageToAllConnections(msg);
    }

    private void sendMessageToAllConnections(Payload msg) {
        // Send state to all connections
        for (Session session : sessionsConnected) {
            sentMessage(session, msg);
        }
    }

    public Queue<Session> getSessionsConnected() {
        return sessionsConnected;
    }

    private void sentMessage(Session session, Payload msg) {
        try {
            session.getBasicRemote().sendObject(msg);
        } catch (IOException | EncodeException e) {
            logger.error("Unable to sent message to websocket session:" + e);
        }
    }

    public void startMatch() {
        logger.debug("Starting match");
        gameMatch.startMatch();
    }

    public void resumeMatch() {
        logger.debug("Resuming match");
        gameMatch.setPaused(false);
    }

    public void pauseMatch() {
        logger.debug("Pausing match");
        gameMatch.setPaused(true);
    }

    public GameMatch getGameMatch() {
        return gameMatch;
    }


}