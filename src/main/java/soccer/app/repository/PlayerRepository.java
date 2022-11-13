package soccer.app.repository;

import soccer.app.entities.player.Player;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
public class PlayerRepository implements CrudRepository<Player> {

    @Inject
    EntityManager entityManager;

    @Override
    public Player create(Player player) {
        entityManager.persist(player);
        return player;
    }

    @Override
    public List<Player> list() {
        return entityManager.createNamedQuery(Player.LIST_ALL, Player.class).getResultList();
    }

    @Override
    public Optional<Player> get(Long id) {
        return Optional.ofNullable(entityManager.find(Player.class, id));
    }

    @Override
    public Player update(Player object) {
        return null;
    }

    @Override
    public void delete(Player object) {
        // TODO
    }
}
