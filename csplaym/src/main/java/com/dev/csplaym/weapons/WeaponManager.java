package com.dev.csplaym.weapons;

import com.dev.csplaym.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class WeaponManager {
    private final Plugin plugin;
    private final Map<String, Weapon> weapons;
    private final Map<Material, Weapon> weaponsByMaterial;

    public WeaponManager(Plugin plugin) {
        this.plugin = plugin;
        this.weapons = new HashMap<>();
        this.weaponsByMaterial = new HashMap<>();
        initializeWeapons();
    }

    private void initializeWeapons() {
        // Initialize default weapons
        registerWeapon(new Weapon("Glock", Material.WOODEN_SWORD, 4, 500, WeaponType.PISTOL));
        registerWeapon(new Weapon("Desert Eagle", Material.IRON_SWORD, 7, 700, WeaponType.PISTOL));
        
        registerWeapon(new Weapon("AK-47", Material.DIAMOND_SWORD, 6, 300, WeaponType.RIFLE));
        registerWeapon(new Weapon("M4A4", Material.GOLDEN_SWORD, 5, 250, WeaponType.RIFLE));
        
        registerWeapon(new Weapon("AWP", Material.BOW, 10, 1500, WeaponType.SNIPER));
        registerWeapon(new Weapon("Scout", Material.CROSSBOW, 8, 1000, WeaponType.SNIPER));
        
        registerWeapon(new Weapon("Nova", Material.STONE_SWORD, 8, 800, WeaponType.SHOTGUN));
    }

    public void registerWeapon(Weapon weapon) {
        weapons.put(weapon.getName().toLowerCase(), weapon);
        weaponsByMaterial.put(weapon.getMaterial(), weapon);
    }

    public Weapon getWeaponByName(String name) {
        return weapons.get(name.toLowerCase());
    }

    public Weapon getWeaponFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return null;
        
        String name = meta.getDisplayName().replace("§6", "");
        return getWeaponByName(name);
    }

    public boolean isWeapon(ItemStack item) {
        return getWeaponFromItem(item) != null;
    }

    public void giveWeaponToPlayer(Player player, Weapon weapon) {
        ItemStack weaponItem = weapon.createItemStack();
        player.getInventory().addItem(weaponItem);
    }

    public void giveDefaultWeapons(Player player) {
        // Give basic pistol to everyone
        Weapon defaultPistol = getWeaponByName("Glock");
        if (defaultPistol != null) {
            giveWeaponToPlayer(player, defaultPistol);
        }
    }

    public void removeWeapons(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isWeapon(item)) {
                player.getInventory().remove(item);
            }
        }
    }

    public Map<String, Weapon> getAllWeapons() {
        return new HashMap<>(weapons);
    }

    public Map<String, Weapon> getWeaponsByType(WeaponType type) {
        Map<String, Weapon> filteredWeapons = new HashMap<>();
        for (Map.Entry<String, Weapon> entry : weapons.entrySet()) {
            if (entry.getValue().getType() == type) {
                filteredWeapons.put(entry.getKey(), entry.getValue());
            }
        }
        return filteredWeapons;
    }
}