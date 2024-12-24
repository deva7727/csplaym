package com.dev.csplaym.weapons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;

public class Weapon {
    private final String name;
    private final Material material;
    private final int damage;
    private final int cooldown;
    private final WeaponType type;

    public Weapon(String name, Material material, int damage, int cooldown, WeaponType type) {
        this.name = name;
        this.material = material;
        this.damage = damage;
        this.cooldown = cooldown;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getDamage() {
        return damage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public WeaponType getType() {
        return type;
    }

    public ItemStack createItemStack() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6" + name);
            List<String> lore = new ArrayList<>();
            lore.add("§7Damage: §c" + damage);
            lore.add("§7Cooldown: §e" + (cooldown / 1000.0) + "s");
            lore.add("§7Type: §b" + type.getName());
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}