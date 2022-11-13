package soccer.app.config;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@DatabaseIdentityStoreDefinition(
        callerQuery = "select password from my_app_user where login = ?",
        dataSourceLookup = "java:/PostgresDS",
        groupsQuery = "select role from user_role where user_id = (select id from my_app_user where login = ?)",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        priorityExpression = "80",
        hashAlgorithmParameters = {
                "Pbkdf2PasswordHash.Iterations=3072",
                "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
                "Pbkdf2PasswordHash.SaltSizeBytes=64"
        }
)
@ApplicationScoped
public class IdentityStoreConfig {

}
