package soccer.app.entities.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import soccer.app.entities.EntityBase;
import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.FormationPosition;
import soccer.app.entities.formation.Formations;
import soccer.app.entities.formation.utils.PositionsAssigner;
import soccer.app.entities.player.Player;
import soccer.app.generators.RandomTeamGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.*;

@Entity
@NamedQuery(name = Team.LIST_ALL, query = "select t from Team t")
@NamedQuery(name = Team.GET_BY_NAME, query = "select t from Team t where name = :name")
public class Team extends EntityBase implements Serializable {

    public static final String SHIRTS_NOT_SET_ERROR_MSG = "Team must have at least one shirt set";
    public static final String LIST_ALL = "list-all-teams";
    public static final String GET_BY_NAME = "get-team-by-name";

    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @NotNull
    private Set<ShirtSet> shirtSets;

    @OneToOne
    @JsonIgnore
    private TeamStrategy teamStrategy;

    @OneToOne(cascade = {CascadeType.ALL})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Formation formation;

    @OneToMany(mappedBy = "team", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Player> players;

    public Team() {
        players = new ArrayList<>();
        shirtSets = new HashSet<>();
        formation = new Formation();
    }

    public void initDefault() {
        formation = new Formation(Formations.FOUR_FOUR_TWO);
        players = RandomTeamGenerator.createRandomTeamPlayers(this, formation, 5);
        assignPlayersToPositions();
        shirtSets.add(new ShirtSet(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW));
        name = "default";
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @PreRemove
    private void preRemove() {
        players.forEach(player -> player.setTeam(null));
    }

    public void changeFormation(Formations formationName) {
        formation.changeFormation(formationName);
        assignPlayersToPositions();
    }

    public void assignPlayersToPositions() {
        PositionsAssigner positionsAssigner = new PositionsAssigner(formation, players);
        positionsAssigner.assignPlayersToPositions();
        sortPlayersByPosition();
    }

    public FormationPosition getPlayerPosition(Player player) {
        for (FormationPosition position : formation.getPositionsList()) {
            if (position.isTaken() && position.getPlayer().equals(player)) {
                return position;
            }
        }
        return null;
    }

    public void sortPlayersByPosition() {
        players.sort((p1, p2) -> {
            if (p1.isSubstitute()) {
                return 1;
            }
            return p1.getAssignedPosition().compareTo(p2.getAssignedPosition());
        });
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ShirtSet> getShirtSets() {
        return shirtSets;
    }

    public void setShirtSets(Set<ShirtSet> shirtSets) {
        this.shirtSets = shirtSets;
    }

    public TeamStrategy getTeamStrategy() {
        return teamStrategy;
    }

    public void setTeamStrategy(TeamStrategy teamStrategy) {
        this.teamStrategy = teamStrategy;
    }

    public List<Player> getPlayers() {
        sortPlayersByPosition();
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addShirtSet(ShirtSet shirtSet) {
        shirtSets.add(shirtSet);
    }

    public Formation getFormation() {
        return formation;
    }

    public void setFormation(Formation formation) {
        this.formation = formation;
    }

    public Color getShirtColor() {
        if (shirtSets.isEmpty()) {
            throw new IllegalStateException(SHIRTS_NOT_SET_ERROR_MSG);
        }
        return shirtSets.iterator().next().getShirtColor();
    }

    public Color getNumberColor() {
        if (shirtSets.isEmpty()) {
            throw new IllegalStateException(SHIRTS_NOT_SET_ERROR_MSG);
        }
        return shirtSets.iterator().next().getNumberColor();
    }

    public Color getGoalkeeperShirtColor() {
        if (shirtSets.isEmpty()) {
            throw new IllegalStateException(SHIRTS_NOT_SET_ERROR_MSG);
        }
        return shirtSets.iterator().next().getGoalkeeperShirtColor();
    }

    public Color getGoalkeeperNumberColor() {
        if (shirtSets.isEmpty()) {
            throw new IllegalStateException(SHIRTS_NOT_SET_ERROR_MSG);
        }
        return shirtSets.iterator().next().getGoalkeeperNumberColor();
    }

    @Override
    public String toString() {
        return "Team{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
