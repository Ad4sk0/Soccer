package soccer.app.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.jboss.logging.Logger;
import soccer.app.config.security.UserInfo;
import soccer.app.config.security.entity.AppUser;
import soccer.app.entities.match.Match;
import soccer.app.repository.MatchRepository;
import soccer.game.socket.MatchesHandler;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

@Path("match")
@Api(value = "Match")
@SwaggerDefinition(tags = {
        @Tag(name = "Match", description = "CRUD Endpoint for match model")
})
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MatchController implements CrudController<Match> {
    @Inject
    UserInfo userInfo;

    @Inject
    Logger logger;

    @Inject
    MatchRepository matchRepository;
    @Inject
    MatchesHandler matchesHandler;
    @Context
    private UriInfo uriInfo;

    @Override
    @POST
    public Response create(Match match) {
        logger.debug("Create match: " + match);

        // Get current User
        Optional<AppUser> appUserOptional = userInfo.getUser();
        if (appUserOptional.isEmpty()) {
            logger.error("Cannot find user");
            return Response.status(500).build();
        }

        logger.debug(match);
        matchRepository.create(match);

        URI createdURI = uriInfo.getAbsolutePathBuilder().path(String.valueOf(match.getId())).build();
        return Response.created(createdURI).entity(match).build();
    }

    @Override
    @GET
    public Response list() {
        return Response.ok(matchRepository.list()).build();
    }

    @Override
    public Response get(Long id) {
        return null;
    }

    @Override
    public Response update(Match object) {
        return null;
    }

    @Override
    public Response delete() {
        return null;
    }

    @PUT
    @Path("init-match/{id}")
    public Response initMatch(@PathParam("id") Long matchId) {
        Optional<Match> optionalMatch = matchRepository.get(matchId);
        if (optionalMatch.isEmpty()) {
            String msg = "Cannot find match with id " + matchId;
            logger.debug(msg);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
        Match match = optionalMatch.get();
        matchesHandler.startMatchThread(match);
        return Response.ok().build();
    }

    @PUT
    @Path("start-match/{id}")
    public Response startMatch(@PathParam("id") Long matchId) {
        matchesHandler.startMatch(matchId);
        return Response.ok().build();
    }

    @PUT
    @Path("pause-match/{id}")
    public Response pauseMatch(@PathParam("id") Long matchId) {
        matchesHandler.pauseMatch(matchId);
        return Response.ok().build();
    }

    @PUT
    @Path("resume-match/{id}")
    public Response resumeMatch(@PathParam("id") Long matchId) {
        matchesHandler.resumeMatch(matchId);
        return Response.ok().build();
    }

    @PUT
    @Path("stop-match/{id}")
    public Response stopMatchThread(@PathParam("id") Long matchId) {
        Optional<Match> optionalMatch = matchRepository.get(matchId);
        if (optionalMatch.isEmpty()) {
            String msg = "Cannot find match with id " + matchId;
            logger.debug(msg);
            return Response.status(Response.Status.NOT_FOUND).entity(msg).build();
        }
        Match match = optionalMatch.get();
        logger.debug("Stopping Match thread for match " + match.getTeamHomeName() + " vs. " + match.getTeamAway());
        matchesHandler.removeMatchThread(match.getId());
        return Response.ok().build();
    }

    @GET
    @Path("matches-number")
    public Response getNumberOfMatchThreads() {
        return Response.ok(matchesHandler.getCurrentMatchThreadsNumber()).build();
    }
}
