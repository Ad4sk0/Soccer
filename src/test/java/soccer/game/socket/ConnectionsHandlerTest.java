package soccer.game.socket;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.game.match.MatchThread;

import javax.websocket.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConnectionsHandlerTest {

    @Mock
    MatchesHandler matchesHandler;

    @Mock
    Session session;

    @Mock
    MatchThread matchThread;

    @Mock
    Logger logger;

    @InjectMocks
    ConnectionsHandler connectionsHandler;

    Long matchId = 1L;
    Long notExistingMachId = 2L;
    String sessionId = "3";

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(matchesHandler.getMatchThread(notExistingMachId)).thenThrow(new IllegalArgumentException());
        when(matchesHandler.getMatchThread(matchId)).thenReturn(matchThread);
        when(session.getId()).thenReturn(sessionId);
        doNothing().when(logger).warn(any());
    }

    @Test
    void shouldNotThrowIfConnectionsEqual0() {
        assertEquals(0, connectionsHandler.getConnectionsNumberToMatch(matchId));
    }

    @Test
    void shouldAppendConnectionToMatch() {
        connectionsHandler.appendConnectionToMatch(matchId, session);
        assertEquals(1, connectionsHandler.getConnectionsNumberToMatch(matchId));
        connectionsHandler.appendConnectionToMatch(matchId, mock(Session.class));
        assertEquals(2, connectionsHandler.getConnectionsNumberToMatch(matchId));
    }

    @Test
    void shouldNotThrowIfTryingToRemoveNonExistingConnection() {
        assertEquals(0, connectionsHandler.getConnectionsNumberToMatch(matchId));
        connectionsHandler.removeConnectionFromMatch(matchId, session);
        assertEquals(0, connectionsHandler.getConnectionsNumberToMatch(matchId));
    }

    @Test
    void shouldRemoveConnectionFromMatch() {
        connectionsHandler.appendConnectionToMatch(matchId, session);
        assertEquals(1, connectionsHandler.getConnectionsNumberToMatch(matchId));
        connectionsHandler.removeConnectionFromMatch(matchId, session);
        assertEquals(0, connectionsHandler.getConnectionsNumberToMatch(matchId));
        verify(matchThread).removeConnections(session);
    }
}