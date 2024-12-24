package com.dev.csplaym.events;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.utils.MessageUtils;
import com.dev.csplaym.weapons.Weapon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WeaponSelectionHandler implements Listener {
    private final Plugin plugin;

    public WeaponSelectionHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("§8Weapon Selection")) {
            return;
        }

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }

        Weapon weapon = plugin.getWeaponManager().getWeaponFromItem(event.getCurrentItem());
        if (weapon == null) {
            return;
        }

        if (!plugin.getGameManager().canPlayerSelectWeapon(player, weapon)) {
            MessageUtils.sendErrorMessage(player, "You cannot select weapons at this time!");
            return;
        }

        plugin.getWeaponManager().giveWeaponToPlayer(player, weapon);
        MessageUtils.sendSuccessMessage(player, "Selected weapon: " + weapon.getName());
        player.closeInventory();
    }
}