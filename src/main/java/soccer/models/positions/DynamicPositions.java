package soccer.models.positions;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Map.entry;
import static soccer.models.positions.DefaultPositionsUtils.*;
import static soccer.models.positions.DynamicPositionsKey.*;
import static soccer.models.positions.PlayingPosition.*;

public class DynamicPositions {

    private static final Map<PlayingPosition, Map<DynamicPositionsKey, Double>> dynamicPositionsMap = new EnumMap<>(Map.ofEntries(

            // GoalKeeper
            entry(GK, new EnumMap<>(Map.of(
                    MIN_X, GK_DYNAMIC_MIN_X,
                    MAX_X, GK_DYNAMIC_MIN_X,
                    MIN_Y, GK_DYNAMIC_MIN_Y,
                    MAX_Y, GK_DYNAMIC_MAX_Y
            ))),

            // Defenders
            entry(LB, new EnumMap<>(Map.of(
                    MIN_X, DF_DYNAMIC_MIN_X,
                    MAX_X, DF_DYNAMIC_MAX_X,
                    MIN_Y, LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, LEFT_DYNAMIC_MAX_Y
            ))),
            entry(LCB, new EnumMap<>(Map.of(
                    MIN_X, DF_DYNAMIC_MIN_X,
                    MAX_X, DF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_LEFT_DYNAMIC_MAX_Y
            ))),
            entry(CB, new EnumMap<>(Map.of(
                    MIN_X, DF_DYNAMIC_MIN_X,
                    MAX_X, DF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_DYNAMIC_MAX_Y
            ))),
            entry(RCB, new EnumMap<>(Map.of(
                    MIN_X, DF_DYNAMIC_MIN_X,
                    MAX_X, DF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_RIGHT_DYNAMIC_MAX_Y
            ))),
            entry(RB, new EnumMap<>(Map.of(
                    MIN_X, DF_DYNAMIC_MIN_X,
                    MAX_X, DF_DYNAMIC_MAX_X,
                    MIN_Y, RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, RIGHT_DYNAMIC_MAX_Y
            ))),

            // Defensive Midfielders
            entry(DM, new EnumMap<>(Map.of(
                    MIN_X, DM_DYNAMIC_MIN_X,
                    MAX_X, DM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_DYNAMIC_MAX_Y
            ))),
            entry(LDM, new EnumMap<>(Map.of(
                    MIN_X, DM_DYNAMIC_MIN_X,
                    MAX_X, DM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_LEFT_DYNAMIC_MAX_Y
            ))),
            entry(RDM, new EnumMap<>(Map.of(
                    MIN_X, DM_DYNAMIC_MIN_X,
                    MAX_X, DM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_RIGHT_DYNAMIC_MAX_Y
            ))),

            // Midfielders
            entry(LM, new EnumMap<>(Map.of(
                    MIN_X, MF_DYNAMIC_MIN_X,
                    MAX_X, MF_DYNAMIC_MAX_X,
                    MIN_Y, LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, LEFT_DYNAMIC_MAX_Y
            ))),
            entry(LCM, new EnumMap<>(Map.of(
                    MIN_X, MF_DYNAMIC_MIN_X,
                    MAX_X, MF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_LEFT_DYNAMIC_MAX_Y
            ))),
            entry(CM, new EnumMap<>(Map.of(
                    MIN_X, MF_DYNAMIC_MIN_X,
                    MAX_X, MF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_DYNAMIC_MAX_Y
            ))),
            entry(RCM, new EnumMap<>(Map.of(
                    MIN_X, MF_DYNAMIC_MIN_X,
                    MAX_X, MF_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_RIGHT_DYNAMIC_MAX_Y
            ))),
            entry(RM, new EnumMap<>(Map.of(
                    MIN_X, MF_DYNAMIC_MIN_X,
                    MAX_X, MF_DYNAMIC_MAX_X,
                    MIN_Y, RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, RIGHT_DYNAMIC_MAX_Y
            ))),

            // Attacking Midfielders
            entry(LAM, new EnumMap<>(Map.of(
                    MIN_X, AM_DYNAMIC_MIN_X,
                    MAX_X, AM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_LEFT_DYNAMIC_MAX_Y
            ))),
            entry(CAM, new EnumMap<>(Map.of(
                    MIN_X, AM_DYNAMIC_MIN_X,
                    MAX_X, AM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_DYNAMIC_MAX_Y
            ))),
            entry(RAM, new EnumMap<>(Map.of(
                    MIN_X, AM_DYNAMIC_MIN_X,
                    MAX_X, AM_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_RIGHT_DYNAMIC_MAX_Y
            ))),

            // Forwards
            entry(LWF, new EnumMap<>(Map.of(
                    MIN_X, FW_DYNAMIC_MIN_X,
                    MAX_X, FW_DYNAMIC_MAX_X,
                    MIN_Y, LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, LEFT_DYNAMIC_MAX_Y
            ))),
            entry(LCF, new EnumMap<>(Map.of(
                    MIN_X, FW_DYNAMIC_MIN_X,
                    MAX_X, FW_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_LEFT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_LEFT_DYNAMIC_MAX_Y
            ))),
            entry(CF, new EnumMap<>(Map.of(
                    MIN_X, FW_DYNAMIC_MIN_X,
                    MAX_X, FW_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_DYNAMIC_MAX_Y
            ))),
            entry(RCF, new EnumMap<>(Map.of(
                    MIN_X, FW_DYNAMIC_MIN_X,
                    MAX_X, FW_DYNAMIC_MAX_X,
                    MIN_Y, CENTER_RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, CENTER_RIGHT_DYNAMIC_MAX_Y
            ))),
            entry(RWF, new EnumMap<>(Map.of(
                    MIN_X, FW_DYNAMIC_MIN_X,
                    MAX_X, FW_DYNAMIC_MAX_X,
                    MIN_Y, RIGHT_DYNAMIC_MIN_Y,
                    MAX_Y, RIGHT_DYNAMIC_MAX_Y
            )))
    ));

    private DynamicPositions() {
    }

    public static Double get(PlayingPosition playingPosition, DynamicPositionsKey dynamicPositionsKey) {
        return dynamicPositionsMap.get(playingPosition).get(dynamicPositionsKey);
    }

    public static Map<PlayingPosition, Map<DynamicPositionsKey, Double>> getDynamicPositionsMap() {
        return dynamicPositionsMap;
    }
}
