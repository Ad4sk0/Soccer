package soccer.app.entities.formation.utils;

import org.junit.jupiter.api.Test;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.formation.FormationPositions;
import soccer.app.entities.formation.Formations;
import soccer.models.playingfield.PlayingField;
import soccer.models.positions.DefaultPositionsUtils;
import soccer.models.positions.PlayingPosition;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class PositionsInitializerTest {

    List<FormationPosition> formationPositionList;

    @Test
    void shouldInitializeEachPosition() {
        initialize(Formations.FOUR_FOUR_TWO);
        for (FormationPosition formationPosition : formationPositionList) {
            assertNotNull(formationPosition.getBasePosition());
            assertNotNull(formationPosition.getResumeByFriendlyGkPosition());
            assertNotNull(formationPosition.getResumeByOppositeGkPosition());
            assertNotNull(formationPosition.getCornerOnFriendlySitePosition());
            assertNotNull(formationPosition.getCornerOnOppositeSitePosition());
        }
    }

    @Test
    void shouldInitializeDefaultXForEachBasePositions() {
        initialize(Formations.FOUR_FOUR_TWO);
        for (FormationPosition formationPosition : formationPositionList) {
            assertEquals(formationPosition.getPosition().getDefaultBasePosition().getX(), formationPosition.getBasePosition().getX());
        }
    }

    @Test
    void shouldInitializeDefaultYForSinglePositions() {
        initialize(Formations.FOUR_FOUR_TWO);
        List<PlayingPosition> positionsToCheck = List.of(
                PlayingPosition.GK,
                PlayingPosition.LB,
                PlayingPosition.RB,
                PlayingPosition.LM,
                PlayingPosition.RM
        );
        for (FormationPosition formationPosition : formationPositionList) {
            if (positionsToCheck.contains(formationPosition.getPosition())) {
                assertEquals(formationPosition.getPosition().getDefaultBasePosition().getY(), formationPosition.getBasePosition().getY());
            }
        }
    }

    @Test
    void shouldAdjustYForDuplicatedPositions() {
        initialize(Formations.FOUR_FOUR_TWO);
        List<PlayingPosition> positionsToCheck = List.of(
                PlayingPosition.CB,
                PlayingPosition.CM,
                PlayingPosition.CF
        );
        for (FormationPosition formationPosition : formationPositionList) {
            if (positionsToCheck.contains(formationPosition.getPosition())) {
                double defaultY = formationPosition.getPosition().getDefaultBasePosition().getY();
                double minY = defaultY - DefaultPositionsUtils.VERTICAL_SPACE_FOR_EACH_PLAYER / 2;
                double maxY = defaultY + DefaultPositionsUtils.VERTICAL_SPACE_FOR_EACH_PLAYER / 2;
                assertNotEquals(defaultY, formationPosition.getBasePosition().getY());
                assertTrue(minY < formationPosition.getBasePosition().getY());
                assertTrue(maxY > formationPosition.getBasePosition().getY());
            }
        }
    }

    @Test
    void shouldCorrectlyAdjustYIfTwoPositionsAreTheSame() {
        assumeTrue(PlayingField.FIELD_HEIGHT == 680);
        initialize(Formations.FOUR_FOUR_TWO);
        int counter = 0;
        for (FormationPosition formationPosition : formationPositionList) {
            if (formationPosition.getPosition() == PlayingPosition.CB) {
                if (counter == 0) {
                    assertEquals(306, formationPosition.getBasePosition().getY());
                } else {
                    assertEquals(374, formationPosition.getBasePosition().getY());
                }
                counter++;
            }
        }
    }

    @Test
    void shouldCorrectlyAdjustYIfThreePositionsAreTheSame() {
        assumeTrue(PlayingField.FIELD_HEIGHT == 680);
        initialize(Formations.THREE_FOUR_THREE);
        int counter = 0;
        for (FormationPosition formationPosition : formationPositionList) {
            if (formationPosition.getPosition() == PlayingPosition.CB) {
                if (counter == 0) {
                    assertEquals(295, formationPosition.getBasePosition().getY(), 0.5);
                } else if (counter == 1) {
                    assertEquals(340, formationPosition.getBasePosition().getY(), 0.5);
                } else {
                    assertEquals(385, formationPosition.getBasePosition().getY(), 0.5);
                }
                counter++;
            }
        }
    }

    void initialize(Formations formation) {
        formationPositionList = setupFormationPositionList(formation);
        PositionsInitializer.initializePositionsBasedOnFormation(formationPositionList);
    }

    List<FormationPosition> setupFormationPositionList(Formations formation) {
        List<FormationPosition> formationPositionList = new ArrayList<>();
        for (PlayingPosition playingPosition : FormationPositions.FORMATION_POSITIONS.get(formation)) {
            FormationPosition formationPosition = new FormationPosition();
            formationPosition.setPosition(playingPosition);
            formationPositionList.add(formationPosition);
        }
        return formationPositionList;
    }
}