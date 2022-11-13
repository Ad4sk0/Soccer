package soccer.game.socket;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import soccer.app.entities.match.Match;
import soccer.app.entities.team.Team;
import soccer.game.match.MatchThread;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MatchesHandlerTest {

    final String team1Name = "testName1";
    final String team2Name = "testName2";
    Long matchId = 3L;
    @Mock
    Match match;

    @Mock
    Logger logger;

    Team team1;
    Team team2;

    @InjectMocks
    MatchesHandler matchesHandler;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        team1 = createTeam();
        team2 = createTeam();
        when(match.getTeamHome()).thenReturn(team1);
        when(match.getTeamAway()).thenReturn(team2);
        when(match.getId()).thenReturn(matchId);
        doNothing().when(logger).error(any());
    }

    private Team createTeam() {
        Team team = new Team();
        team.initDefault();
        return team;
    }

    @Test
    void shouldCreateMatchThreadForMatch() {
        matchesHandler.startMatchThread(match);
        assertEquals(1, matchesHandler.getMatches().size());
        assertEquals(1, matchesHandler.getCurrentMatchThreadsNumber());
    }

    @Test
    void shouldPreventCreationOfDuplicateThreadsForMatch() {
        matchesHandler.startMatchThread(match);
        assertEquals(1, matchesHandler.getMatches().size());
        matchesHandler.startMatchThread(match);
        assertEquals(1, matchesHandler.getMatches().size());
        verify(matchesHandler.logger).error(any());
    }

    @Test
    void shouldInterruptThreadBeforeRemovingMatchThread() {
        matchesHandler.startMatchThread(match);
        assertTrue(matchesHandler.isMatchRunning(matchId));
        assertEquals(1, matchesHandler.getMatches().size());
        MatchThread matchThread = matchesHandler.getMatches().get(matchId);
        matchesHandler.removeMatchThread(matchId);
        assertEquals(0, matchesHandler.getMatches().size());
        assertTrue(matchThread.isInterrupted());
        assertFalse(matchesHandler.isMatchRunning(matchId));
    }

    @Test
    void shouldHandleProvidingWrongMatchId() {
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.getMatchThread(1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.removeMatchThread(1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.stopMatchThread(1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.startMatch(1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.pauseMatch(1L);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            matchesHandler.resumeMatch(1L);
        });
    }

    @Test
    void shouldStartPauseAndResumeMatch() {
        matchesHandler.startMatchThread(match);
    }
}