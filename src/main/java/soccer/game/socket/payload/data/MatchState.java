package soccer.game.socket.payload.data;

import soccer.game.match.GameMatch;
import soccer.models.playingfield.FieldSite;

import java.util.HashMap;
import java.util.Map;


public class MatchState {

    private final Map<String, TeamState> teams;
    private final Map<String, Integer> scores;
    private final BallState ball;
    private final String matchDuration;

    public MatchState(GameMatch gameMatch) {
        teams = new HashMap<>();
        scores = new HashMap<>();
        teams.put(FieldSite.LEFT.toString(), new TeamState(gameMatch.getGameTeams().get(0)));
        teams.put(FieldSite.RIGHT.toString(), new TeamState(gameMatch.getGameTeams().get(1)));
        scores.put(FieldSite.LEFT.toString(), gameMatch.getGameTeams().get(0).getScore());
        scores.put(FieldSite.RIGHT.toString(), gameMatch.getGameTeams().get(1).getScore());
        ball = new BallState(gameMatch.getBall());
        matchDuration = gameMatch.getMatchDurationString();
    }

    public Map<String, TeamState> getTeams() {
        return teams;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public BallState getBall() {
        return ball;
    }

    public String getMatchDuration() {
        return matchDuration;
    }
}
