package soccer.app.resolvers;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import soccer.app.entities.team.Team;
import soccer.app.repository.TeamRepository;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import java.util.Optional;

@Dependent
public class CustomResolver extends SimpleObjectIdResolver {
    TeamRepository teamRepository = CDI.current().select(TeamRepository.class).get();

    @Override
    public Object resolveId(ObjectIdGenerator.IdKey id) {
        Optional<Team> optionalTeam = teamRepository.get((Long) id.key);
        if (optionalTeam.isEmpty()) {
            throw new IllegalArgumentException("Team with given id doesn't exist");
        }
        Team team = optionalTeam.get();
        bindItem(id, team);
        return team;
    }

    @Override
    public ObjectIdResolver newForDeserialization(Object context) {
        return new CustomResolver();
    }

    @Override
    public boolean canUseFor(ObjectIdResolver resolverType) {
        return resolverType.getClass() == this.getClass();
    }
}
