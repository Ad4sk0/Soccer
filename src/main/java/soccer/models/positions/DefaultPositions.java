package soccer.models.positions;

import soccer.models.playingfield.PlayingField;
import soccer.utils.Position;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Map.entry;
import static soccer.models.positions.DefaultPositionsUtils.*;
import static soccer.models.positions.InGamePosition.*;
import static soccer.models.positions.PlayingPosition.*;

public class DefaultPositions {

    private static final Map<PlayingPosition, Map<InGamePosition, Position>> defaultPositionsMap = new EnumMap<>(Map.ofEntries(

            // GoalKeeper
            entry(GK, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(GK_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(PlayingField.SMALL_PENALTY_AREA_WIDTH, PlayingField.FIELD_HEIGHT_HALF + 50),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(GK_BASE_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(GK_BASE_X, CENTER_BASE_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(GK_BASE_X + 50, CENTER_BASE_Y)
            ))),

            // Defenders
            entry(LB, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DF_BASE_X, LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DF_RESUME_BY_FRIENDLY_GK_X, LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DF_RESUME_BY_OPPOSITE_GK_X, LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DF_CORNER_ON_FRIENDLY_SITE_X, LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DF_CORNER_ON_OPPOSITE_SITE_X, LEFT_CORNER_Y)
            ))),
            entry(LCB, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DF_BASE_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DF_RESUME_BY_FRIENDLY_GK_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DF_RESUME_BY_OPPOSITE_GK_X, CENTER_LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DF_CORNER_ON_FRIENDLY_SITE_X, CENTER_LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DF_CORNER_ON_OPPOSITE_SITE_X, CENTER_LEFT_CORNER_Y)
            ))),
            entry(CB, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DF_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DF_RESUME_BY_FRIENDLY_GK_X, CENTER_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DF_RESUME_BY_OPPOSITE_GK_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DF_CORNER_ON_FRIENDLY_SITE_X, CENTER_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DF_CORNER_ON_OPPOSITE_SITE_X, CENTER_CORNER_Y)
            ))),
            entry(RCB, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DF_BASE_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DF_RESUME_BY_FRIENDLY_GK_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DF_RESUME_BY_OPPOSITE_GK_X, CENTER_RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DF_CORNER_ON_FRIENDLY_SITE_X, CENTER_RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DF_CORNER_ON_OPPOSITE_SITE_X, CENTER_RIGHT_CORNER_Y)
            ))),
            entry(RB, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DF_BASE_X, RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DF_RESUME_BY_FRIENDLY_GK_X, RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DF_RESUME_BY_OPPOSITE_GK_X, RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DF_CORNER_ON_FRIENDLY_SITE_X, RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DF_CORNER_ON_OPPOSITE_SITE_X, RIGHT_CORNER_Y)
            ))),

            // Defensive Midfielders
            entry(DM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DM_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DM_RESUME_BY_FRIENDLY_GK_X, CENTER_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DM_RESUME_BY_OPPOSITE_GK_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DM_CORNER_ON_FRIENDLY_SITE_X, CENTER_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DM_CORNER_ON_OPPOSITE_SITE_X, CENTER_CORNER_Y)
            ))),
            entry(LDM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DM_BASE_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DM_RESUME_BY_FRIENDLY_GK_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DM_RESUME_BY_OPPOSITE_GK_X, CENTER_LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DM_CORNER_ON_FRIENDLY_SITE_X, CENTER_LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DM_CORNER_ON_OPPOSITE_SITE_X, CENTER_LEFT_CORNER_Y)
            ))),
            entry(RDM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(DM_BASE_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(DM_RESUME_BY_FRIENDLY_GK_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(DM_RESUME_BY_OPPOSITE_GK_X, CENTER_RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(DM_CORNER_ON_FRIENDLY_SITE_X, CENTER_RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(DM_CORNER_ON_OPPOSITE_SITE_X, CENTER_RIGHT_CORNER_Y)
            ))),

            // Midfielders
            entry(LM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(MF_BASE_X, LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(MF_RESUME_BY_FRIENDLY_GK_X, LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(MF_RESUME_BY_OPPOSITE_GK_X, LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(MF_CORNER_ON_FRIENDLY_SITE_X, LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(MF_CORNER_ON_OPPOSITE_SITE_X, LEFT_CORNER_Y)
            ))),
            entry(LCM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(MF_BASE_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(MF_RESUME_BY_FRIENDLY_GK_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(MF_RESUME_BY_OPPOSITE_GK_X, CENTER_LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(MF_CORNER_ON_FRIENDLY_SITE_X, CENTER_LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(MF_CORNER_ON_OPPOSITE_SITE_X, CENTER_LEFT_CORNER_Y)
            ))),
            entry(CM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(MF_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(MF_RESUME_BY_FRIENDLY_GK_X, CENTER_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(MF_RESUME_BY_OPPOSITE_GK_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(MF_CORNER_ON_FRIENDLY_SITE_X, CENTER_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(MF_CORNER_ON_OPPOSITE_SITE_X, CENTER_CORNER_Y)
            ))),
            entry(RCM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(MF_BASE_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(MF_RESUME_BY_FRIENDLY_GK_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(MF_RESUME_BY_OPPOSITE_GK_X, CENTER_RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(MF_CORNER_ON_FRIENDLY_SITE_X, CENTER_RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(MF_CORNER_ON_OPPOSITE_SITE_X, CENTER_RIGHT_CORNER_Y)
            ))),
            entry(RM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(MF_BASE_X, RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(MF_RESUME_BY_FRIENDLY_GK_X, RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(MF_RESUME_BY_OPPOSITE_GK_X, RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(MF_CORNER_ON_FRIENDLY_SITE_X, RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(MF_CORNER_ON_OPPOSITE_SITE_X, RIGHT_CORNER_Y)
            ))),

            // Attacking Midfielders
            entry(LAM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(AM_BASE_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(AM_RESUME_BY_FRIENDLY_GK_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(AM_RESUME_BY_OPPOSITE_GK_X, CENTER_LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(AM_CORNER_ON_FRIENDLY_SITE_X, CENTER_LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(AM_CORNER_ON_OPPOSITE_SITE_X, CENTER_LEFT_CORNER_Y)
            ))),
            entry(CAM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(AM_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(AM_RESUME_BY_FRIENDLY_GK_X, CENTER_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(AM_RESUME_BY_OPPOSITE_GK_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(AM_CORNER_ON_FRIENDLY_SITE_X, CENTER_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(AM_CORNER_ON_OPPOSITE_SITE_X, CENTER_CORNER_Y)
            ))),
            entry(RAM, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(AM_BASE_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(AM_RESUME_BY_FRIENDLY_GK_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(AM_RESUME_BY_OPPOSITE_GK_X, CENTER_RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(AM_CORNER_ON_FRIENDLY_SITE_X, CENTER_RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(AM_CORNER_ON_OPPOSITE_SITE_X, CENTER_RIGHT_CORNER_Y)
            ))),

            // Forwards
            entry(LWF, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(FW_BASE_X, LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(FW_RESUME_BY_FRIENDLY_GK_X, LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(FW_RESUME_BY_OPPOSITE_GK_X, LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(FW_CORNER_ON_FRIENDLY_SITE_X, LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(FW_CORNER_ON_OPPOSITE_SITE_X, LEFT_CORNER_Y)
            ))),
            entry(LCF, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(FW_BASE_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(FW_RESUME_BY_FRIENDLY_GK_X, CENTER_LEFT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(FW_RESUME_BY_OPPOSITE_GK_X, CENTER_LEFT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(FW_CORNER_ON_FRIENDLY_SITE_X, CENTER_LEFT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(FW_CORNER_ON_OPPOSITE_SITE_X, CENTER_LEFT_CORNER_Y)
            ))),
            entry(CF, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(FW_BASE_X, CENTER_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(FW_RESUME_BY_FRIENDLY_GK_X, CENTER_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(FW_RESUME_BY_OPPOSITE_GK_X, CENTER_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(FW_CORNER_ON_FRIENDLY_SITE_X, CENTER_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(FW_CORNER_ON_OPPOSITE_SITE_X, CENTER_CORNER_Y)
            ))),
            entry(RCF, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(FW_BASE_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(FW_RESUME_BY_FRIENDLY_GK_X, CENTER_RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(FW_RESUME_BY_OPPOSITE_GK_X, CENTER_RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(FW_CORNER_ON_FRIENDLY_SITE_X, CENTER_RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(FW_CORNER_ON_OPPOSITE_SITE_X, CENTER_RIGHT_CORNER_Y)
            ))),
            entry(RWF, new EnumMap<>(Map.of(
                    BASE_POSITION, new Position(FW_BASE_X, RIGHT_BASE_Y),
                    RESUME_BY_FRIENDLY_GK_POSITION, new Position(FW_RESUME_BY_FRIENDLY_GK_X, RIGHT_BASE_Y),
                    RESUME_BY_OPPOSITE_GK_POSITION, new Position(FW_RESUME_BY_OPPOSITE_GK_X, RIGHT_BASE_Y),
                    CORNER_ON_FRIENDLY_SITE_POSITION, new Position(FW_CORNER_ON_FRIENDLY_SITE_X, RIGHT_CORNER_Y),
                    CORNER_ON_OPPOSITE_SITE_POSITION, new Position(FW_CORNER_ON_OPPOSITE_SITE_X, RIGHT_CORNER_Y)
            )))
    ));

    private DefaultPositions() {
    }

    public static Position get(PlayingPosition playingPosition, InGamePosition inGamePosition) {
        return defaultPositionsMap.get(playingPosition).get(inGamePosition);
    }

    public static Map<PlayingPosition, Map<InGamePosition, Position>> getDefaultPositionsMap() {
        return defaultPositionsMap;
    }
}
