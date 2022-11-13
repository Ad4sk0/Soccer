package soccer.app.api;

import javax.validation.Valid;
import javax.ws.rs.core.Response;

public interface CrudController<T> {

    /**
     * Returns T
     **/
    Response create(@Valid T object);

    /**
     * Returns List<T>
     **/
    Response list();

    /**
     * Returns T
     **/
    Response get(Long id);

    /**
     * Returns T
     **/
    Response update(@Valid T object);

    /**
     * Returns void
     **/
    Response delete();

}
