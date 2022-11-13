package soccer.app.config.security;

import soccer.app.config.security.entity.AppUser;
import soccer.app.config.security.entity.Role;

import javax.inject.Inject;
import java.util.Optional;

public class UserService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordHashingService passwordHashingService;

    public Optional<AppUser> get(long id) {
        return userRepository.get(id);
    }

    public Optional<AppUser> get(String login) {
        return userRepository.get(login);
    }

    public AppUser save(AppUser appUser) {
        appUser.setPassword(passwordHashingService.hashPassword(appUser.getPassword().toCharArray()));
        appUser.addUserRole(Role.USER);
        userRepository.save(appUser);
        return appUser;
    }

    public AppUser update(AppUser user) {
        userRepository.update(user);
        return user;
    }
}
