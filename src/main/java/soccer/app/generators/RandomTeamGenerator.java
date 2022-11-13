package soccer.app.generators;

import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.player.Player;
import soccer.app.entities.team.Team;
import soccer.models.positions.PlayingPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomTeamGenerator {

    private static final Random random = new Random();

    private RandomTeamGenerator() {
    }

    public static List<Player> createRandomTeamPlayers(Team team, Formation formation, int additionalPlayersNumber) {
        // Assert formation is correct
        if (formation.getPositionsList().size() != 11) {
            throw new IllegalStateException("Given formation doesn't have 11 positions!");
        }

        // Generate numbers
        ArrayList<Integer> availableNumbers = new ArrayList<>(IntStream.rangeClosed(1, 11 + additionalPlayersNumber + 10).boxed().toList());

        // Needed positions
        List<PlayingPosition> positionsToGenerate = new ArrayList<>();
        boolean isGoalkeeperPresent = false;
        for (FormationPosition formationPosition : formation.getPositionsList()) {
            if (formationPosition.getPosition() == PlayingPosition.GK) {
                if (isGoalkeeperPresent) {
                    throw new IllegalStateException("Formation cannot have more than 1 goalkeeper!");
                }
                isGoalkeeperPresent = true;
                continue;
            }
            positionsToGenerate.add(formationPosition.getPosition());
        }

        // Result list
        List<Player> players = new ArrayList<>();

        // Each team has GK with number 1
        availableNumbers.remove(0);
        players.add(RandomPlayerGenerator.generateRandomPlayer(team, 1, PlayingPosition.GK));

        // Generate all necessary positions
        for (PlayingPosition position : positionsToGenerate) {
            int idx = random.nextInt(availableNumbers.size());
            int number = availableNumbers.get(idx);
            availableNumbers.remove(idx);
            players.add(RandomPlayerGenerator.generateRandomPlayer(team, number, position));
        }

        // Randomly generate additional players
        for (int i = 0; i < additionalPlayersNumber; i++) {
            PlayingPosition position = PlayingPosition.values()[random.nextInt(PlayingPosition.values().length)];
            int idx = random.nextInt(availableNumbers.size());
            int number = availableNumbers.get(idx);
            availableNumbers.remove(idx);
            players.add(RandomPlayerGenerator.generateRandomPlayer(team, number, position));
        }

        return players;
    }
}
