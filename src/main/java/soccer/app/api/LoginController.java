package soccer.app.api;

import io.swagger.annotations.*;
import soccer.app.config.security.auth.AuthenticationService;
import soccer.app.config.security.entity.AppUser;

import javax.inject.Inject;
import javax.security.auth.login.CredentialException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("login")
@Api(value = "Login")
@SwaggerDefinition(tags = {
        @Tag(name = "Login", description = "Endpoint used for login")
})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    @Inject
    AuthenticationService authenticationService;

    @POST
    @ApiOperation(value = "Login using username and password. Returns JWT token")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Authentication successful"),
            @ApiResponse(code = 400, message = "Authentication not successful")
    })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response loginUser(@FormParam("login") String login, @FormParam("password") String password) {
        AppUser appUser;

        // Try to log in the user
        try {
            appUser = authenticationService.login(login, password.toCharArray());
        } catch (CredentialException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error:\":\"Wrong credentials\"}").build();
        }

        // Return token
        String token = authenticationService.generateToken(appUser);
        return Response.ok("{\"token\": \"" + token + "\"}").build();
    }

}
