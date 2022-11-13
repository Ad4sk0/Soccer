package soccer.app.config.security.auth;

import org.jboss.logging.Logger;
import soccer.app.config.security.PasswordHashingService;
import soccer.app.config.security.UserRepository;
import soccer.app.config.security.entity.AppUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import java.util.Optional;

@RequestScoped
public class AuthenticationService {

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    UserRepository userRepository;

    @Inject
    Logger logger;

    @Inject
    PasswordHashingService passwordHashingService;

    public AppUser login(String username, char[] password) throws CredentialException {

        Optional<AppUser> user = userRepository.get(username);

        // Check if user exists
        if (user.isEmpty()) {
            logger.debug("Wrong login: " + username);
            throw new CredentialException("Invalid login");
        }

        // Check credentials
        if (!passwordHashingService.arePasswordsTheSame(password, user.get().getPassword())) {
            logger.debug("Wrong password");
            throw new CredentialException("Credentials invalid");
        }

        logger.debug("User: " + username + " logged in");
        return user.get();
    }

    public String generateToken(AppUser user) {
        return jwtTokenService.generateToken(user);
    }
}
