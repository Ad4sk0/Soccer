package soccer.game.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.app.entities.match.Match;
import soccer.app.entities.team.Team;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertTrue;

class MatchThreadTest {

    Match match;
    MatchThread matchThread;
    Team team1;
    Team team2;
    Session session;

    @BeforeEach
    void init() throws EncodeException, IOException {
        match = mock(Match.class);
        RemoteEndpoint.Basic remote = mock(RemoteEndpoint.Basic.class);
        team1 = createTeam();
        team2 = createTeam();
        when(match.getTeamHome()).thenReturn(team1);
        when(match.getTeamAway()).thenReturn(team2);
        matchThread = new MatchThread(match);
        session = mock(Session.class);
        when(session.getBasicRemote()).thenReturn(remote);
        doNothing().when(remote).sendObject(any());
    }

    private Team createTeam() {
        Team team = new Team();
        team.initDefault();
        return team;
    }

    @Test
    void shouldAddConnection() {
        assertEquals(0, matchThread.getSessionsConnected().size());
        matchThread.addConnection(mock(Session.class));
        assertEquals(1, matchThread.getSessionsConnected().size());
        matchThread.addConnection(mock(Session.class));
        assertEquals(2, matchThread.getSessionsConnected().size());
    }

    @Test
    void shouldNotAddTheSameConnectionTwice() {
        assertEquals(0, matchThread.getSessionsConnected().size());
        matchThread.addConnection(session);
        assertEquals(1, matchThread.getSessionsConnected().size());
        matchThread.addConnection(session);
        assertEquals(1, matchThread.getSessionsConnected().size());
    }

    @Test
    void shouldRemoveConnection() {
        Session session1 = mock(Session.class);
        Session session2 = mock(Session.class);
        assertEquals(0, matchThread.getSessionsConnected().size());
        matchThread.addConnection(session1);
        assertEquals(1, matchThread.getSessionsConnected().size());
        matchThread.addConnection(session2);
        assertEquals(2, matchThread.getSessionsConnected().size());
        matchThread.removeConnections(session2);
        assertEquals(1, matchThread.getSessionsConnected().size());
    }

    @Test
    void shouldSendInitialMessage() throws EncodeException, IOException {
        matchThread.startMatch();
        matchThread.addConnection(session);
        matchThread.sentInitialMatchState(session);
        verify(session.getBasicRemote(), times(2)).sendObject(any());
    }

    @Test
    void shouldSendMatchStateDuringMatch() throws EncodeException, IOException, InterruptedException {
        matchThread.startMatch();
        matchThread.addConnection(session);
        matchThread.start();
        Thread.sleep(100);
        matchThread.interrupt();
        verify(session.getBasicRemote(), atLeast(1)).sendObject(any());
    }

    @Test
    void shouldStopWhenInterrupted() {
        matchThread.start();
        matchThread.interrupt();
        assertTrue(matchThread.isInterrupted());
    }

    @Test
    void shouldStartPauseAndResumeMatch() {
        matchThread.startMatch();
        assertFalse(matchThread.getGameMatch().isPaused());
        matchThread.pauseMatch();
        assertTrue(matchThread.getGameMatch().isPaused());
        matchThread.resumeMatch();
        assertFalse(matchThread.getGameMatch().isPaused());
    }
}