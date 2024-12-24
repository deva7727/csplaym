package com.dev.csplaym.events;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.game.Match;
import com.dev.csplaym.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerEvents implements Listener {
    private final Plugin plugin;

    public PlayerEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load player stats if they exist
        plugin.getConfigManager().loadPlayerStats(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Match match = plugin.getGameManager().getPlayerMatch(player);
        
        if (match != null) {
            match.handlePlayerQuit(player);
        }
        
        // Save player stats
        plugin.getConfigManager().savePlayerStats(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Match match = plugin.getGameManager().getPlayerMatch(player);
        
        if (match != null) {
            match.handlePlayerRespawn(player);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGameManager().isPlayerInGame(player)) {
            event.setCancelled(true);
            MessageUtils.sendErrorMessage(player, "You cannot drop items during a match!");
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getGameManager().isPlayerInGame(player)) {
                event.setCancelled(true);
                player.setFoodLevel(20);
            }
        }
    }
}