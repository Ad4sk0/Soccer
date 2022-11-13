package soccer.app.entities.team;

import soccer.app.entities.EntityBase;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class TeamStrategy extends EntityBase implements Serializable {

    @NotNull
    private String strategy;

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
