package soccer.app.config.security;

import org.jboss.logging.Logger;
import soccer.app.config.security.entity.AppUser;
import soccer.app.entities.team.Team;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.security.Principal;
import java.util.Optional;

@RequestScoped
public class UserInfo {

    @Inject
    UserService userService;

    @Inject
    Principal principal;

    @Inject
    Logger logger;

    public Optional<AppUser> getUser() {
        if (principal.getName().equals("anonymous")) {
            logger.debug("User principal not found in CDI bean: " + principal);
            return Optional.empty();
        }
        return userService.get(principal.getName());
    }

    public boolean hasTeam() {
        Optional<AppUser> user = getUser();
        return user.isPresent() && user.get().getTeam() != null;
    }

    public Team getTeam() {
        Optional<AppUser> user = getUser();
        return user.map(AppUser::getTeam).orElse(null);
    }
}
