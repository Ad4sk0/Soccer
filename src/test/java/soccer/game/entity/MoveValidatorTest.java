package soccer.game.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import soccer.game.entity.ball.Ball;
import soccer.game.match.GameMatch;
import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.wildfly.common.Assert.assertTrue;

class MoveValidatorTest {

    Ball ball;

    @BeforeEach
    void initMovingEntity() {
        MockitoAnnotations.openMocks(this);
        GameMatch match = mock(GameMatch.class);
        ball = new Ball(match);
    }


    @Test
    void shouldCheckExceedingVerticalLine() {
        testPointsAlongTheVerticalLines();
    }

    @Test
    void shouldCheckExceedingHorizontalLine() {
        testPointsAlongTheHorizontalLines();
    }

    @Test
    void shouldCheckExceedingGoalLine() {

        double startY = PlayingField.UPPER_POST_HEIGHT;
        double endY = PlayingField.BOTTOM_POST_HEIGHT;

        double leftX = 0;
        double rightX = PlayingField.FIELD_WIDTH;

        // Test whole vertical line
        for (int y = 0; y < PlayingField.FIELD_HEIGHT + 1; y++) {
            ball.getPosition().setY(y);

            // On the left line
            ball.getPosition().setX(leftX);
            assertFalse(MoveValidator.isGoalLineExceeded(ball));

            // Behind the left line
            ball.getPosition().setX(leftX - 1);

            // Test values behind goal posts for true, rest for false
            if (y >= startY && y <= endY) {
                assertTrue(MoveValidator.isGoalLineExceeded(ball));
            } else {
                assertFalse(MoveValidator.isGoalLineExceeded(ball));
            }

            // On the right line
            ball.getPosition().setX(rightX);
            assertFalse(MoveValidator.isGoalLineExceeded(ball));

            // Behind the right line
            ball.getPosition().setX(rightX + 1);

            if (y >= startY && y <= endY) {
                assertTrue(MoveValidator.isGoalLineExceeded(ball));
            } else {
                assertFalse(MoveValidator.isGoalLineExceeded(ball));
            }
        }
    }

    @Test
    void shouldCheckExceedingCornerLine() {
        double startY = PlayingField.UPPER_POST_HEIGHT;
        double endY = PlayingField.BOTTOM_POST_HEIGHT;

        double leftX = 0;
        double rightX = PlayingField.FIELD_WIDTH;

        // Test whole vertical line
        for (int y = 0; y < PlayingField.FIELD_HEIGHT + 1; y++) {
            ball.getPosition().setY(y);

            // On the left line
            ball.getPosition().setX(leftX);
            assertFalse(MoveValidator.isCornerLineExceeded(ball));

            // Behind the left line
            ball.getPosition().setX(leftX - 1);

            // Test values behind goal posts for true, rest for false
            if (y > startY && y < endY) {
                assertFalse(MoveValidator.isCornerLineExceeded(ball));
            } else {
                assertTrue(MoveValidator.isCornerLineExceeded(ball));
            }

            // On the right line
            ball.getPosition().setX(rightX);
            assertFalse(MoveValidator.isCornerLineExceeded(ball));

            // Behind the right line
            ball.getPosition().setX(rightX + 1);

            if (y > startY && y < endY) {
                assertFalse(MoveValidator.isCornerLineExceeded(ball));
            } else {
                assertTrue(MoveValidator.isCornerLineExceeded(ball));
            }
        }
    }

    @Test
    void shouldCheckHittingGoalPosts() {
        double rightX = PlayingField.FIELD_WIDTH;

        List<Double> xPointsToTest = new ArrayList<>();
        for (double x = -1; x <= ball.getSize() / 2.0; x++) {
            xPointsToTest.add(x);
        }
        for (double x = rightX - ball.getSize() / 2.0 - 1; x <= rightX + 1; x++) {
            xPointsToTest.add(x);
        }

        // Test whole vertical line
        for (double y = 0; y < PlayingField.FIELD_HEIGHT + 1; y++) {
            for (double x : xPointsToTest) {

                ball.getPosition().setX(x);
                ball.getPosition().setY(y);


                boolean hit = false;
                for (Position post : PlayingField.getGoalPosts()) {
                    double dist = Point2D.distance(x, y, post.getX(), post.getY());
                    if (dist < ball.getSize() / 2.0) {
                        hit = true;
                    }
                }

                if (hit) {
                    assertTrue(MoveValidator.isGoalPostHit(ball));
                } else {
                    assertFalse(MoveValidator.isGoalPostHit(ball));
                }
            }
        }
    }

    private void testPointsAlongTheVerticalLines() {
        testPointsAlongTheVerticalLine(false);
        testPointsAlongTheVerticalLine(true);
    }

    private void testPointsAlongTheHorizontalLines() {
        testPointsAlongTheHorizontalLine(false);
        testPointsAlongTheHorizontalLine(true);
    }

    private void testPointsAlongTheVerticalLine(boolean rightSite) {
        double x = 0;
        double behindTheLineCorrection = -1;
        if (rightSite) {
            x = PlayingField.FIELD_WIDTH;
            behindTheLineCorrection = 1;
        }

        for (double y = 0; y < PlayingField.FIELD_HEIGHT + 1; y++) {
            ball.getPosition().setY(y);

            // On the line
            ball.getPosition().setX(x);
            assertFalse(MoveValidator.isVerticalSideLineExceeded(ball));

            // Behind the line
            ball.getPosition().setX(x + behindTheLineCorrection);
            assertTrue(MoveValidator.isVerticalSideLineExceeded(ball));
        }
    }

    private void testPointsAlongTheHorizontalLine(boolean rightSite) {

        double y = 0;
        int behindTheLineCorrection = -1;
        if (rightSite) {
            y = PlayingField.FIELD_HEIGHT;
            behindTheLineCorrection = 1;
        }

        for (double x = 0; x < PlayingField.FIELD_WIDTH + 1; x++) {
            ball.getPosition().setX(x);

            // On the line
            ball.getPosition().setY(y);
            assertFalse(MoveValidator.isHorizontalSideLineExceeded(ball));

            // Behind the line
            ball.getPosition().setY(y + behindTheLineCorrection);
            assertTrue(MoveValidator.isHorizontalSideLineExceeded(ball));
        }
    }

}