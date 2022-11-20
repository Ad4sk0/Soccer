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
    private final GameMatch gameMatch;
    private final String name;
    private final List<GamePlayer> allPlayers = new ArrayList<>();
    private final List<GamePlayer> playingPlayers = new ArrayList<>();
    private final GameTeamStrategy teamStrategy;
    private int score;
    private GamePlayer passingTarget;
    private ShirtSet shirtSet;

    public GameTeam(Team team, GameMatch gameMatch) {
        this.team = team;
        this.gameMatch = gameMatch;
        this.name = team.getName();
        this.teamStrategy = new GameTeamStrategy(this);
    }

    public void choosePlayingPlayers(GameMatch match, Ball ball) {
        boolean hasGoalkeeper = false;
        for (Player player : team.getPlayers()) {
            allPlayers.add(new GamePlayer(player, this, match, ball));
            if (player.isSubstitute()) continue;
            GamePlayer gamePlayer = new GamePlayer(player, this, match, ball);
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
        this.shirtSet = team.getShirtSets().iterator().next();
    }

    public ShirtSet getShirtSet() {
        return shirtSet;
    }

    public boolean isLeftSiteTeam() {
        return gameMatch.getLeftSiteTeam().equals(this);
    }

    public boolean isRightSiteTeam() {
        return gameMatch.getRightSiteTeam().equals(this);
    }

    public GameTeam getOppositeTeam() {
        if (gameMatch.getLeftSiteTeam().equals(this)) {
            return gameMatch.getRightSiteTeam();
        } else if (gameMatch.getRightSiteTeam().equals(this)) {
            return gameMatch.getLeftSiteTeam();
        }
        return null;
    }

    public double getLastPlayerForOffsideLine() {
        double result;
        if (!allPlayers.get(0).isGoalkeeper()) {
            result = allPlayers.get(0).getX();
        } else {
            result = allPlayers.get(1).getX();
        }
        for (GamePlayer gamePlayer : playingPlayers) {
            if (gamePlayer.isGoalkeeper()) {
                continue;
            }

            if (isLeftSiteTeam() && gamePlayer.getX() < result) {
                result = gamePlayer.getX();
            }

            if (isRightSiteTeam() && gamePlayer.getX() > result) {
                result = gamePlayer.getX();
            }
        }
        return result;
    }

    public boolean hasBall() {
        return gameMatch.getTeamWithBall() != null && gameMatch.getTeamWithBall().equals(this);
    }

    public boolean isAttacking() {
        return gameMatch.getAttackingTeam() != null && gameMatch.getAttackingTeam().equals(this);
    }

    public boolean isDefending() {
        return gameMatch.getDefendingTeam() != null && gameMatch.getDefendingTeam().equals(this);
    }

    public GamePlayer getPassingTarget() {
        return passingTarget;
    }

    public void setPassingTarget(GamePlayer passingTarget) {
        this.passingTarget = passingTarget;
    }
}
