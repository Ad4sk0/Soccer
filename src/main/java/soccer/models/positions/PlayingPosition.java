package soccer.models.positions;

import soccer.utils.Position;

import static soccer.models.positions.PositionType.*;

public enum PlayingPosition implements Comparable<PlayingPosition> {

    GK(1, PositionType.GK),

    // Defenders
    LB(2, DF),
    LCB(3, DF),
    CB(4, DF),
    RCB(5, DF),
    RB(6, DF),

    // Defensive midfielder
    DM(7, MF),
    LDM(8, MF),
    RDM(9, MF),

    // Central Midfielder
    LM(10, MF),
    LCM(11, MF),
    CM(12, MF),
    RCM(13, MF),
    RM(14, MF),

    // Attacking Midfielders
    LAM(15, MF),
    CAM(16, MF),
    RAM(17, MF),

    // Forwards
    LWF(18, FW),
    LCF(19, FW),
    CF(20, FW),
    RCF(21, FW),
    RWF(22, FW);

    final int order;
    final PositionType type;
    Position defaultBasePosition;
    Position defaultResumeByFriendlyGkPosition;
    Position defaultResumeByOppositeGkPosition;
    Position defaultCornerOnFriendlySitePosition;
    Position defaultCornerOnOppositeSitePosition;

    PlayingPosition(int order, PositionType positionType) {
        this.order = order;
        this.type = positionType;
    }

    public int getOrder() {
        return order;
    }

    public PositionType getType() {
        return type;
    }

    public Position getDefaultBasePosition() {
        if (defaultBasePosition == null) {
            defaultBasePosition = DefaultPositions.get(this, InGamePosition.BASE_POSITION);
        }
        return defaultBasePosition;
    }

    public Position getDefaultResumeByFriendlyGkPosition() {
        if (defaultResumeByFriendlyGkPosition == null) {
            defaultResumeByFriendlyGkPosition = DefaultPositions.get(this, InGamePosition.RESUME_BY_FRIENDLY_GK_POSITION);
        }
        return defaultResumeByFriendlyGkPosition;
    }

    public Position getDefaultResumeByOppositeGkPosition() {
        if (defaultResumeByOppositeGkPosition == null) {
            defaultResumeByOppositeGkPosition = DefaultPositions.get(this, InGamePosition.RESUME_BY_OPPOSITE_GK_POSITION);
        }
        return defaultResumeByOppositeGkPosition;
    }

    public Position getDefaultCornerOnFriendlySitePosition() {
        if (defaultCornerOnFriendlySitePosition == null) {
            defaultCornerOnFriendlySitePosition = DefaultPositions.get(this, InGamePosition.CORNER_ON_FRIENDLY_SITE_POSITION);
        }
        return defaultCornerOnFriendlySitePosition;
    }

    public Position getDefaultCornerOnOppositeSitePosition() {
        if (defaultCornerOnOppositeSitePosition == null) {
            defaultCornerOnOppositeSitePosition = DefaultPositions.get(this, InGamePosition.CORNER_ON_OPPOSITE_SITE_POSITION);
        }
        return defaultCornerOnOppositeSitePosition;
    }

    public double getDynamicPositionMinX() {
        return DynamicPositions.get(this, DynamicPositionsKey.MIN_X);
    }

    public double getDynamicPositionMaxX() {
        return DynamicPositions.get(this, DynamicPositionsKey.MAX_X);
    }

    public double getDynamicPositionMinY() {
        return DynamicPositions.get(this, DynamicPositionsKey.MIN_Y);
    }

    public double getDynamicPositionMaxY() {
        return DynamicPositions.get(this, DynamicPositionsKey.MAX_Y);
    }
}
