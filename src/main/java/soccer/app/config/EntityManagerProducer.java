package soccer.app.config;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Dependent
public class EntityManagerProducer {

    @Produces
    @PersistenceContext
    EntityManager entityManager;
}