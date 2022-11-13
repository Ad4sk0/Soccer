package soccer.app.config.security.auth;

import org.jboss.logging.Logger;
import soccer.app.config.security.UserService;
import soccer.app.config.security.entity.AppUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class RegistrationService {

    @Inject
    UserService userService;

    @Inject
    Logger logger;

    public AppUser registerUser(AppUser appUser) throws IllegalArgumentException {
        Optional<AppUser> existingUser = userService.get(appUser.getLogin());

        // Check if user exists
        if (existingUser.isPresent()) {
            logger.debug("User exists " + appUser.getLogin());
            throw new IllegalArgumentException("Login already taken");
        }

        userService.save(appUser);

        logger.debug("User: " + appUser.getLogin() + " registered successfully");
        return appUser;
    }
}
