package soccer.game.socket;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.game.match.MatchThread;
import soccer.game.socket.payload.Payload;

import javax.websocket.Session;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SocketControllerTest {

    @Mock
    Session session;

    @Mock
    MatchThread matchThread;

    @Mock
    ConnectionsHandler connectionsHandler;

    @Mock
    MatchesHandler matchesHandler;

    @Mock
    Logger logger;

    @InjectMocks
    SocketController socketController;

    Long matchId = 1L;
    Long notRunningMatchId = 2L;

    @BeforeEach
    void init() throws IOException {
        MockitoAnnotations.openMocks(this);
        doNothing().when(connectionsHandler).appendConnectionToMatch(any(), any());
        doNothing().when(connectionsHandler).removeConnectionFromMatch(any(), any());
        when(matchesHandler.getMatchThread(any())).thenReturn(matchThread);
        when(matchesHandler.isMatchRunning(matchId)).thenReturn(true);
        when(matchesHandler.isMatchRunning(notRunningMatchId)).thenReturn(false);
        doNothing().when(session).close(any());
    }

    @Test
    void shouldOpenConnectionToExistingMatch() {
        socketController.open(session, matchId);
        verify(connectionsHandler).appendConnectionToMatch(matchId, session);
        verify(matchThread).sentInitialMatchState(session);
    }

    @Test
    void shouldNotOpenConnectionIfMatchDoesNotExistAndClose() throws IOException {
        socketController.open(session, notRunningMatchId);
        verify(session).close(any());
    }

    @Test
    void shouldCloseConnectionToExistingMatch() {
        socketController.close(session, matchId);
        verify(connectionsHandler).removeConnectionFromMatch(matchId, session);
    }

    @Test
    void shouldHandleIncomingMessage() {
        Payload payload = mock(Payload.class);
        socketController.handleMessage(payload, session, matchId);
        verify(logger).debug(payload); // Temp
    }
}