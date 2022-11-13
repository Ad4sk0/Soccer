package soccer.app.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    T create(T object);

    List<T> list();

    Optional<T> get(Long id);

    T update(T object);

    void delete(T object);
}
