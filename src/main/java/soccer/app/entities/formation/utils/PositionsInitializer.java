package soccer.app.entities.formation.utils;

import soccer.app.entities.formation.FormationPosition;
import soccer.models.positions.DefaultPositionsUtils;
import soccer.models.positions.InGamePosition;
import soccer.models.positions.PlayingPosition;
import soccer.utils.Position;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PositionsInitializer {

    private static EnumMap<PlayingPosition, List<FormationPosition>> positionToPlayersMap;

    private PositionsInitializer() {
    }

    public static void initializePositionsBasedOnFormation(List<FormationPosition> positionsList) {
        // Determine number of players on the same position
        prepareMapPlayersForEachPosition(positionsList);
        for (Map.Entry<PlayingPosition, List<FormationPosition>> entry : positionToPlayersMap.entrySet()) {
            PlayingPosition playingPosition = entry.getKey();
            List<FormationPosition> formationPositionList = entry.getValue();
            if (formationPositionList.size() == 1) {
                initializePositionWithoutDuplicates(playingPosition, formationPositionList.get(0));
            } else {
                initializePositionWithDuplicates(playingPosition, formationPositionList);
            }
        }
    }

    private static void initializePositionWithoutDuplicates(PlayingPosition playingPosition, FormationPosition formationPosition) {
        formationPosition.setBasePosition(playingPosition.getDefaultBasePosition());
        formationPosition.setResumeByFriendlyGkPosition(playingPosition.getDefaultResumeByFriendlyGkPosition());
        formationPosition.setResumeByOppositeGkPosition(playingPosition.getDefaultResumeByOppositeGkPosition());
        formationPosition.setCornerOnFriendlySitePosition(playingPosition.getDefaultCornerOnFriendlySitePosition());
        formationPosition.setCornerOnOppositeSitePosition(playingPosition.getDefaultCornerOnOppositeSitePosition());
    }

    private static void initializePositionWithDuplicates(PlayingPosition playingPosition, List<FormationPosition> formationPositionList) {
        for (InGamePosition inGamePosition : InGamePosition.values()) {
            Position currentPosition;
            double verticalSpaceForEachPlayer = DefaultPositionsUtils.VERTICAL_SPACE_FOR_EACH_PLAYER;
            switch (inGamePosition) {
                case BASE_POSITION -> currentPosition = playingPosition.getDefaultBasePosition();
                case RESUME_BY_FRIENDLY_GK_POSITION ->
                        currentPosition = playingPosition.getDefaultResumeByFriendlyGkPosition();
                case RESUME_BY_OPPOSITE_GK_POSITION ->
                        currentPosition = playingPosition.getDefaultResumeByOppositeGkPosition();
                case CORNER_ON_FRIENDLY_SITE_POSITION -> {
                    currentPosition = playingPosition.getDefaultCornerOnFriendlySitePosition();
                    verticalSpaceForEachPlayer = DefaultPositionsUtils.VERTICAL_SPACE_FOR_EACH_PLAYER_IN_PENALTY_AREA;
                }
                case CORNER_ON_OPPOSITE_SITE_POSITION -> {
                    currentPosition = playingPosition.getDefaultCornerOnOppositeSitePosition();
                    verticalSpaceForEachPlayer = DefaultPositionsUtils.VERTICAL_SPACE_FOR_EACH_PLAYER_IN_PENALTY_AREA;
                }
                default -> throw new IllegalStateException("Unhandled inGamePosition " + inGamePosition);
            }

            List<Position> resultPositions = calculateAdjustedCoordinatesForDuplicatePositions(playingPosition, formationPositionList, currentPosition, verticalSpaceForEachPlayer);

            for (int i = 0; i < resultPositions.size(); i++) {
                switch (inGamePosition) {
                    case BASE_POSITION -> formationPositionList.get(i).setBasePosition(resultPositions.get(i));
                    case RESUME_BY_FRIENDLY_GK_POSITION ->
                            formationPositionList.get(i).setResumeByFriendlyGkPosition(resultPositions.get(i));
                    case RESUME_BY_OPPOSITE_GK_POSITION ->
                            formationPositionList.get(i).setResumeByOppositeGkPosition(resultPositions.get(i));
                    case CORNER_ON_FRIENDLY_SITE_POSITION ->
                            formationPositionList.get(i).setCornerOnFriendlySitePosition(resultPositions.get(i));
                    case CORNER_ON_OPPOSITE_SITE_POSITION ->
                            formationPositionList.get(i).setCornerOnOppositeSitePosition(resultPositions.get(i));
                    default -> throw new IllegalStateException("Unhandled inGamePosition " + inGamePosition);
                }
            }
        }
    }

    private static List<Position> calculateAdjustedCoordinatesForDuplicatePositions(PlayingPosition playingPosition, List<FormationPosition> formationPositionList, Position defaultPosition, double spaceForEachPlayer) {
        List<Position> resultPositions = new ArrayList<>();
        // Put all players between min and max y for this position
        double defaultX = defaultPosition.getX();
        double defaultY = defaultPosition.getY();
        double minY = defaultY - spaceForEachPlayer / 2;
        double maxY = defaultY + spaceForEachPlayer / 2;
        double adjustedSpaceForEachPlayer = (maxY - minY) / (formationPositionList.size());

        for (int i = 0; i < formationPositionList.size(); i++) {
            double y = minY + adjustedSpaceForEachPlayer / 2 + adjustedSpaceForEachPlayer * i;
            if (y > maxY) {
                throw new ArithmeticException("Error on calculating position for formation position " + playingPosition);
            }
            resultPositions.add(new Position(defaultX, y));
        }
        if (resultPositions.size() != formationPositionList.size()) {
            throw new ArithmeticException("Not all formations initialized");
        }
        return resultPositions;
    }
    
    private static void prepareMapPlayersForEachPosition(List<FormationPosition> positionsList) {
        positionToPlayersMap = new EnumMap<>(PlayingPosition.class);
        for (FormationPosition formationPosition : positionsList) {
            PlayingPosition playingPosition = formationPosition.getPosition();
            if (positionToPlayersMap.containsKey(playingPosition)) {
                positionToPlayersMap.get(playingPosition).add(formationPosition);
            } else {
                positionToPlayersMap.put(playingPosition, new ArrayList<>(List.of(formationPosition)));
            }
        }
    }
}
