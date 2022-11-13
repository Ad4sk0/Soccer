package soccer.game.socket.payload.data;

import soccer.game.match.GameMatch;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class MatchStateInitial {
    private final LocalDateTime matchStartedTime;
    private final HashMap<String, TeamStateInitial> teams;

    public MatchStateInitial(GameMatch gameMatch) {
        matchStartedTime = gameMatch.getMatchStartedTimeActual();
        teams = new HashMap<>();
        teams.put("LEFT_TEAM", new TeamStateInitial(gameMatch.getGameTeams().get(0)));
        teams.put("RIGHT_TEAM", new TeamStateInitial(gameMatch.getGameTeams().get(1)));
    }

    public LocalDateTime getMatchStartedTime() {
        return matchStartedTime;
    }

    public Map<String, TeamStateInitial> getTeams() {
        return teams;
    }
}
