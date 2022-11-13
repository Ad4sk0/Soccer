package soccer.app.entities.formation.utils;

import org.jboss.logging.Logger;
import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.player.Player;
import soccer.models.positions.PlayingPosition;
import soccer.models.positions.PositionType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionsAssigner {

    final Formation formation;
    final List<Player> players;

    @Inject
    Logger logger;

    Map<Integer, Map<Player, Integer>> pointsMap;

    public PositionsAssigner(Formation formation, List<Player> players) {
        this.formation = formation;
        this.players = players;
    }

    public void assignPlayersToPositions() {
        prepareScoresForPositions();
        assignPlayersToPositionsBasedOnScores();
        assertElevenPlayersWithAssignedPosition();
    }

    private void prepareScoresForPositions() {
        pointsMap = new HashMap<>();
        int idx = 0;
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            PlayingPosition position = formationPosition.getPosition();

            Map<Player, Integer> playersToPointsMap = new HashMap<>();
            for (Player player : players) {
                int points = 0;

                // Position Matches
                if (player.getPreferredPosition() == position) {
                    points += 100_000;
                }

                // Same Position Type
                else if (player.getPreferredPosition().getType() == position.getType()) {
                    points += 10_000;
                }

                // Similar position type
                else if (isPositionSimilar(player.getPreferredPosition(), position)) {
                    points += 1_000;
                }

                // Neither of positions is gk
                else if (player.getPreferredPosition() != PlayingPosition.GK && position != PlayingPosition.GK) {
                    points += 100;
                }

                points += player.getOverall();
                playersToPointsMap.put(player, points);
            }
            pointsMap.put(idx, playersToPointsMap);
            idx++;
        }
    }

    private void assignPlayersToPositionsBasedOnScores() {
        int chooseAboveScore = 100_000;
        int positionsToAssign = 11;

        while (chooseAboveScore > 1) {
            // System.out.println(chooseAboveScore);
            for (Map.Entry<Integer, Map<Player, Integer>> IdxToPlayerScoresEntry : pointsMap.entrySet()) {
                List<Map.Entry<Player, Integer>> sortedPlayersToScoresEntryList = sortPlayersByPoints(IdxToPlayerScoresEntry.getValue());
                Integer formationIdx = IdxToPlayerScoresEntry.getKey();
                FormationPosition currentFormationPosition = formation.getPositionsList().get(formationIdx);

                // Ignore already assigned positions
                if (currentFormationPosition.isTaken()) continue;
                // System.out.println(currentFormationPosition);
                boolean positionAssigned = choosePlayerForPosition(sortedPlayersToScoresEntryList, chooseAboveScore, currentFormationPosition);

                // End earlier
                if (positionAssigned) positionsToAssign--;
                if (positionsToAssign == 0) return;
            }

            // Reduce score boundary
            chooseAboveScore /= 10;
        }
    }

    private boolean choosePlayerForPosition(List<Map.Entry<Player, Integer>> sortedPlayersToScoresEntryList, int chooseAboveScore, FormationPosition currentFormationPosition) {
        // Iterate players together with their scores for current position - from best to worst
        for (Map.Entry<Player, Integer> PlayerToScoreEntry : sortedPlayersToScoresEntryList) {
            Player player = PlayerToScoreEntry.getKey();
            Integer score = PlayerToScoreEntry.getValue();
            if (score > chooseAboveScore && player.getAssignedPosition() == null) {
                // Assign player to position
                currentFormationPosition.assignPlayer(player);
                return true;
            }
        }
        return false;
    }

    private List<Map.Entry<Player, Integer>> sortPlayersByPoints(Map<Player, Integer> map) {
        return map.entrySet().stream().sorted(Map.Entry.<Player, Integer>comparingByValue().reversed()).toList();
    }

    private void assertElevenPlayersWithAssignedPosition() {
        int playersWithAssignedPosition = 0;
        for (Player player : players) {
            if (player.getAssignedPosition() != null) {
                playersWithAssignedPosition++;
            }
        }
        if (
                (players.size() >= 11 && playersWithAssignedPosition != 11)
                        || (players.size() < 11 && playersWithAssignedPosition != players.size())) {
            throw new ArithmeticException("Failed to assign players to positions. Number of players with assigned position: " + playersWithAssignedPosition);
        }
    }

    private boolean isPositionSimilar(PlayingPosition pos1, PlayingPosition pos2) {
        List<PositionType> similarPositions = new ArrayList<>();
        if (pos1.getType() == PositionType.DF) {
            similarPositions.add(PositionType.MF);
        } else if (pos1.getType() == PositionType.MF) {
            similarPositions.add(PositionType.DF);
            similarPositions.add(PositionType.FW);
        } else if (pos1.getType() == PositionType.FW) {
            similarPositions.add(PositionType.MF);
        }
        return similarPositions.contains(pos2.getType());
    }
}
