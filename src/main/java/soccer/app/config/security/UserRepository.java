package soccer.app.config.security;

import soccer.app.config.security.entity.AppUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class UserRepository {

    @Inject
    EntityManager entityManager;

    public Optional<AppUser> get(long id) {
        return Optional.ofNullable(entityManager.find(AppUser.class, id));
    }

    public Optional<AppUser> get(String login) {
        List<AppUser> users = entityManager.createNamedQuery(AppUser.GET_BY_LOGIN, AppUser.class)
                .setParameter("login", login)
                .getResultList();
        if (!users.isEmpty()) {
            return Optional.ofNullable(users.get(0));
        } else {
            return Optional.empty();
        }
    }

    public AppUser save(AppUser appUser) {
        entityManager.persist(appUser);
        return appUser;
    }

    public AppUser update(AppUser user) {
        entityManager.merge(user);
        return user;
    }
}
