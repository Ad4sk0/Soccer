package soccer.game.team;

import soccer.app.entities.player.Player;
import soccer.app.entities.team.ShirtSet;
import soccer.app.entities.team.Team;
import soccer.game.entity.ball.Ball;
import soccer.game.entity.player.GamePlayer;
import soccer.game.match.GameMatch;

import java.util.ArrayList;
import java.util.List;

public class GameTeam {

    private final Team team;
    private final String name;
    private final List<GamePlayer> allPlayers = new ArrayList<>();
    private List<GamePlayer> playingPlayers = new ArrayList<>();
    private GameTeamStrategy teamStrategy;
    private int score;

    private ShirtSet shirtSet;

    public GameTeam(Team team) {
        this.team = team;
        this.name = team.getName();
        this.teamStrategy = new GameTeamStrategy(this);
    }

    public void choosePlayingPlayers(GameMatch match, Ball ball) {
        boolean hasGoalkeeper = false;
        for (Player player : team.getPlayers()) {
            allPlayers.add(new GamePlayer(player, match, ball));
            if (player.isSubstitute()) continue;
            GamePlayer gamePlayer = new GamePlayer(player, match, ball);
            playingPlayers.add(gamePlayer);
            if (gamePlayer.isGoalkeeper()) {
                hasGoalkeeper = true;
            }
        }
        if (playingPlayers.size() < 7) {
            throw new IllegalStateException("Team cannot have less than 7 playing players");
        }
        if (!hasGoalkeeper) {
            throw new IllegalStateException("Team must have goalkeeper");
        }
    }


    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public List<GamePlayer> getPlayingPlayers() {
        return playingPlayers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
    }

    public List<GamePlayer> getAllPlayers() {
        return allPlayers;
    }

    public void prepareForMatch(GameMatch match, Ball ball) {
        choosePlayingPlayers(match, ball);
        teamStrategy.assignPlayerRoles();
        chooseShirtSetForMatch();
    }

    public void initPlayersPositions() {
        teamStrategy.initPlayersPositions();
    }

    private void chooseShirtSetForMatch() {
        // TODO
        this.shirtSet = team.getShirtSets().iterator().next();
    }

    public ShirtSet getShirtSet() {
        return shirtSet;
    }
}
