package soccer.app.config;

import io.swagger.jaxrs.config.BeanConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("api")
public class App extends Application {

    public App() {
        initSwagger();
    }

    public void initSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/soccer/api");
        beanConfig.setResourcePackage("soccer.app.api");
        beanConfig.setPrettyPrint(true);
        beanConfig.setTitle("Soccer");
        beanConfig.setDescription("Api for soccer models");
        beanConfig.setScan(true);
    }

}
