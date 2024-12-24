package com.dev.csplaym.game;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.map.BattleMap;
import com.dev.csplaym.player.BattlePlayer;
import com.dev.csplaym.weapons.Weapon;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {
    private final Plugin plugin;
    private final Map<UUID, Match> activeMatches;
    private final Map<UUID, BattlePlayer> activePlayers;

    public GameManager(Plugin plugin) {
        this.plugin = plugin;
        this.activeMatches = new HashMap<>();
        this.activePlayers = new HashMap<>();
    }

    public Match getPlayerMatch(Player player) {
        for (Match match : activeMatches.values()) {
            if (match.hasPlayer(player)) {
                return match;
            }
        }
        return null;
    }

    public boolean isPlayerInGame(Player player) {
        return getPlayerMatch(player) != null;
    }

    public void removePlayer(Player player) {
        Match match = getPlayerMatch(player);
        if (match != null) {
            match.removePlayer(player);
        }
    }

    public boolean canPlayerSelectWeapon(Player player, Weapon weapon) {
        Match match = getPlayerMatch(player);
        if (match == null) return false;
        return match.getState() == Match.MatchState.WAITING || 
               match.getState() == Match.MatchState.PREPARING;
    }

    public void shutdownAllGames() {
        for (Match match : new ArrayList<>(activeMatches.values())) {
            match.end();
        }
        activeMatches.clear();
        activePlayers.clear();
    }

    public void createMatch(GameMode gameMode, List<Player> players, BattleMap map) {
        Match match = new Match(plugin, gameMode, players, map);
        activeMatches.put(match.getMatchId(), match);
    }

    public void removeMatch(UUID matchId) {
        activeMatches.remove(matchId);
    }

    public Collection<Match> getActiveMatches() {
        return activeMatches.values();
    }
}