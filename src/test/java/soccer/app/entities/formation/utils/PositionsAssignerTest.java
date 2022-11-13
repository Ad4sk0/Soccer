package soccer.app.entities.formation.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.formation.Formations;
import soccer.app.entities.player.Player;
import soccer.app.entities.player.PlayerStats;
import soccer.app.entities.team.Team;
import soccer.app.generators.RandomPlayerGenerator;
import soccer.models.positions.PlayingPosition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionsAssignerTest {

    Team team;
    Formation formation;
    List<Player> players;
    int playersNumber;

    @BeforeEach
    void init() {
        playersNumber = 0;
        team = new Team();
        formation = new Formation(Formations.FOUR_FOUR_TWO);
        team.setFormation(formation);
        players = new ArrayList<>();
        generateTeamWithAllMatchingPositions();
    }


    @Test
    void shouldAssignPlayersToPositionsWhenPlayerForEachPositionIsPresent() {
        assignPlayersToPositions();
        int playersWithAssignedPosition = 0;
        int playersWithoutAssignedPosition = 0;
        for (Player player : players) {
            if (player.getAssignedPosition() != null) {
                playersWithAssignedPosition++;
            } else {
                playersWithoutAssignedPosition++;
            }
        }
        assertEquals(11, playersWithAssignedPosition);
        assertEquals(0, playersWithoutAssignedPosition);
    }

    @Test
    void shouldAssignPlayerWithTheSamePositionType() {
        PlayingPosition positionToChange = PlayingPosition.LB;
        PlayingPosition newPosition = PlayingPosition.CB;
        addPlayerWithPosition(PlayingPosition.CM);
        PlayingPosition expectedPosition = PlayingPosition.CB;

        changePlayersPreferredPositions(positionToChange, newPosition);
        assignPlayersToPositions();
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition().equals(positionToChange)) {
                assertEquals(expectedPosition, formationPosition.getPlayer().getPreferredPosition());
            }
        }
    }

    @Test
    void shouldAssignPlayerWithSimilarPositionType() {
        PlayingPosition positionToChange = PlayingPosition.CF;
        PlayingPosition newPosition = PlayingPosition.CB;
        addPlayerWithPosition(PlayingPosition.CM);
        addPlayerWithPosition(PlayingPosition.CM);
        PlayingPosition expectedPosition = PlayingPosition.CM;

        changePlayersPreferredPositions(positionToChange, newPosition);
        assignPlayersToPositions();
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition().equals(positionToChange)) {
                assertEquals(expectedPosition, formationPosition.getPlayer().getPreferredPosition());
            }
        }
    }

    @Test
    void shouldAssignDifferentPositionBeforeGK() {
        PlayingPosition positionToChange = PlayingPosition.CF;
        PlayingPosition newPosition = PlayingPosition.GK;
        addPlayerWithPosition(PlayingPosition.GK);
        addPlayerWithPosition(PlayingPosition.CB);
        addPlayerWithPosition(PlayingPosition.CB);
        PlayingPosition expectedPosition = PlayingPosition.CB;

        changePlayersPreferredPositions(positionToChange, newPosition);
        assignPlayersToPositions();
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition().equals(positionToChange)) {
                assertEquals(expectedPosition, formationPosition.getPlayer().getPreferredPosition());
            }
        }
    }

    @Test
    void shouldAssignGK() {
        PlayingPosition positionToChange = PlayingPosition.CF;
        PlayingPosition newPosition = PlayingPosition.GK;
        addPlayerWithPosition(PlayingPosition.GK);
        PlayingPosition expectedPosition = PlayingPosition.GK;

        changePlayersPreferredPositions(positionToChange, newPosition);
        assignPlayersToPositions();
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition().equals(positionToChange)) {
                assertEquals(expectedPosition, formationPosition.getPlayer().getPreferredPosition());
            }
        }
    }

    @Test
    void shouldAssignToGK() {
        PlayingPosition positionToChange = PlayingPosition.GK;
        PlayingPosition newPosition = PlayingPosition.CF;
        PlayingPosition expectedPosition = PlayingPosition.CF;

        changePlayersPreferredPositions(positionToChange, newPosition);
        assignPlayersToPositions();
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition().equals(positionToChange)) {
                assertEquals(expectedPosition, formationPosition.getPlayer().getPreferredPosition());
            }
        }
    }

    void assignPlayersToPositions() {
        PositionsAssigner positionsAssigner = new PositionsAssigner(formation, players);
        positionsAssigner.assignPlayersToPositions();
    }

    void generateTeamWithAllMatchingPositions() {
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            PlayingPosition playingPosition = formationPosition.getPosition();
            addPlayerWithPosition(playingPosition);
        }
    }

    private void addPlayerWithPosition(PlayingPosition playingPosition) {
        PlayerStats playerStats = RandomPlayerGenerator.generateRandomPlayerStats();
        Player player = new Player("test" + playersNumber, playingPosition, playersNumber, team, playerStats);
        players.add(player);
        playersNumber++;
    }

    void changePlayersPreferredPositions(PlayingPosition fromPosition, PlayingPosition toPosition) {
        for (Player player : players) {
            if (player.getPreferredPosition().equals(fromPosition)) {
                player.setPreferredPosition(toPosition);
            }
        }
    }
}