package soccer.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import soccer.models.playingfield.PlayingFieldUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static soccer.models.playingfield.PlayingField.*;

class PositionTest {

    private static List<double[]> provideTestCasesForAdd() {
        return List.of(
                new double[]{2, 4, 4, 8, 6, 12},
                new double[]{0, 0, 0, 0, 0, 0},
                new double[]{-1.2, -2.3, -3.2, -4.3, -4.4, -6.7},
                new double[]{6.53, 3.21, 20.30, 30.31, 26.83, 33.52}
        );
    }

    private static List<double[]> provideTestCasesForSubtract() {
        return List.of(
                new double[]{2, 4, 4, 8, -2, -4},
                new double[]{0, 0, 0, 0, 0, 0},
                new double[]{-1.2, -2.3, -3.2, -4.3, 2, 2},
                new double[]{6.53, 3.21, 20.30, 30.31, -13.77, -27.1}
        );
    }

    private static List<double[]> provideTestCasesForDistance() {
        return List.of(
                new double[]{2, 4, 4, 8, 4.47},
                new double[]{0, 0, 0, 0, 0.00},
                new double[]{-1.2, -2.3, -3.2, -4.3, 2.83},
                new double[]{6.53, 3.21, 20.30, 30.31, 30.40}
        );
    }

    private static List<Object[]> provideTestCasesForOutsidePlayingField() {
        return List.of(

                // Outside playing field
                new Object[]{-1, 1, true},
                new Object[]{1, -1, true},
                new Object[]{FIELD_WIDTH + 1, 0, true},
                new Object[]{FIELD_WIDTH - 1, -1, true},
                new Object[]{1, -1, true},
                new Object[]{-1, 1, true},
                new Object[]{1, FIELD_HEIGHT + 1, true},
                new Object[]{-1, FIELD_HEIGHT - 1, true},

                // Inside playing field
                new Object[]{1, 1, false},
                new Object[]{FIELD_WIDTH - 1, 1, false},
                new Object[]{1, FIELD_HEIGHT - 1, false},
                new Object[]{FIELD_WIDTH - 1, FIELD_HEIGHT - 1, false},

                // On the line - inside playing field
                new Object[]{0, 0, false},
                new Object[]{FIELD_WIDTH, 0, false},
                new Object[]{0, FIELD_HEIGHT, false},
                new Object[]{FIELD_WIDTH, FIELD_HEIGHT, false}
        );
    }

    private static List<Object[]> provideTestCasesForFieldQuarter() {
        return List.of(
                new Object[]{0, 0, "left"},
                new Object[]{FIELD_WIDTH, 0D, "right"},
                new Object[]{0, 0, "upper"},
                new Object[]{0, FIELD_HEIGHT, "bottom"},

                new Object[]{FIELD_WIDTH / 2 - 1, 0, "left"},
                new Object[]{FIELD_WIDTH / 2 + 1, 0, "right"},
                new Object[]{0, FIELD_HEIGHT / 2 - 1, "upper"},
                new Object[]{0, FIELD_HEIGHT / 2 + 1, "bottom"},

                new Object[]{FIELD_WIDTH / 2 - 1, FIELD_HEIGHT, "left"},
                new Object[]{FIELD_WIDTH / 2 + 1, FIELD_HEIGHT, "right"},
                new Object[]{FIELD_WIDTH, FIELD_HEIGHT / 2 - 1, "upper"},
                new Object[]{FIELD_WIDTH, FIELD_HEIGHT / 2 + 1, "bottom"}
        );
    }

    private static List<Object[]> provideTestCasesForFieldQuarterOutsidePlayingField() {
        return List.of(
                new Object[]{-1, 1, "left"},
                new Object[]{1, -1, "left"},

                new Object[]{FIELD_WIDTH + 1, 0, "right"},
                new Object[]{FIELD_WIDTH - 1, -1, "right"},

                new Object[]{1, -1, "upper"},
                new Object[]{-1, 1, "upper"},

                new Object[]{1, FIELD_HEIGHT + 1, "bottom"},
                new Object[]{-1, FIELD_HEIGHT - 1, "bottom"}
        );
    }

    private static List<Object[]> provideTestCasesForEquality() {
        return List.of(
                new Object[]{2, 4, 2, 4, true},
                new Object[]{0, 0, 0, 0, true},
                new Object[]{-1.2, -5.3, -1.2, -5.3, true},
                new Object[]{-1.2, 5.3, -1.2, 5.3, true},
                new Object[]{2, 4, 1.99, 4, true},
                new Object[]{2, 3.99, 2, 4, true},
                new Object[]{-1.2, -2.3, -3.2, -4.3, false},
                new Object[]{6.53, 3.21, 20.30, 30.31, false},
                new Object[]{1, 1, 1, 0.01D, true},
                new Object[]{1, 1, 0, 1, false}
        );
    }

    private static List<Object[]> provideTestCasesForPenaltyAreaLeftSite() {
        return List.of(
                // LEFT EDGE
                new Object[]{0, FIELD_HEIGHT / 2, true},
                new Object[]{-1, FIELD_HEIGHT / 2, false},

                // RIGHT EDGE
                new Object[]{PENALTY_AREA_WIDTH, FIELD_HEIGHT / 2, true},
                new Object[]{PENALTY_AREA_WIDTH + 1, FIELD_HEIGHT / 2, false},

                // UPPER EDGE
                new Object[]{0, PENALTY_AREA_UPPER_LINE_HEIGHT, true},
                new Object[]{0, PENALTY_AREA_UPPER_LINE_HEIGHT - 1, false},

                // BOTTOM EDGE
                new Object[]{0, PENALTY_AREA_BOTTOM_LINE_HEIGHT, true},
                new Object[]{0, PENALTY_AREA_BOTTOM_LINE_HEIGHT + 1, false}
        );
    }


    private static Stream<Arguments> convertTestCasesToCalculatePositionsParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(testCase[0], testCase[1]),
                            new Position(testCase[2], testCase[3]),
                            new Position(testCase[4], testCase[5])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateVectorParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(testCase[0], testCase[1]),
                            new Vector2d(testCase[2], testCase[3]),
                            new Position(testCase[4], testCase[5])
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateDistanceParams(List<double[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (double[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(testCase[0], testCase[1]),
                            new Position(testCase[2], testCase[3]),
                            testCase[4]
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateFieldQuarterParams(List<Object[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (Object[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(Double.parseDouble(testCase[0].toString()), Double.parseDouble(testCase[1].toString())),
                            testCase[2]
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculateEquality(List<Object[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (Object[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(Double.parseDouble(testCase[0].toString()), Double.parseDouble(testCase[1].toString())),
                            new Position(Double.parseDouble(testCase[2].toString()), Double.parseDouble(testCase[3].toString())),
                            testCase[4]
                    ));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> convertTestCasesToCalculatePenaltyArea(List<Object[]> testCases) {
        List<Arguments> arguments = new ArrayList<>();
        for (Object[] testCase : testCases) {
            arguments.add(
                    Arguments.of(
                            new Position(Double.parseDouble(testCase[0].toString()), Double.parseDouble(testCase[1].toString())),
                            testCase[2]
                    ));
        }
        return arguments.stream();
    }


    private static Stream<Arguments> providePositionsForAdd() {
        return convertTestCasesToCalculatePositionsParams(provideTestCasesForAdd());
    }

    private static Stream<Arguments> provideVectorsForAdd() {
        return convertTestCasesToCalculateVectorParams(provideTestCasesForAdd());
    }

    private static Stream<Arguments> provideVectorsForSubtract() {
        return convertTestCasesToCalculateVectorParams(provideTestCasesForSubtract());
    }

    private static Stream<Arguments> providePositionsForDistance() {
        return convertTestCasesToCalculateDistanceParams(provideTestCasesForDistance());
    }

    private static Stream<Arguments> providePositionsForOutsidePlayingField() {
        return convertTestCasesToCalculateFieldQuarterParams(provideTestCasesForOutsidePlayingField());
    }

    private static Stream<Arguments> providePositionsForFieldQuarter() {
        return convertTestCasesToCalculateFieldQuarterParams(provideTestCasesForFieldQuarter());
    }

    private static Stream<Arguments> providePositionsForFieldQuarterOutsidePlayingField() {
        return convertTestCasesToCalculateFieldQuarterParams(provideTestCasesForFieldQuarterOutsidePlayingField());
    }

    private static Stream<Arguments> providePositionsForEquality() {
        return convertTestCasesToCalculateEquality(provideTestCasesForEquality());
    }

    private static Stream<Arguments> providePositionsForPenaltyAreaLeft() {
        return convertTestCasesToCalculatePenaltyArea(provideTestCasesForPenaltyAreaLeftSite());
    }

    @ParameterizedTest
    @MethodSource("providePositionsForAdd")
    void shouldAddTwoPositions(Position position1, Position position2, Position expectedPosition) {
        Position actual = Position.add(position1, position2);
        assertEquals(expectedPosition, actual);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForAdd")
    void shouldAddPositionToAnotherPosition(Position position1, Position position2, Position expectedPosition) {
        position1.addPosition(position2);
        assertEquals(expectedPosition, position1);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForAdd")
    void shouldAddVector(Position position, Vector2d vector2d, Position expected) {
        position.addVector(vector2d);
        assertEquals(expected, position);
    }

    @ParameterizedTest
    @MethodSource("provideVectorsForSubtract")
    void shouldSubtractVector(Position position, Vector2d vector2d, Position expected) {
        position.subtractVector(vector2d);
        assertEquals(expected, position);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForDistance")
    void shouldCalculateDistance(Position position1, Position position2, double expectedDistance) {
        double actualDistance = position1.getDistance(position2);
        assertEquals(expectedDistance, actualDistance, 0.01);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForOutsidePlayingField")
    void shouldCheckIfIsOutsidePlayingField(Position position, boolean expected) {
        boolean actual = position.isOutsidePlayingField();
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForFieldQuarter")
    void shouldPointOnCorrectFieldSite(Position position, String expectedFieldQuarter) {
        switch (expectedFieldQuarter) {
            case "left" -> {
                assertTrue(position.isOnLeftSite());
                return;
            }
            case "right" -> {
                assertTrue(position.isOnRightSite());
                return;
            }
            case "upper" -> {
                assertTrue(position.isOnUpperHalf());
                return;
            }
            case "bottom" -> {
                assertTrue(position.isOnBottomHalf());
                return;
            }
        }
        throw new IllegalArgumentException(expectedFieldQuarter + " in bad format. Should be left, right, upper or bottom");
    }

    @ParameterizedTest
    @MethodSource("providePositionsForFieldQuarterOutsidePlayingField")
    void shouldNotPointOnFieldSiteIfIsOutsidePlayingField(Position position, String expectedFieldQuarter) {
        switch (expectedFieldQuarter) {
            case "left" -> {
                assertFalse(position.isOnLeftSite());
                return;
            }
            case "right" -> {
                assertFalse(position.isOnRightSite());
                return;
            }
            case "upper" -> {
                assertFalse(position.isOnUpperHalf());
                return;
            }
            case "bottom" -> {
                assertFalse(position.isOnBottomHalf());
                return;
            }
        }
        throw new IllegalArgumentException(expectedFieldQuarter + " in bad format. Should be left, right, upper or bottom");
    }

    @Test
    void shouldNotPointOnFieldSiteIfIsExactlyOnTheMiddleLine() {
        // Check Width
        Position positionInTheMiddleOfWidth = new Position(FIELD_WIDTH / 2, 0);
        assertFalse(positionInTheMiddleOfWidth.isOnLeftSite());
        assertFalse(positionInTheMiddleOfWidth.isOnRightSite());

        // Check Height
        Position positionInTheMiddleOfHeight = new Position(0, FIELD_HEIGHT / 2);
        assertFalse(positionInTheMiddleOfHeight.isOnUpperHalf());
        assertFalse(positionInTheMiddleOfHeight.isOnBottomHalf());
    }

    @ParameterizedTest
    @MethodSource("providePositionsForEquality")
    void shouldBeEqualIfDistanceIsSmallerThan1(Position position1, Position position2, boolean expected) {
        boolean actual = position1.equals(position2);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForPenaltyAreaLeft")
    void shouldRecognizeLeftPenaltyArea(Position position, boolean expectedIsInsideLeftPenaltyArea) {
        boolean actual = position.isInLeftPenaltyArea();
        assertEquals(expectedIsInsideLeftPenaltyArea, actual);
    }

    @ParameterizedTest
    @MethodSource("providePositionsForPenaltyAreaLeft")
    void shouldRecognizeRightPenaltyArea(Position leftPosition, boolean expectedIsInsideLeftPenaltyArea) {
        Position position = PlayingFieldUtils.moveXToOtherHalf(leftPosition);
        boolean actual = position.isInRightPenaltyArea();
        assertEquals(expectedIsInsideLeftPenaltyArea, actual);
    }


}