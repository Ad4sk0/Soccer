package soccer.app.config.security.entity;

import com.fasterxml.jackson.annotation.*;
import soccer.app.entities.team.Team;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name = AppUser.GET_BY_LOGIN, query = "select u from AppUser u where login = :login")
public class AppUser {

    public static final String GET_BY_LOGIN = "get-user-by-login";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    @Size(min = 6, message = "Login must be at least 6 letters long")
    @Column(unique = true)
    String login;

    @NotNull
    @Size(min = 6, message = "Password must be at least 6 letters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @Column
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Role> userRoles = new HashSet<>();

    @OneToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Team team;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    int level = 1;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public void addUserRole(Role userRole) {
        this.userRoles.add(userRole);
    }

    @Override
    public String toString() {
        return "User{" +
                "Login=" + login +
                '}';
    }
}
