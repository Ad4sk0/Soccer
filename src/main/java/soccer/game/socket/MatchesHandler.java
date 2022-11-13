package soccer.game.socket;

import org.jboss.logging.Logger;
import soccer.app.entities.match.Match;
import soccer.game.match.MatchThread;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MatchesHandler {

    private static final String INVALID_ID_ERROR_MSG = "Match thread for match with id %s doesn't exist";
    private final Map<Long, MatchThread> matches = new HashMap<>();
    @Inject
    Logger logger;

    public Map<Long, MatchThread> getMatches() {
        return matches;
    }

    public void startMatchThread(Match match) {
        if (isMatchRunning(match.getId())) {
            logger.error("Cannot start match with id " + match.getId() + " - is already running");
            return;
        }
        MatchThread matchThread = new MatchThread(match);
        matches.put(match.getId(), matchThread);
        matchThread.start();
    }

    public MatchThread getMatchThread(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        return matches.get(matchId);
    }

    public void removeMatchThread(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        stopMatchThread(matchId);
        matches.remove(matchId);
    }

    public void stopMatchThread(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        matches.get(matchId).interrupt();
    }

    public int getCurrentMatchThreadsNumber() {
        return matches.size();
    }

    public boolean isMatchRunning(Long matchId) {
        return matches.containsKey(matchId);
    }

    public void startMatch(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        matches.get(matchId).startMatch();
    }

    public void resumeMatch(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        matches.get(matchId).resumeMatch();
    }

    public void pauseMatch(Long matchId) {
        if (!isMatchRunning(matchId)) {
            throw new IllegalArgumentException(INVALID_ID_ERROR_MSG.formatted(matchId));
        }
        matches.get(matchId).pauseMatch();
    }
}
