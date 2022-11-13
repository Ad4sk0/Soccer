package soccer.app.entities.formation;


import soccer.models.positions.PlayingPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static soccer.models.positions.PlayingPosition.*;

public class FormationPositions {

    public static final Map<Formations, List<PlayingPosition>> FORMATION_POSITIONS = Map.ofEntries(

            // 4-4-2
            entry(Formations.FOUR_FOUR_TWO, new ArrayList<>(List.of(
                    GK,

                    LB,
                    CB,
                    CB,
                    RB,

                    LM,
                    CM,
                    CM,
                    RM,

                    CF,
                    CF
            ))),

            // 4-3-3
            entry(Formations.FOUR_THREE_THREE, new ArrayList<>(List.of(
                    GK,

                    LB,
                    CB,
                    CB,
                    RB,

                    LM,
                    CM,
                    RM,

                    LCF,
                    CF,
                    RCF
            ))),

            // 3-4-3
            entry(Formations.THREE_FOUR_THREE, new ArrayList<>(List.of(
                    GK,

                    CB,
                    CB,
                    CB,

                    LM,
                    CM,
                    CM,
                    RM,

                    LCF,
                    CF,
                    RCF
            ))),

            // 4-5-1
            entry(Formations.FOUR_FIVE_ONE, new ArrayList<>(List.of(
                    GK,

                    LB,
                    CB,
                    CB,
                    RB,

                    LM,
                    CM,
                    CM,
                    RM,

                    CAM,

                    CF
            )))
    );

    private FormationPositions() {
    }


}
