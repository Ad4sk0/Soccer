package soccer.app.repository;

import soccer.app.entities.team.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Transactional
public class TeamRepository implements CrudRepository<Team>, Serializable {

    @Inject
    EntityManager entityManager;

    @Override
    public Team create(Team team) {
        entityManager.persist(team);
        return team;
    }

    @Override
    public List<Team> list() {
        return entityManager.createNamedQuery(Team.LIST_ALL, Team.class).getResultList();
    }

    @Override
    public Optional<Team> get(Long id) {
        return Optional.ofNullable(entityManager.find(Team.class, id));
    }

    public Optional<Team> getByName(String name) {
        List<Team> teams = entityManager.createNamedQuery(Team.GET_BY_NAME, Team.class)
                .setParameter("name", name)
                .getResultList();
        if (!teams.isEmpty()) {
            return Optional.ofNullable(teams.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Team update(Team team) {
        entityManager.merge(team);
        return team;
    }

    @Override
    public void delete(Team team) {
        entityManager.remove(entityManager.contains(team) ? team : entityManager.merge(team));
    }
}
