package soccer.app.entities.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.json.bind.annotation.JsonbTransient;
import java.beans.Transient;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PlayerStats implements Serializable {

    @Serial
    private static final long serialVersionUID = 8002860013622059339L;

    @StatsConstraint
    private int speed = 50;

    @StatsConstraint
    private int acceleration = 50;

    @StatsConstraint
    private int shotAccuracy = 50;

    @StatsConstraint
    private int passingAccuracy = 50;

    @StatsConstraint
    private int strength = 50;

    @StatsConstraint
    private int endurance = 50;

    @StatsConstraint
    private int shotPower = 50;

    public PlayerStats() {
    }

    public PlayerStats(int speed, int acceleration, int shotAccuracy, int passingAccuracy, int strength, int endurance, int shotPower) {
        this.speed = speed;
        this.acceleration = acceleration;
        this.shotAccuracy = shotAccuracy;
        this.passingAccuracy = passingAccuracy;
        this.strength = strength;
        this.endurance = endurance;
        this.shotPower = shotPower;
    }

    @JsonIgnore
    @JsonbTransient
    @Transient
    public List<Integer> getStats() {
        return new ArrayList<>(List.of(
                speed,
                acceleration,
                shotAccuracy,
                passingAccuracy,
                strength,
                endurance,
                shotPower
        ));
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public int getShotAccuracy() {
        return shotAccuracy;
    }

    public void setShotAccuracy(int shotAccuracy) {
        this.shotAccuracy = shotAccuracy;
    }

    public int getPassingAccuracy() {
        return passingAccuracy;
    }

    public void setPassingAccuracy(int passingAccuracy) {
        this.passingAccuracy = passingAccuracy;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getShotPower() {
        return shotPower;
    }

    public void setShotPower(int shotPower) {
        this.shotPower = shotPower;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "speed=" + speed +
                ", acceleration=" + acceleration +
                ", shotAccuracy=" + shotAccuracy +
                ", passingAccuracy=" + passingAccuracy +
                ", strength=" + strength +
                ", endurance=" + endurance +
                ", shotPower=" + shotPower +
                '}';
    }
}
