package soccer.app.config.security;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.PasswordHash;

@RequestScoped
public class PasswordHashingService {

    @Inject
    PasswordHash passwordHash;

    public String hashPassword(char[] password) {
        return passwordHash.generate(password);
    }

    public boolean arePasswordsTheSame(char[] passwordToCheck, String passwordStored) {
        return passwordHash.verify(passwordToCheck, passwordStored);
    }

}
