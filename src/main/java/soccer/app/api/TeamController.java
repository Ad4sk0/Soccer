package soccer.app.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.jboss.logging.Logger;
import soccer.app.config.security.UserInfo;
import soccer.app.config.security.UserRepository;
import soccer.app.config.security.entity.AppUser;
import soccer.app.entities.formation.Formation;
import soccer.app.entities.formation.Formations;
import soccer.app.entities.team.ShirtSet;
import soccer.app.entities.team.Team;
import soccer.app.generators.RandomTeamGenerator;
import soccer.app.repository.TeamRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("team")
@Api(value = "Team")
@SwaggerDefinition(tags = {
        @Tag(name = "Team", description = "CRUD Endpoint for team model")
})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TeamController implements CrudController<Team> {
    @Inject
    UserInfo userInfo;
    @Inject
    Logger logger;
    @Inject
    TeamRepository teamRepository;
    @Inject
    UserRepository userRepository;
    @Context
    private UriInfo uriInfo;

    @Override
    @POST
    public Response create(@Valid Team team) {
        logger.debug("Team post request from user: " + userInfo.getUser());

        // Get current User
        Optional<AppUser> appUserOptional = userInfo.getUser();
        if (appUserOptional.isEmpty()) {
            logger.error("Cannot find user");
            return Response.status(500).build();
        }
        AppUser appUser = appUserOptional.get();

        // Validate duplicates
        if (userInfo.hasTeam()) {
            return Response.status(Response.Status.CONFLICT).entity("User already has a team").build();
        }
        if (teamRepository.getByName(team.getName()).isPresent()) {
            return Response.status(Response.Status.CONFLICT).entity("Team name already taken").build();
        }

        // Update Shirt sets info
        for (ShirtSet shirtSet : team.getShirtSets()) {
            shirtSet.setTeam(team);
        }

        // Generate Players
        team.setFormation(new Formation(Formations.FOUR_FOUR_TWO));
        team.setPlayers(RandomTeamGenerator.createRandomTeamPlayers(team, team.getFormation(), 5));
        team.assignPlayersToPositions();

        // Save team
        team.setName(team.getName().trim());
        teamRepository.create(team);

        // Update user info
        appUser.setTeam(team);
        userRepository.update(appUser);

        // Send response
        URI createdURI = uriInfo.getAbsolutePathBuilder().path(String.valueOf(team.getId())).build();
        return Response.created(createdURI).entity(team).build();
    }

    @Override
    @GET
    public Response list() {
        logger.debug("Team list request from user: " + userInfo.getUser());
        return Response.ok(teamRepository.list()).build();
    }

    @Override
    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long teamId) {
        logger.debug("Team post request from user: " + userInfo.getUser() + " for team with id: " + teamId);
        Optional<Team> team = teamRepository.get(teamId);
        if (team.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(team.get()).build();
    }

    @GET
    @Path("/me")
    public Response getMyTeam() {
        logger.debug("Team get request from user: " + userInfo.getUser() + " for his team");
        Team team = userInfo.getTeam();
        if (team == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(team).build();
    }

    @Override
    @PUT
    public Response update(@Valid Team team) {
        logger.debug("Team update request from user: " + userInfo.getUser());
        return null;
    }

    @Override
    @DELETE
    public Response delete() {
        logger.debug("Team delete request from user: " + userInfo.getUser());

        // Get current User
        Optional<AppUser> appUserOptional = userInfo.getUser();
        if (appUserOptional.isEmpty()) {
            logger.error("Cannot find user");
            return Response.status(500).build();
        }
        AppUser appUser = appUserOptional.get();
        Team teamToDelete = appUser.getTeam();


        // Update user info
        appUser.setTeam(null);
        userRepository.update(appUser);

        // Delete team
        teamRepository.delete(teamToDelete);

        return Response.ok().build();
    }

    @PUT
    @Path("change-formation/{formation}")
    public Response changeFormation(@PathParam("formation") Formations formation) {
        logger.debug("Change formation to " + formation + "  request from: " + userInfo.getUser());
        Team myTeam = userInfo.getTeam();
        if (formation.equals(myTeam.getFormation().getName())) {
            return Response.ok(myTeam).build();
        }
        myTeam.changeFormation(formation);
        teamRepository.update(myTeam);
        return Response.ok(myTeam).build();
    }

}
