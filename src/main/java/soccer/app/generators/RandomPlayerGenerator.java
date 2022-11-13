package soccer.app.generators;

import soccer.app.entities.player.Player;
import soccer.app.entities.player.PlayerStats;
import soccer.app.entities.team.Team;
import soccer.models.positions.PlayingPosition;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomPlayerGenerator {

    private static final Random random = new Random();
    private static final List<String> firstNames = Arrays.asList(
            "James", "John", "Joe", "Richard", "David", "William", "Mike", "Alexander", "Tom", "Brad", "Gabriel", "Ron"
    );
    private static final List<String> lastNames = Arrays.asList(
            "Smith", "Brown", "Lee", "Kowalski", "Nowak", "Watson", "Morrison", "Guana", "Bra", "Diaz", "Wittinson"
    );

    private RandomPlayerGenerator() {
    }

    public static PlayerStats generateRandomPlayerStats() {
        int minValue = 50;
        int maxValue = 100;

        int speed = random.nextInt(minValue, maxValue);
        int acceleration = random.nextInt(minValue, maxValue);
        int shotAccuracy = random.nextInt(minValue, maxValue);
        int passingAccuracy = random.nextInt(minValue, maxValue);
        int strength = random.nextInt(minValue, maxValue);
        int endurance = random.nextInt(minValue, maxValue);
        int shotPower = random.nextInt(minValue, maxValue);

        return new PlayerStats(
                speed,
                acceleration,
                shotAccuracy,
                passingAccuracy,
                strength,
                endurance,
                shotPower);
    }

    public static String generateRandomName() {
        String firstName = firstNames.get(random.nextInt(firstNames.size()));
        String lastName = lastNames.get(random.nextInt(lastNames.size()));
        return firstName + " " + lastName;
    }

    public static Player generateRandomPlayer(Team team, Integer number, PlayingPosition position) {
        String name = generateRandomName();
        PlayerStats playerStats = generateRandomPlayerStats();
        return new Player(name, position, number, team, playerStats);
    }
}
