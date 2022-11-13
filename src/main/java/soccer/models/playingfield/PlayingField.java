package soccer.models.playingfield;


import soccer.utils.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayingField {
    public static final double FIELD_WIDTH = 1050;
    public static final double FIELD_HEIGHT = 680;
    public static final double FIELD_WIDTH_HALF = FIELD_WIDTH / 2;
    public static final double FIELD_HEIGHT_HALF = FIELD_HEIGHT / 2;
    public static final double PENALTY_AREA_WIDTH = 165;
    public static final double PENALTY_AREA_HEIGHT = 400;
    public static final double PENALTY_AREA_UPPER_LINE_HEIGHT = FIELD_HEIGHT / 2 - PENALTY_AREA_HEIGHT / 2;
    public static final double PENALTY_AREA_BOTTOM_LINE_HEIGHT = FIELD_HEIGHT / 2 + PENALTY_AREA_HEIGHT / 2;
    public static final double UPPER_PENALTY_AREA_LINE_HEIGHT = (FIELD_HEIGHT - PENALTY_AREA_HEIGHT) / 2;
    public static final double SMALL_PENALTY_AREA_WIDTH = 55;
    public static final double SMALL_PENALTY_AREA_HEIGHT = 183;
    public static final double GOAL_HEIGHT = 73;
    public static final double PENALTY_SPOT_WIDTH = 110;
    public static final double CENTRE_CIRCLE_RADIUS = 91;
    public static final double MIDDLE_LINE_WIDTH = FIELD_WIDTH / 2;
    public static final double UPPER_POST_HEIGHT = FIELD_HEIGHT / 2 - GOAL_HEIGHT / 2;
    public static final Position RIGHT_UPPER_POST = new Position(FIELD_WIDTH, UPPER_POST_HEIGHT);
    public static final Position LEFT_UPPER_POST = new Position(0, UPPER_POST_HEIGHT);
    public static final double BOTTOM_POST_HEIGHT = FIELD_HEIGHT / 2 + GOAL_HEIGHT / 2;
    public static final Position RIGHT_BOTTOM_POST = new Position(FIELD_WIDTH, BOTTOM_POST_HEIGHT);
    public static final Position LEFT_BOTTOM_POST = new Position(0, BOTTOM_POST_HEIGHT);
    private static final List<Position> GOAL_POSTS = new ArrayList<>(Arrays.asList(RIGHT_UPPER_POST, RIGHT_BOTTOM_POST, LEFT_UPPER_POST, LEFT_BOTTOM_POST));
    public static final Position CENTRE_CIRCLE_POSITION = new Position(FIELD_WIDTH / 2, FIELD_HEIGHT / 2);
    public static final Position STARTING_FROM_MIDDLE_LEFT_TEAM_POS_1 = new Position(CENTRE_CIRCLE_POSITION.getX() - 5, CENTRE_CIRCLE_POSITION.getY() - 25);
    public static final Position STARTING_FROM_MIDDLE_LEFT_TEAM_POS_2 = new Position(CENTRE_CIRCLE_POSITION.getX() + 5, CENTRE_CIRCLE_POSITION.getY() + 25);
    public static final Position STARTING_FROM_MIDDLE_RIGHT_TEAM_POS_1 = new Position(CENTRE_CIRCLE_POSITION.getX() + 5, CENTRE_CIRCLE_POSITION.getY() - 25);
    public static final Position STARTING_FROM_MIDDLE_RIGHT_TEAM_POS_2 = new Position(CENTRE_CIRCLE_POSITION.getX() - 5, CENTRE_CIRCLE_POSITION.getY() + 25);
    public static final Position LEFT_GOAL_POSITION = new Position(0, FIELD_HEIGHT / 2);
    public static final Position RIGHT_GOAL_POSITION = new Position(FIELD_WIDTH, FIELD_HEIGHT / 2);
    public static final Position LEFT_UPPER_CORNER = new Position(0, 0);
    public static final Position LEFT_BOTTOM_CORNER = new Position(0, FIELD_HEIGHT);
    public static final Position RIGHT_UPPER_CORNER = new Position(FIELD_WIDTH, 0);
    public static final Position RIGHT_BOTTOM_CORNER = new Position(FIELD_WIDTH, FIELD_HEIGHT);
    public static final Position RESUME_BY_GK_LEFT_POSITION = new Position(SMALL_PENALTY_AREA_WIDTH, FIELD_HEIGHT / 2 + 40);
    public static final Position RESUME_BY_GK_RIGHT_POSITION = new Position(FIELD_WIDTH - SMALL_PENALTY_AREA_WIDTH, FIELD_HEIGHT / 2 - 40);
    public static final Position PENALTY_SPOT_LEFT = new Position(PENALTY_SPOT_WIDTH, FIELD_HEIGHT / 2);
    public static final Position PENALTY_SPOT_RIGHT = new Position(FIELD_WIDTH - PENALTY_SPOT_WIDTH, FIELD_HEIGHT / 2);
    public static final Position END_MATCH_TARGET_POSITION = new Position(FIELD_WIDTH / 2, FIELD_HEIGHT + 100);

    private PlayingField() {
    }

    public static List<Position> getGoalPosts() {
        return GOAL_POSTS;
    }


}
