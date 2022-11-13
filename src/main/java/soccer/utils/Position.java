package soccer.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import soccer.models.playingfield.PlayingField;
import soccer.models.playingfield.PlayingFieldUtils;

import javax.persistence.Embeddable;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
public class Position implements Serializable {
    private double x;
    private double y;

    public Position() {
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public static Position add(Position pos1, Position pos2) {
        Position result = new Position(pos1.getX(), pos1.getY());
        result.addPosition(pos2);
        return result;
    }

    public void addPosition(Position otherPosition) {
        this.x += otherPosition.getX();
        this.y += otherPosition.getY();
    }

    public void addVector(Vector2d vec) {
        this.x += vec.getX();
        this.y += vec.getY();
    }

    public void subtractVector(Vector2d vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
    }

    public double getDistance(Position otherPosition) {
        return Point2D.distance(x, y, otherPosition.getX(), otherPosition.getY());
    }

    public boolean isOnLeftSite() {
        return isOnPlayingField() && x >= 0 && x < PlayingField.FIELD_WIDTH / 2;
    }

    public boolean isBehindLeftLine() {
        return x < 0;
    }

    public boolean isOnRightSite() {
        return isOnPlayingField() && x > PlayingField.FIELD_WIDTH / 2 && x <= PlayingField.FIELD_WIDTH;
    }

    public boolean isBehindRightLine() {
        return x > PlayingField.FIELD_WIDTH;
    }

    public boolean isOnUpperHalf() {
        return isOnPlayingField() && y >= 0 && y < PlayingField.FIELD_HEIGHT / 2;
    }

    public boolean isOnBottomHalf() {
        return isOnPlayingField() && y > PlayingField.FIELD_HEIGHT / 2 && y <= PlayingField.FIELD_HEIGHT;
    }

    public boolean isOutsidePlayingField() {
        return x < 0 || x > PlayingField.FIELD_WIDTH || y < 0 || y > PlayingField.FIELD_HEIGHT;
    }

    public boolean isOnPlayingField() {
        return !isOutsidePlayingField();
    }

    public boolean isInPenaltyAreaHeight() {
        return y >= PlayingField.PENALTY_AREA_UPPER_LINE_HEIGHT && y <= PlayingField.PENALTY_AREA_BOTTOM_LINE_HEIGHT;
    }

    public boolean isInLeftPenaltyArea() {
        return isOnPlayingField() && isInPenaltyAreaHeight() && x <= PlayingField.PENALTY_AREA_WIDTH;
    }

    public boolean isInRightPenaltyArea() {
        return isOnPlayingField() && isInPenaltyAreaHeight() && x >= PlayingFieldUtils.moveXToOtherHalf(PlayingField.PENALTY_AREA_WIDTH);
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position[x=" + x + ", y=" + y + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Position other = (Position) obj;
        return getDistance(other) < 1;
    }
}
