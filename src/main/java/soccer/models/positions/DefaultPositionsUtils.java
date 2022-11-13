package soccer.models.positions;


import soccer.models.playingfield.PlayingField;

public class DefaultPositionsUtils {

    public static final int MAX_PLAYERS_IN_VERTICAL_LINE = 5;
    public static final double VERTICAL_SPACE_FOR_EACH_PLAYER = PlayingField.FIELD_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE;
    public static final double VERTICAL_SPACE_FOR_EACH_PLAYER_IN_PENALTY_AREA = PlayingField.PENALTY_AREA_WIDTH / MAX_PLAYERS_IN_VERTICAL_LINE;

    // BASE POSITION
    public static final double GK_BASE_X = 20;
    public static final double DF_BASE_X = PlayingField.PENALTY_AREA_WIDTH - 20;
    public static final double MF_BASE_X = PlayingField.PENALTY_AREA_WIDTH * 2 - 30;
    public static final double FW_BASE_X = PlayingField.FIELD_WIDTH_HALF - PlayingField.CENTRE_CIRCLE_RADIUS - 10;
    public static final double DM_BASE_X = (MF_BASE_X + DF_BASE_X) / 2;
    public static final double AM_BASE_X = (MF_BASE_X + FW_BASE_X) / 2;

    public static final double LEFT_BASE_Y = PlayingField.FIELD_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2;
    public static final double CENTER_LEFT_BASE_Y = PlayingField.FIELD_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 3;
    public static final double CENTER_BASE_Y = PlayingField.FIELD_HEIGHT_HALF;
    public static final double CENTER_RIGHT_BASE_Y = PlayingField.FIELD_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 7;
    public static final double RIGHT_BASE_Y = PlayingField.FIELD_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 9;

    // RESUME BY FRIENDLY GK
    public static final double DF_RESUME_BY_FRIENDLY_GK_X = PlayingField.MIDDLE_LINE_WIDTH - PlayingField.CENTRE_CIRCLE_RADIUS + 30;
    public static final double MF_RESUME_BY_FRIENDLY_GK_X = PlayingField.MIDDLE_LINE_WIDTH + 100;
    public static final double FW_RESUME_BY_FRIENDLY_GK_X = PlayingField.FIELD_WIDTH_HALF + PlayingField.CENTRE_CIRCLE_RADIUS * 2;
    public static final double DM_RESUME_BY_FRIENDLY_GK_X = (MF_RESUME_BY_FRIENDLY_GK_X + DF_RESUME_BY_FRIENDLY_GK_X) / 2;
    public static final double AM_RESUME_BY_FRIENDLY_GK_X = (MF_RESUME_BY_FRIENDLY_GK_X + FW_RESUME_BY_FRIENDLY_GK_X) / 2;

    // RESUME BY OPPOSITE GK
    public static final double DF_RESUME_BY_OPPOSITE_GK_X = PlayingField.PENALTY_AREA_WIDTH + 10;
    public static final double MF_RESUME_BY_OPPOSITE_GK_X = PlayingField.MIDDLE_LINE_WIDTH - 200;
    public static final double FW_RESUME_BY_OPPOSITE_GK_X = PlayingField.FIELD_WIDTH_HALF - PlayingField.CENTRE_CIRCLE_RADIUS - 50;
    public static final double DM_RESUME_BY_OPPOSITE_GK_X = (MF_RESUME_BY_OPPOSITE_GK_X + DF_RESUME_BY_OPPOSITE_GK_X) / 2;
    public static final double AM_RESUME_BY_OPPOSITE_GK_X = (MF_RESUME_BY_OPPOSITE_GK_X + FW_RESUME_BY_OPPOSITE_GK_X) / 2;

    // CORNER
    public static final double LEFT_CORNER_Y = PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + PlayingField.PENALTY_AREA_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2;
    public static final double CENTER_LEFT_CORNER_Y = PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + PlayingField.PENALTY_AREA_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 3;
    public static final double CENTER_CORNER_Y = PlayingField.FIELD_HEIGHT_HALF;
    public static final double CENTER_RIGHT_CORNER_Y = PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + PlayingField.PENALTY_AREA_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 7;
    public static final double RIGHT_CORNER_Y = PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT + PlayingField.PENALTY_AREA_HEIGHT / MAX_PLAYERS_IN_VERTICAL_LINE / 2 * 9;

    // CORNER ON FRIENDLY SITE
    public static final double DF_CORNER_ON_FRIENDLY_SITE_X = PlayingField.PENALTY_AREA_WIDTH / 2 - 30;
    public static final double MF_CORNER_ON_FRIENDLY_SITE_X = PlayingField.PENALTY_AREA_WIDTH / 2 + 30;
    public static final double FW_CORNER_ON_FRIENDLY_SITE_X = PlayingField.PENALTY_AREA_WIDTH + 10;
    public static final double DM_CORNER_ON_FRIENDLY_SITE_X = (MF_CORNER_ON_FRIENDLY_SITE_X + DF_CORNER_ON_FRIENDLY_SITE_X) / 2;
    public static final double AM_CORNER_ON_FRIENDLY_SITE_X = (MF_CORNER_ON_FRIENDLY_SITE_X + FW_CORNER_ON_FRIENDLY_SITE_X) / 2;

    // CORNER ON OPPOSITE SITE
    public static final double DF_CORNER_ON_OPPOSITE_SITE_X = PlayingField.FIELD_WIDTH / 2 + 30;
    public static final double MF_CORNER_ON_OPPOSITE_SITE_X = PlayingField.FIELD_WIDTH - PlayingField.PENALTY_AREA_WIDTH / 2 - 60;
    public static final double FW_CORNER_ON_OPPOSITE_SITE_X = PlayingField.FIELD_WIDTH - PlayingField.PENALTY_AREA_WIDTH / 2 + 10;
    public static final double DM_CORNER_ON_OPPOSITE_SITE_X = (MF_CORNER_ON_OPPOSITE_SITE_X + DF_CORNER_ON_OPPOSITE_SITE_X) / 2;
    public static final double AM_CORNER_ON_OPPOSITE_SITE_X = (MF_CORNER_ON_OPPOSITE_SITE_X + FW_CORNER_ON_OPPOSITE_SITE_X) / 2;


    private DefaultPositionsUtils() {
    }
}
