package soccer.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeomUtilsTest {

    private static List<double[]> provideTestCases() {
        return List.of(
                new double[]{0, 0, 0, -1, -1.570, 0, -1},
                new double[]{0, 0, 1, -1, -0.785, 0.707, -0.707},
                new double[]{0, 0, 1, 0, 0, 1, 0},
                new double[]{0, 0, 1, 1, 0.785, 0.707, 0.707},
                new double[]{0, 0, 0, 1, 1.570, 0, 1},
                new double[]{0, 0, -1, 1, 2.356, -0.707, 0.707},
                new double[]{0, 0, -1, 0, 3.141, -1, 0},
                new double[]{0, 0, -1, -1, -2.356, -0.707, -0.707}
        );
    }

    private static Stream<Arguments> convertTestCasesToParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(testCase[0], testCase[1]),
                            new Position(testCase[2], testCase[3]),
                            testCase[4],
                            new Vector2d(testCase[5], testCase[6])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> provideParamsForTests() {
        return convertTestCasesToParams(provideTestCases());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTests")
    void shouldCalculateAngleTowardsPosition(Position initialPosition, Position targetPosition, double expectedAngle, Vector2d expectedDirection) {
        double actualAngle = GeomUtils.calculateAngleTowardsPosition(initialPosition, targetPosition);
        assertEquals(expectedAngle, actualAngle, 0.001);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForTests")
    void shouldCalculateDirectionTowardsPosition(Position initialPosition, Position targetPosition, double expectedAngle, Vector2d expectedDirection) {
        Vector2d actualDirection = GeomUtils.calculateDirectionTowardsPosition(initialPosition, targetPosition);
        assertEquals(expectedDirection.getX(), actualDirection.getX(), 0.001);
        assertEquals(expectedDirection.getY(), actualDirection.getY(), 0.001);
    }
}