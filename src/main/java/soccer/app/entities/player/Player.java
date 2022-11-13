package soccer.app.entities.player;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import soccer.app.entities.EntityBase;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.team.Team;
import soccer.models.positions.PlayingPosition;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NamedQuery(name = Player.LIST_ALL, query = "select p from Player p")
public class Player extends EntityBase implements Serializable {

    public static final String LIST_ALL = "list-all-players";

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PlayingPosition preferredPosition;

    @ManyToOne
    @JsonIgnore
    private Team team;

    @Embedded
    @JsonUnwrapped
    private PlayerStats stats;

    @Embedded
    @JsonUnwrapped
    private TeamAttributes teamAttributes;

    private double condition = 100;


    public Player() {
        this.stats = new PlayerStats();
    }

    public Player(String name, PlayingPosition preferredPosition, Integer number, Team team, PlayerStats stats) {
        this.name = name;
        this.preferredPosition = preferredPosition;
        this.team = team;
        this.stats = stats;
        this.teamAttributes = new TeamAttributes();
        this.teamAttributes.setNumber(number);
    }


    @JsonGetter("assignedPosition")
    public PlayingPosition getAssignedPositionValue() {
        FormationPosition assignedPosition = getAssignedPosition();
        if (assignedPosition == null) return null;
        return assignedPosition.getPosition();
    }

    public FormationPosition getAssignedPosition() {
        return team.getPlayerPosition(this);
    }

    public boolean isSubstitute() {
        return getAssignedPosition() == null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    public PlayingPosition getPreferredPosition() {
        return preferredPosition;
    }

    public void setPreferredPosition(PlayingPosition preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getOverall() {
        return stats.getStats().stream().reduce(0, Integer::sum) / stats.getStats().size();
    }

    public TeamAttributes getTeamAttributes() {
        return teamAttributes;
    }

    public void setTeamAttributes(TeamAttributes teamAttributes) {
        this.teamAttributes = teamAttributes;
    }

    public double getCondition() {
        return condition;
    }

    public void setCondition(double condition) {
        this.condition = condition;
    }

    public int getNumber() {
        if (this.team == null) {
            throw new IllegalStateException("Player has not assigned team yet");
        }
        return teamAttributes.getNumber();
    }

    public int getSpeed() {
        return stats.getSpeed();
    }

    public int getEndurance() {
        return stats.getEndurance();
    }

    public int getAcceleration() {
        return stats.getAcceleration();
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", preferredPosition=" + preferredPosition +
                ", overall=" + getOverall() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name)
                && Objects.equals(getOverall(), player.getOverall())
                && Objects.equals(preferredPosition, player.preferredPosition)
                && Objects.equals(team, player.team)
                && Objects.equals(getNumber(), player.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, preferredPosition, team, getOverall());
    }

    public int getShotPower() {
        return stats.getShotPower();
    }
}
