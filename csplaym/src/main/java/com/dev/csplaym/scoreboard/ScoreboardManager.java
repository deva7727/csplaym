package com.dev.csplaym.scoreboard;

import com.dev.csplaym.game.Match;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {
    private final Map<UUID, Scoreboard> playerScoreboards;

    public ScoreboardManager() {
        this.playerScoreboards = new HashMap<>();
    }

    public void createMatchScoreboard(Match match) {
        for (Player player : match.getPlayers()) {
            org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (manager == null) return;

            Scoreboard board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective("match", "dummy", "§6§lBattleGrounds");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Set scores
            int score = 15;
            objective.getScore("§r").setScore(score--);
            objective.getScore("§fMode: §e" + match.getGameMode().getDisplayName()).setScore(score--);
            objective.getScore("§r§r").setScore(score--);
            objective.getScore("§fMap: §e" + match.getMap().getName()).setScore(score--);
            objective.getScore("§r§r§r").setScore(score--);
            objective.getScore("§fTeam 1: §a" + match.getTeam1Score()).setScore(score--);
            objective.getScore("§fTeam 2: §c" + match.getTeam2Score()).setScore(score--);
            objective.getScore("§r§r§r§r").setScore(score--);
            objective.getScore("§fRound: §e" + match.getCurrentRound() + "/5").setScore(score);

            player.setScoreboard(board);
            playerScoreboards.put(player.getUniqueId(), board);
        }
    }

    public void updateScores(Match match) {
        for (Player player : match.getPlayers()) {
            Scoreboard board = playerScoreboards.get(player.getUniqueId());
            if (board == null) continue;

            Objective objective = board.getObjective("match");
            if (objective == null) continue;

            objective.getScore("§fTeam 1: §a" + match.getTeam1Score()).setScore(10);
            objective.getScore("§fTeam 2: §c" + match.getTeam2Score()).setScore(9);
            objective.getScore("§fRound: §e" + match.getCurrentRound() + "/5").setScore(7);
        }
    }

    public void removeScoreboard(Player player) {
        playerScoreboards.remove(player.getUniqueId());
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            player.setScoreboard(manager.getNewScoreboard());
        }
    }
}