package com.dev.csplaym.game;

// Created by: deva7727
// Last Updated: 2024-12-24 05:38:38 UTC

import com.dev.csplaym.Plugin;
import com.dev.csplaym.game.Match.MatchState;
import com.dev.csplaym.map.BattleMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Match {
    private final UUID matchId;
    private final Plugin plugin;
    private final GameMode gameMode;
    private final List<Team> teams;
    private final Map<UUID, Player> players;
    private final BattleMap map;
    private MatchState state;
    private int currentRound;
    private int roundTime;
    private BukkitRunnable roundTimer;

    public Match(Plugin plugin, GameMode gameMode, List<Player> playerList, BattleMap map) {
        this.matchId = UUID.randomUUID();
        this.plugin = plugin;
        this.gameMode = gameMode;
        this.players = new HashMap<>();
        this.teams = new ArrayList<>();
        this.map = map;
        this.state = MatchState.WAITING;
        this.currentRound = 1;
        this.roundTime = 180; // 3 minutes per round
        
        initializeTeams(playerList);
    }

    private void initializeTeams(List<Player> playerList) {
        List<Player> team1Players = new ArrayList<>();
        List<Player> team2Players = new ArrayList<>();
        
        // Distribute players evenly
        for (int i = 0; i < playerList.size(); i++) {
            if (i % 2 == 0) {
                team1Players.add(playerList.get(i));
            } else {
                team2Players.add(playerList.get(i));
            }
        }

        teams.add(new Team("Team 1", team1Players));
        teams.add(new Team("Team 2", team2Players));

        // Store all players in the players map
        for (Player player : playerList) {
            players.put(player.getUniqueId(), player);
        }
    }

    public void start() {
        System.out.println("Attempting to start match..."); // Debug message
        
        if (state != MatchState.WAITING) {
            System.out.println("Match not in WAITING state: " + state); // Debug message
            return;
        }

        System.out.println("Starting match..."); // Debug message
        state = MatchState.IN_PROGRESS;
        teleportPlayersToSpawns();
        giveWeaponsToPlayers();
        startRoundTimer();
    }

    private void teleportPlayersToSpawns() {
        for (Player player : getPlayers()) {
            Location spawn = getSpawnLocation(player);
            if (spawn != null) {
                System.out.println("Teleporting " + player.getName() + " to spawn"); // Debug message
                player.teleport(spawn);
            } else {
                System.out.println("WARNING: No spawn location found for " + player.getName()); // Debug message
            }
        }
    }

    private void giveWeaponsToPlayers() {
        for (Player player : getPlayers()) {
            System.out.println("Giving weapons to " + player.getName()); // Debug message
            plugin.getWeaponManager().giveDefaultWeapons(player);
        }
    }

    private void startRoundTimer() {
        if (roundTimer != null) {
            roundTimer.cancel();
        }

        roundTimer = new BukkitRunnable() {
            @Override
            public void run() {
                roundTime--;
                if (roundTime <= 0) {
                    System.out.println("Round time expired"); // Debug message
                    endRound();
                }
            }
        };
        System.out.println("Starting round timer"); // Debug message
        roundTimer.runTaskTimer(plugin, 20L, 20L); // Run every second
    }

    public void endRound() {
        if (roundTimer != null) {
            roundTimer.cancel();
        }

        System.out.println("Ending round " + currentRound); // Debug message
        currentRound++;
        if (currentRound > 5 || shouldEndMatch()) {
            end();
        } else {
            resetRound();
        }
    }

    public void end() {
        if (roundTimer != null) {
            roundTimer.cancel();
        }

        state = MatchState.ENDED;
        
        // Determine winner
        Team winner = teams.get(0).getScore() > teams.get(1).getScore() ? teams.get(0) : teams.get(1);
        
        System.out.println("Match ended. Winner: " + winner.getName()); // Debug message
        
        // Announce winner
        for (Player player : getPlayers()) {
            player.sendTitle("§6Game Over!", "§eWinner: " + winner.getName(), 10, 70, 20);
            player.teleport(player.getWorld().getSpawnLocation());
            plugin.getWeaponManager().removeWeapons(player);
        }
    }

    private void resetRound() {
        roundTime = 180;
        System.out.println("Resetting round"); // Debug message
        teleportPlayersToSpawns();
        giveWeaponsToPlayers();
        startRoundTimer();
    }

    public boolean hasPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        System.out.println("Removing player: " + player.getName()); // Debug message
        players.remove(player.getUniqueId());
        for (Team team : teams) {
            if (team.containsPlayer(player.getUniqueId())) {
                team.removePlayer(player);
                break;
            }
        }

        if (shouldEndMatch()) {
            System.out.println("Not enough players, ending match"); // Debug message
            end();
        }
    }

    public void handlePlayerDeath(Player player) {
        System.out.println("Player died: " + player.getName()); // Debug message
        
        // Find opponent's team and increment their score
        Team opponentTeam = null;
        for (Team team : teams) {
            if (!team.containsPlayer(player.getUniqueId())) {
                opponentTeam = team;
                break;
            }
        }

        if (opponentTeam != null) {
            opponentTeam.incrementScore();
            plugin.getScoreboardManager().updateScores(this);
        }

        // Respawn player
        new BukkitRunnable() {
            @Override
            public void run() {
                handlePlayerRespawn(player);
            }
        }.runTaskLater(plugin, 60L); // 3-second respawn delay
    }

    public void handlePlayerQuit(Player player) {
        System.out.println("Player quit: " + player.getName()); // Debug message
        removePlayer(player);
    }

    public void handlePlayerRespawn(Player player) {
        System.out.println("Respawning player: " + player.getName()); // Debug message
        player.teleport(getSpawnLocation(player));
        plugin.getWeaponManager().giveDefaultWeapons(player);
    }

    public boolean arePlayersOnSameTeam(Player player1, Player player2) {
        for (Team team : teams) {
            if (team.containsPlayer(player1.getUniqueId()) && 
                team.containsPlayer(player2.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    private Location getSpawnLocation(Player player) {
        for (Team team : teams) {
            if (team.containsPlayer(player.getUniqueId())) {
                return team == teams.get(0) ? map.getTeam1Spawn() : map.getTeam2Spawn();
            }
        }
        return player.getWorld().getSpawnLocation();
    }

    private boolean shouldEndMatch() {
        return players.size() < 2 || currentRound > 5 || 
               Math.abs(teams.get(0).getScore() - teams.get(1).getScore()) > (5 - currentRound);
    }

    // Getters
    public List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public BattleMap getMap() {
        return map;
    }

    public int getTeam1Score() {
        return teams.get(0).getScore();
    }

    public int getTeam2Score() {
        return teams.get(1).getScore();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public MatchState getState() {
        return state;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public int getRoundTime() {
        return roundTime;
    }

    public enum MatchState {
        WAITING,
        PREPARING,
        IN_PROGRESS,
        ENDED
    }
}