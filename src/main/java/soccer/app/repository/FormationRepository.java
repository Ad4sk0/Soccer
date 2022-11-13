package soccer.app.repository;

import soccer.app.entities.formation.Formation;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
public class FormationRepository implements CrudRepository<Formation> {

    @Inject
    EntityManager entityManager;

    @Override
    public Formation create(Formation object) {
        return null;
    }

    @Override
    public List<Formation> list() {
        return new ArrayList<>();
    }

    @Override
    public Optional<Formation> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Formation update(Formation object) {
        return null;
    }

    @Override
    public void delete(Formation formation) {
        entityManager.remove(formation);
    }
}
