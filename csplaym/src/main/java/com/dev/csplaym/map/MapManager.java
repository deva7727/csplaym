package com.dev.csplaym.map;

import com.dev.csplaym.Plugin;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class MapManager {
    private final Plugin plugin;
    private final Map<String, BattleMap> maps;
    private String defaultMap;

    public MapManager(Plugin plugin) {
        this.plugin = plugin;
        this.maps = new HashMap<>();
        loadMaps();
    }

    private void loadMaps() {
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection mapsSection = config.getConfigurationSection("maps");
        
        if (mapsSection != null) {
            for (String mapName : mapsSection.getKeys(false)) {
                ConfigurationSection mapSection = mapsSection.getConfigurationSection(mapName);
                if (mapSection != null) {
                    BattleMap map = BattleMap.fromConfig(mapSection);
                    maps.put(mapName, map);
                }
            }
        }

        defaultMap = config.getString("settings.default-map", "");
    }

    public void createMap(String mapName, Location center) {
        if (maps.containsKey(mapName)) {
            return;
        }

        BattleMap map = new BattleMap(mapName, center);
        maps.put(mapName, map);
        saveMap(map);
    }

    public void setSpawnPoint(String mapName, String team, Location location) {
        BattleMap map = maps.get(mapName);
        if (map == null) return;

        if (team.equals("team1")) {
            map.setTeam1Spawn(location);
        } else if (team.equals("team2")) {
            map.setTeam2Spawn(location);
        }

        saveMap(map);
    }

    public boolean setDefaultMap(String mapName) {
        if (!maps.containsKey(mapName)) {
            return false;
        }

        defaultMap = mapName;
        plugin.getConfig().set("settings.default-map", mapName);
        plugin.saveConfig();
        return true;
    }

    private void saveMap(BattleMap map) {
        FileConfiguration config = plugin.getConfig();
        String path = "maps." + map.getName();

        config.set(path + ".center", locationToString(map.getCenter()));
        config.set(path + ".team1-spawn", locationToString(map.getTeam1Spawn()));
        config.set(path + ".team2-spawn", locationToString(map.getTeam2Spawn()));

        plugin.saveConfig();
    }

    private String locationToString(Location loc) {
        if (loc == null) return "";
        return String.format("%s,%d,%d,%d,%f,%f",
                loc.getWorld().getName(),
                loc.getBlockX(),
                loc.getBlockY(),
                loc.getBlockZ(),
                loc.getYaw(),
                loc.getPitch());
    }

    public BattleMap getMap(String mapName) {
        return maps.get(mapName);
    }

    public BattleMap getDefaultMap() {
        return maps.get(defaultMap);
    }
}