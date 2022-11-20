package soccer.app.config.security.auth;

import io.jsonwebtoken.Jwts;
import org.jboss.logging.Logger;
import soccer.app.config.security.entity.AppUser;
import soccer.app.config.security.entity.Role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ApplicationScoped
public class JwtTokenService {
    public static final int EXPIRATION_TIME_IN_MIN = 360;
    private static final String ISSUER = "soccer_app";
    private static final String AUDIENCE = "soccer_app_user";
    @Inject
    Logger logger;
    private PrivateKey privateKey;

    private void readPrivateKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        char[] password = System.getProperty("privateKeyPassword").toCharArray();
        String alias = System.getProperty("privateKeyAlias");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(getKeyStorePath()), password);
        privateKey = (PrivateKey) keyStore.getKey(alias, password);
    }

    private String getKeyStorePath() {
        String jbossHomePath = System.getenv("JBOSS_HOME");
        if (jbossHomePath == null) {
            throw new IllegalStateException("JBOSS_HOME not set. Unable to get keystore path");
        }
        Path keyStorePath = Paths.get(jbossHomePath, "soccer", "soccer.keystore");
        if (!keyStorePath.toFile().isFile()) {
            throw new IllegalStateException("KeyStore now found at path: " + keyStorePath.toString());
        }
        return keyStorePath.toString();
    }

    public String generateToken(AppUser user) {
        try {
            readPrivateKey();
        } catch (Exception e) {
            logger.error("Unable to read private key");
            logger.error(e);
            throw new UnsupportedOperationException("Unable to generate jwt token");
        }
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuer(ISSUER)
                .signWith(privateKey)
                .setAudience(AUDIENCE)
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(EXPIRATION_TIME_IN_MIN).atZone(ZoneId.systemDefault()).toInstant()))
                .claim("groups", user.getUserRoles().stream().map(Role::toString).toList())
                .compact();
    }
}
