package soccer.models.positions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultPositionsTest {

    @Test
    void defaultPositionEntriesShouldMatchPositionsNumber() {
        assertEquals(PlayingPosition.values().length, DefaultPositions.getDefaultPositionsMap().size());
    }

    @Test
    void eachDefaultPositionEntryShouldHaveEachInGamePosition() {
        for (var entry : DefaultPositions.getDefaultPositionsMap().entrySet()) {
            for (InGamePosition inGamePosition : InGamePosition.values()) {
                assertTrue(entry.getValue().containsKey(inGamePosition));
            }
        }
    }
}