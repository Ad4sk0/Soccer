package soccer.game.socket.payload.data;

import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.Formations;
import soccer.app.entities.formation.utils.PositionsAssigner;
import soccer.app.entities.player.Player;
import soccer.app.entities.team.ShirtSet;
import soccer.app.entities.team.Team;
import soccer.app.generators.RandomTeamGenerator;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.match.GameMatch;
import soccer.game.team.GameTeam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MatchStateTestUtils {

    public static final int matchDurationInSec = 131;
    private static GameMatch gameMatch;
    private static List<GamePlayer> players;

    private static void setUp() {
        gameMatch = mock(GameMatch.class);
        GameTeam gameTeam = mock(GameTeam.class);
        ShirtSet shirtSet = mock(ShirtSet.class);
        Ball ball = mock(Ball.class);
        when(gameMatch.getMatchStartedTimeActual()).thenReturn(LocalDateTime.now());
        when(gameMatch.getGameTeams()).thenReturn(new ArrayList<>(List.of(gameTeam, gameTeam)));
        when(gameTeam.getName()).thenReturn("Test");
        when(gameTeam.getShirtSet()).thenReturn(shirtSet);
        when(shirtSet.getShirtColorAsHexString()).thenReturn("#000000");
        when(shirtSet.getNumberColorAsHexString()).thenReturn("#000000");
        when(shirtSet.getGoalkeeperShirtColorAsHexString()).thenReturn("#000000");
        when(shirtSet.getGoalkeeperNumberColorAsHexString()).thenReturn("#000000");
        when(ball.getX()).thenReturn(0D);
        when(ball.getY()).thenReturn(0D);
        when(gameMatch.getBall()).thenReturn(ball);
        when(gameMatch.getCurrentHalfDurationInSeconds()).thenReturn(matchDurationInSec);
        when(gameMatch.getMatchDurationString()).thenCallRealMethod();
        preparePlayers();
        when(gameTeam.getAllPlayers()).thenReturn(players);
        when(gameTeam.getPlayingPlayers()).thenReturn(players);
    }

    private static void preparePlayers() {
        Formation formation = new Formation(Formations.FOUR_FOUR_TWO);
        Team team = new Team();
        team.setFormation(formation);
        List<Player> playerList = RandomTeamGenerator.createRandomTeamPlayers(team, formation, 0);
        PositionsAssigner positionsAssigner = new PositionsAssigner(formation, playerList);
        positionsAssigner.assignPlayersToPositions();

        players = new ArrayList<>();
        for (Player player : playerList) {
            GamePlayer gamePlayer = new GamePlayer(player, mock(GameTeam.class), gameMatch, mock(Ball.class));
            players.add(gamePlayer);
        }
    }

    public static GameMatch getGameMatchMock() {
        setUp();
        return gameMatch;
    }
}
