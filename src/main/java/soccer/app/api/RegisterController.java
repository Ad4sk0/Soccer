package soccer.app.api;

import io.swagger.annotations.*;
import soccer.app.config.security.auth.RegistrationService;
import soccer.app.config.security.entity.AppUser;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("register")
@Api(value = "Register")
@SwaggerDefinition(tags = {
        @Tag(name = "Register", description = "Endpoint used for registration of new users")
})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegisterController {
    @Inject
    RegistrationService registrationService;

    @POST
    @ApiOperation(value = "User registration")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Registration successful"),
            @ApiResponse(code = 400, message = "Registration not successful")
    })
    public Response registerUser(@Valid AppUser appUser) {
        try {
            appUser = registrationService.registerUser(appUser);
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error:\":\"Registration error\"}").build();
        }
        return Response.ok(appUser).build();
    }
}
