package soccer.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Vector2dTest {

    private static List<double[]> provideTestCasesForMultiply() {
        return List.of(
                new double[]{2, 4, 4, 8, 8, 32},
                new double[]{0, 0, 0, 0, 0, 0},
                new double[]{-1.2, -2.3, -3.2, -4.3, 3.84, 9.89},
                new double[]{6.53, 3.21, 20.30, 30.31, 132.56, 97.30},
                new double[]{2, 4, -1, 1, -2, 4},
                new double[]{2, 4, 1, -1, 2, -4}
        );
    }

    private static List<double[]> provideTestCasesForMultiplyByScalar() {
        return List.of(
                new double[]{2, 4, 4, 8, 16},
                new double[]{0, 0, 0, 0, 0},
                new double[]{-1.2, -2.3, 2.4, -2.88, -5.52},
                new double[]{6.53, 3.21, 30.31, 197.92, 97.30},
                new double[]{2, 4, -1, -2.0, -4.0},
                new double[]{2, 4, 1, 2.0, 4.0}
        );
    }

    private static List<double[]> provideTestCasesForLength() {
        return List.of(
                new double[]{2, 4, 4.47},
                new double[]{0, 0, 0},
                new double[]{-1.2, -2.3, 2.59},
                new double[]{6.53, 3.21, 7.27},
                new double[]{-2, 4, 4.47},
                new double[]{2, -4, 4.47}
        );
    }

    private static List<double[]> provideTestCasesForNormalization() {
        return List.of(
                new double[]{2, 4, 0.447, 0.894},
                new double[]{0, 0, 0, 0},
                new double[]{-1.2, -2.3, -0.463, -0.887},
                new double[]{6.53, 3.21, 0.897, 0.441},
                new double[]{-2, 4, -0.447, 0.894},
                new double[]{2, -4, 0.447, -0.894}
        );
    }

    private static List<double[]> provideTestCasesForReverse() {
        return List.of(
                new double[]{2, 4, -2, -4},
                new double[]{0, 0, 0, 0},
                new double[]{-1.2, -2.3, 1.2, 2.3},
                new double[]{6.53, 3.21, -6.53, -3.21},
                new double[]{-2, 4, 2, -4},
                new double[]{2, -4, -2, 4}
        );
    }

    private static Stream<Arguments> convertTestCasesToCalculateVectorsParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Vector2d(testCase[0], testCase[1]),
                            new Vector2d(testCase[2], testCase[3]),
                            new Vector2d(testCase[4], testCase[5])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateVectorsParamsWithScalar(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Vector2d(testCase[0], testCase[1]),
                            testCase[2],
                            new Vector2d(testCase[3], testCase[4])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateLength(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Vector2d(testCase[0], testCase[1]),
                            testCase[2]
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToTwoVectorsParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Vector2d(testCase[0], testCase[1]),
                            new Vector2d(testCase[2], testCase[3])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> provideVectorsForMultiply() {
        return convertTestCasesToCalculateVectorsParams(provideTestCasesForMultiply());
    }

    private static Stream<Arguments> provideVectorsForMultiplyByScalar() {
        return convertTestCasesToCalculateVectorsParamsWithScalar(provideTestCasesForMultiplyByScalar());
    }

    private static Stream<Arguments> provideVectorsForLength() {
        return convertTestCasesToCalculateLength(provideTestCasesForLength());
    }

    private static Stream<Arguments> provideVectorsForNormalization() {
        return convertTestCasesToTwoVectorsParams(provideTestCasesForNormalization());
    }

    private static Stream<Arguments> provideVectorsForReverse() {
        return convertTestCasesToTwoVectorsParams(provideTestCasesForReverse());
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForMultiply")
    void shouldMultiplyTwoVectors(Vector2d vector1, Vector2d vector2, Vector2d expected) {
        Vector2d actual = Vector2d.multiply(vector1, vector2);
        assertEquals(expected.getX(), actual.getX(), 0.01);
        assertEquals(expected.getY(), actual.getY(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForMultiplyByScalar")
    void shouldMultiplyVectorByScalarStatically(Vector2d vector1, double scalar, Vector2d expected) {
        Vector2d actual = Vector2d.multiply(vector1, scalar);
        assertEquals(expected.getX(), actual.getX(), 0.01);
        assertEquals(expected.getY(), actual.getY(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForMultiplyByScalar")
    void shouldMultiplyVectorByScalar(Vector2d vector1, double scalar, Vector2d expected) {
        vector1.multiply(scalar);
        assertEquals(expected.getX(), vector1.getX(), 0.01);
        assertEquals(expected.getY(), vector1.getY(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForLength")
    void shouldCalculateLength(Vector2d vector1, double expected) {
        double actual = vector1.length();
        assertEquals(expected, actual, 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForNormalization")
    void shouldNormalize(Vector2d vector, Vector2d expected) {
        vector.normalize();
        assertEquals(expected.getX(), vector.getX(), 0.001);
        assertEquals(expected.getY(), vector.getY(), 0.001);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForReverse")
    void shouldReverseOnXAndY(Vector2d vector, Vector2d expected) {
        vector.reverseX();
        vector.reverseY();
        assertEquals(expected.getX(), vector.getX(), 0.01);
        assertEquals(expected.getY(), vector.getY(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForReverse")
    void shouldBeSetTo0(Vector2d vector, Vector2d expected) {
        vector.setTo0();
        assertEquals(0, vector.getX());
        assertEquals(0, vector.getY());
    }
}