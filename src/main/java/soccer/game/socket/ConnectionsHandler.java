package soccer.game.socket;

import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ConnectionsHandler {

    //private static final String INVALID_ID_ERROR_MSG = "There are no connections added for match with id %s";
    private final Map<Long, ArrayList<Session>> matchesConnections = new HashMap<>();
    @Inject
    MatchesHandler matchesHandler;
    @Inject
    Logger logger;

//    public Map<Long, ArrayList<Session>> getMatchesConnections() {
//        return matchesConnections;
//    }

    public void appendConnectionToMatch(Long matchId, Session session) {
        if (matchesConnections.containsKey(matchId)) {
            matchesConnections.get(matchId).add(session);
        } else {
            matchesConnections.put(matchId, new ArrayList<>(List.of(session)));
        }
        matchesHandler.getMatchThread(matchId).addConnection(session);
    }

    public void removeConnectionFromMatch(Long matchId, Session session) {
        if (matchesConnections.get(matchId) == null) {
            logger.warn("Trying to remove connection from match " + matchId + "but there is no connections.");
            return;
        }
        matchesConnections.get(matchId).remove(session);
        if (matchesConnections.get(matchId).isEmpty()) {
            matchesConnections.remove(matchId);
        }
        matchesHandler.getMatchThread(matchId).removeConnections(session);
    }

    public int getConnectionsNumberToMatch(Long matchId) {
        if (matchesConnections.get(matchId) == null) {
            return 0;
        }
        return matchesConnections.get(matchId).size();
    }
}
