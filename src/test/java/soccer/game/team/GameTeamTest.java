package soccer.game.team;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.Formations;
import soccer.app.entities.formation.utils.PositionsAssigner;
import soccer.app.entities.player.Player;
import soccer.app.entities.player.PlayerStats;
import soccer.app.entities.team.ShirtSet;
import soccer.app.entities.team.Team;
import soccer.app.generators.RandomPlayerGenerator;
import soccer.game.entity.ball.Ball;
import soccer.game.match.GameMatch;
import soccer.models.positions.PlayingPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class GameTeamTest {
    GameTeam gameTeam;
    GameMatch gameMatch;
    Ball ball;
    int playersNumber;
    Team team;
    Formation formation;
    List<Player> players;

    @BeforeEach
    void init() {
        playersNumber = 0;
        gameMatch = mock(GameMatch.class);
        ball = mock(Ball.class);
        playersNumber = 0;
        team = new Team();
        team.addShirtSet(new ShirtSet(Color.red, Color.GREEN, Color.BLUE, Color.MAGENTA));
        formation = new Formation(Formations.FOUR_FOUR_TWO);
        team.setFormation(formation);
        players = new ArrayList<>();
        gameTeam = new GameTeam(team);
    }

    @Test
    void shouldPrepareMatchPlayers() {
        List<PlayingPosition> playingPositionList = formation.getPlayingPositionsList();
        playingPositionList.add(PlayingPosition.GK);
        playingPositionList.add(PlayingPosition.CB);
        playingPositionList.add(PlayingPosition.CAM);
        generateGameTeamFromPositions(playingPositionList);
        assertEquals(11, gameTeam.getPlayingPlayers().size());
        assertEquals(14, gameTeam.getAllPlayers().size());
    }

    @Test
    void shouldThrowErrorIfLessThan7Players() {
        List<PlayingPosition> playingPositionList = new ArrayList<>(List.of(
                PlayingPosition.GK,
                PlayingPosition.CB,
                PlayingPosition.CB,
                PlayingPosition.CM,
                PlayingPosition.CM,
                PlayingPosition.CF
        ));
        assertThrows(IllegalStateException.class, () -> {
            generateGameTeamFromPositions(playingPositionList);
        });
        init();
        playingPositionList.add(PlayingPosition.CF);
        generateGameTeamFromPositions(playingPositionList);
        assertEquals(7, gameTeam.getPlayingPlayers().size());
    }

    @Test
    void shouldThrowErrorIfTeamHasNoGoalkeeper() {
        List<PlayingPosition> playingPositionList = new ArrayList<>(List.of(
                PlayingPosition.CB,
                PlayingPosition.CB,
                PlayingPosition.CM,
                PlayingPosition.CM,
                PlayingPosition.CF,
                PlayingPosition.CF,
                PlayingPosition.CF,
                PlayingPosition.CF,
                PlayingPosition.CF
        ));
        assertThrows(IllegalStateException.class, () -> {
            generateGameTeamFromPositions(playingPositionList);
        });
    }

    @Test
    void shouldIncrementScore() {
        assertEquals(0, gameTeam.getScore());
        gameTeam.incrementScore();
        assertEquals(1, gameTeam.getScore());
        gameTeam.incrementScore();
        assertEquals(2, gameTeam.getScore());
        gameTeam.setScore(0);
        assertEquals(0, gameTeam.getScore());
    }

    private void generateGameTeamFromPositions(List<PlayingPosition> playingPositionList) {
        for (PlayingPosition playingPosition : playingPositionList) {
            addPlayerWithPosition(playingPosition);
        }
        team.setPlayers(players);
        PositionsAssigner positionsAssigner = new PositionsAssigner(formation, players);
        positionsAssigner.assignPlayersToPositions();
        gameTeam = new GameTeam(team);
        gameTeam.choosePlayingPlayers(gameMatch, ball);
    }

    private void addPlayerWithPosition(PlayingPosition playingPosition) {
        PlayerStats playerStats = RandomPlayerGenerator.generateRandomPlayerStats();
        Player player = new Player("test" + playersNumber, playingPosition, playersNumber, team, playerStats);
        players.add(player);
        playersNumber++;
    }
}