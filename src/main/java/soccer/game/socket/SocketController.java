package soccer.game.socket;

import org.jboss.logging.Logger;
import soccer.game.socket.decoding.MyDecoder;
import soccer.game.socket.decoding.MyEncoder;
import soccer.game.socket.payload.Payload;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(value = "/ws/game/match/{match_id}", encoders = MyEncoder.class, decoders = MyDecoder.class)
public class SocketController {

    @Inject
    ConnectionsHandler connectionsHandler;

    @Inject
    MatchesHandler matchesHandler;

    @Inject
    Logger logger;

    @OnOpen
    public void open(Session session, @PathParam("match_id") Long matchId) {
        logger.debug("New socket connection to match " + matchId);
        if (!matchesHandler.isMatchRunning(matchId)) {
            closeSession(session, CloseCodes.UNEXPECTED_CONDITION, "Match with given id doesn't run already");
            logger.warn("Match thread for match with id " + matchId + " doesn't exist");
            return;
        }
        connectionsHandler.appendConnectionToMatch(matchId, session);
        logger.debug("New connection added to match with id " + matchId + ". Current connections number:" + connectionsHandler.getConnectionsNumberToMatch(matchId));
        matchesHandler.getMatchThread(matchId).sentInitialMatchState(session);
    }

    @OnClose
    public void close(Session session, @PathParam("match_id") Long matchId) {
        if (matchesHandler.isMatchRunning(matchId)) {
            connectionsHandler.removeConnectionFromMatch(matchId, session);
        }
        logger.debug("Connection closed to match with id " + matchId + ". Current connections number:" + connectionsHandler.getConnectionsNumberToMatch(matchId));
    }

    @OnMessage
    public void handleMessage(Payload payload, Session session, @PathParam("match_id") Long matchId) {
        logger.debug("New message for match: " + matchId);
        logger.debug(payload);
    }

    private void closeSession(Session session, CloseCodes closeCode, String msg) {
        try {
            session.close(new CloseReason(closeCode, msg));
        } catch (IOException e) {
            logger.warn("IO Exception while closing connection");
        }
    }

}
