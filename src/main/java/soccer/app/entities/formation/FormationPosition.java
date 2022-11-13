package soccer.app.entities.formation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import soccer.app.entities.EntityBase;
import soccer.app.entities.player.Player;
import soccer.models.positions.PlayingPosition;
import soccer.utils.Position;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class FormationPosition extends EntityBase implements Comparable<FormationPosition>, Serializable {

    @Enumerated(EnumType.STRING)
    @NotNull
    PlayingPosition position;

    @OneToOne
    Player player;

    @ManyToOne
    @NotNull
    @JsonIgnore
    Formation formation;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "base_position_x")),
            @AttributeOverride(name = "y", column = @Column(name = "base_position_y"))
    })
    Position basePosition;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "resume_by_friendly_gk_x")),
            @AttributeOverride(name = "y", column = @Column(name = "resume_by_friendly_gk_y"))
    })
    Position resumeByFriendlyGkPosition;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "resume_by_opposite_gk_x")),
            @AttributeOverride(name = "y", column = @Column(name = "resume_by_opposite_gk_y"))
    })
    Position resumeByOppositeGkPosition;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "corner_on_friendly_site_x")),
            @AttributeOverride(name = "y", column = @Column(name = "corner_on_friendly_site_y"))
    })
    Position cornerOnFriendlySitePosition;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "x", column = @Column(name = "corner_on_opposite_site_x")),
            @AttributeOverride(name = "y", column = @Column(name = "corner_on_opposite_site_y"))
    })
    Position cornerOnOppositeSitePosition;


    public FormationPosition() {
    }

    public FormationPosition(Formation formation) {
        this.formation = formation;
    }

    public boolean isTaken() {
        return player != null;
    }

    public boolean isFree() {
        return player == null;
    }

    public PlayingPosition getPosition() {
        return position;
    }

    public void setPosition(PlayingPosition position) {
        this.position = position;
    }

    @JsonGetter("player")
    public Long getPlayerId() {
        if (player == null) return null;
        return player.getId();
    }

    public Player getPlayer() {
        return player;
    }

    public void assignPlayer(Player player) {
        this.player = player;
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Position getBasePosition() {
        return basePosition;
    }

    public void setBasePosition(Position basePosition) {
        this.basePosition = basePosition;
    }

    public Position getResumeByFriendlyGkPosition() {
        return resumeByFriendlyGkPosition;
    }

    public void setResumeByFriendlyGkPosition(Position resumeByFriendlyGkPosition) {
        this.resumeByFriendlyGkPosition = resumeByFriendlyGkPosition;
    }

    public Position getResumeByOppositeGkPosition() {
        return resumeByOppositeGkPosition;
    }

    public void setResumeByOppositeGkPosition(Position resumeByOppositeGkPosition) {
        this.resumeByOppositeGkPosition = resumeByOppositeGkPosition;
    }

    public Position getCornerOnFriendlySitePosition() {
        return cornerOnFriendlySitePosition;
    }

    public void setCornerOnFriendlySitePosition(Position cornerOnFriendlySitePosition) {
        this.cornerOnFriendlySitePosition = cornerOnFriendlySitePosition;
    }

    public Position getCornerOnOppositeSitePosition() {
        return cornerOnOppositeSitePosition;
    }

    public void setCornerOnOppositeSitePosition(Position cornerOnOppositeSitePosition) {
        this.cornerOnOppositeSitePosition = cornerOnOppositeSitePosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormationPosition that = (FormationPosition) o;
        return position == that.position && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, player);
    }

    @Override
    public int compareTo(FormationPosition other) {
        if (other == null) return -1;
        return Integer.compare(position.getOrder(), other.position.getOrder());
    }

    @Override
    public String toString() {
        return "FormationPosition{" +
                "position=" + position +
                '}';
    }
}
