package soccer.app.repository;

import soccer.app.entities.match.Match;
import soccer.app.entities.team.Team;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


@Transactional
public class MatchRepository implements CrudRepository<Match>, Serializable {

    @Inject
    EntityManager entityManager;

    @Override
    public Match create(Match match) {
        entityManager.persist(match);
        return match;
    }

    @Override
    public List<Match> list() {
        return entityManager.createNamedQuery(Match.LIST_ALL, Match.class).getResultList();
    }

    @Override
    public Optional<Match> get(Long id) {
        return Optional.ofNullable(entityManager.find(Match.class, id));
    }

    public Optional<Match> getByTeam(Team team) {
        List<Match> teams = entityManager.createNamedQuery(Match.GET_BY_TEAM, Match.class)
                .setParameter("team", team)
                .getResultList();
        if (!teams.isEmpty()) {
            return Optional.ofNullable(teams.get(0));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Match update(Match match) {
        entityManager.merge(match);
        return match;
    }

    @Override
    public void delete(Match match) {
        entityManager.remove(entityManager.contains(match) ? match : entityManager.merge(match));
    }
}
