package soccer.app.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import soccer.app.config.security.UserInfo;
import soccer.app.config.security.entity.AppUser;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("user")
@Api(value = "User")
@SwaggerDefinition(tags = {
        @Tag(name = "User", description = "Currently logged in user information")
})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {
    @Inject
    UserInfo userInfo;

    @Path("me")
    @GET
    public Response getCurrentUser() {
        Optional<AppUser> optionalUser = userInfo.getUser();
        if (optionalUser.isEmpty()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(optionalUser.get()).build();
    }
}
