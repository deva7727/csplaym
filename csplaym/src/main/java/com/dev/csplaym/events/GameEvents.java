package com.dev.csplaym.events;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.game.Match;
import com.dev.csplaym.weapons.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameEvents implements Listener {
    private final Plugin plugin;

    public GameEvents(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        Match match = plugin.getGameManager().getPlayerMatch(damager);
        if (match == null) return;

        // Check if players are on the same team
        if (match.arePlayersOnSameTeam(damager, victim)) {
            event.setCancelled(true);
            return;
        }

        // Handle weapon damage
        ItemStack weaponItem = damager.getInventory().getItemInMainHand();
        Weapon weapon = plugin.getWeaponManager().getWeaponFromItem(weaponItem);
        if (weapon != null) {
            event.setDamage(weapon.getDamage());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Match match = plugin.getGameManager().getPlayerMatch(player);
        
        if (match != null) {
            event.getDrops().clear();
            match.handlePlayerDeath(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getGameManager().removePlayer(player);
    }
}