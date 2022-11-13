package soccer.app.entities.match;

import com.fasterxml.jackson.annotation.*;
import org.checkerframework.checker.optional.qual.Present;
import soccer.app.entities.EntityBase;
import soccer.app.entities.team.Team;
import soccer.app.resolvers.CustomResolver;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NamedQuery(name = Match.LIST_ALL, query = "select m from Match m")
@NamedQuery(name = Match.GET_BY_TEAM, query = "select m from Match m where teamHome = :team OR teamAway = :team")
public class Match extends EntityBase {

    public static final String LIST_ALL = "list-all-matches";
    public static final String GET_BY_TEAM = "get-match-by-team";

    @Present
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime matchStartedTimeActual;

    @NotNull
    private LocalDateTime matchStartedTimePlanned;

    @NotNull
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = CustomResolver.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne
    private Team teamHome;

    @NotNull
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, resolver = CustomResolver.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne
    private Team teamAway;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int teamHomeScore;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int teamAwayScore;

    @JsonGetter
    public String getTeamHomeName() {
        if (teamHome == null) return null;
        return teamHome.getName();
    }

    @JsonGetter
    public String getTeamAwayName() {
        if (teamAway == null) return null;
        return teamAway.getName();
    }

    public LocalDateTime getMatchStartedTimeActual() {
        return matchStartedTimeActual;
    }

    public void setMatchStartedTimeActual(LocalDateTime matchStartedTimeActual) {
        this.matchStartedTimeActual = matchStartedTimeActual;
    }

    public LocalDateTime getMatchStartedTimePlanned() {
        return matchStartedTimePlanned;
    }

    public void setMatchStartedTimePlanned(LocalDateTime matchStartedTimePlanned) {
        this.matchStartedTimePlanned = matchStartedTimePlanned;
    }

    public Team getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(Team teamHome) {
        this.teamHome = teamHome;
    }

    public Team getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(Team teamAway) {
        this.teamAway = teamAway;
    }

    public int getTeamHomeScore() {
        return teamHomeScore;
    }

    public void setTeamHomeScore(int teamHomeScore) {
        this.teamHomeScore = teamHomeScore;
    }

    public int getTeamAwayScore() {
        return teamAwayScore;
    }

    public void setTeamAwayScore(int teamAwayScore) {
        this.teamAwayScore = teamAwayScore;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchStartedTimeActual=" + matchStartedTimeActual +
                ", matchStartedTimePlanned=" + matchStartedTimePlanned +
                ", teamHome=" + teamHome +
                ", teamAway=" + teamAway +
                ", teamHomeScore=" + teamHomeScore +
                ", teamAwayScore=" + teamAwayScore +
                '}';
    }
}
