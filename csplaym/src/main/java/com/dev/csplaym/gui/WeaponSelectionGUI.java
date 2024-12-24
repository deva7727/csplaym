package com.dev.csplaym.gui;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.weapons.Weapon;
import com.dev.csplaym.weapons.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class WeaponSelectionGUI {
    private final Plugin plugin;
    private final Inventory gui;
    private final Player player;

    public WeaponSelectionGUI(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.gui = Bukkit.createInventory(null, 54, "§8Weapon Selection");
        initializeItems();
    }

    private void initializeItems() {
        // Add category separators
        addCategorySeparator(0, "§e§lPistols", Material.WOODEN_SWORD);
        addCategorySeparator(9, "§e§lRifles", Material.DIAMOND_SWORD);
        addCategorySeparator(18, "§e§lSnipers", Material.BOW);
        addCategorySeparator(27, "§e§lShotguns", Material.STONE_SWORD);

        // Add weapons by category
        int pistolSlot = 1;
        int rifleSlot = 10;
        int sniperSlot = 19;
        int shotgunSlot = 28;

        // Add all weapons to their respective categories
        for (Weapon weapon : plugin.getWeaponManager().getAllWeapons().values()) {
            ItemStack item = createWeaponItem(weapon);
            
            switch (weapon.getType()) {
                case PISTOL:
                    gui.setItem(pistolSlot++, item);
                    break;
                case RIFLE:
                    gui.setItem(rifleSlot++, item);
                    break;
                case SNIPER:
                    gui.setItem(sniperSlot++, item);
                    break;
                case SHOTGUN:
                    gui.setItem(shotgunSlot++, item);
                    break;
            }
        }

        // Add close button
        ItemStack closeButton = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeButton.getItemMeta();
        if (closeMeta != null) {
            closeMeta.setDisplayName("§c§lClose Menu");
            closeButton.setItemMeta(closeMeta);
        }
        gui.setItem(49, closeButton);
    }

    private void addCategorySeparator(int slot, String name, Material icon) {
        ItemStack separator = new ItemStack(icon);
        ItemMeta meta = separator.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> lore = new ArrayList<>();
            lore.add("§7Click on a weapon to select it");
            meta.setLore(lore);
            separator.setItemMeta(meta);
        }
        gui.setItem(slot, separator);
    }

    private ItemStack createWeaponItem(Weapon weapon) {
        ItemStack item = new ItemStack(weapon.getMaterial());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6" + weapon.getName());
            
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c" + weapon.getDamage());
            lore.add("§7Cooldown: §e" + (weapon.getCooldown() / 1000.0) + "s");
            lore.add("§7Type: §b" + weapon.getType().getName());
            lore.add("");
            lore.add("§eClick to select!");
            
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void openMenu() {
        player.openInventory(gui);
    }
}