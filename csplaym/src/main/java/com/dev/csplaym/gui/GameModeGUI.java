package com.dev.csplaym.gui;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.game.GameMode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GameModeGUI {

    private final Plugin plugin;
    private final Inventory gui;

    public GameModeGUI(Plugin plugin) {
        this.plugin = plugin;
        this.gui = Bukkit.createInventory(null, 27, "§6Select Game Mode");
        initializeItems();
    }

    private void initializeItems() {
        // 1v1 Mode
        ItemStack oneVOne = createItem(Material.IRON_SWORD, "§e1v1 Mode",
                "§7Click to join 1v1 queue", "§7Players: 2");
        
        // 2v2 Mode
        ItemStack twoVTwo = createItem(Material.GOLDEN_SWORD, "§e2v2 Mode",
                "§7Click to join 2v2 queue", "§7Players: 4");
        
        // 4v4 Mode
        ItemStack fourVFour = createItem(Material.DIAMOND_SWORD, "§e4v4 Mode",
                "§7Click to join 4v4 queue", "§7Players: 8");

        gui.setItem(11, oneVOne);
        gui.setItem(13, twoVTwo);
        gui.setItem(15, fourVFour);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void openMenu(Player player) {
        player.openInventory(gui);
    }
}