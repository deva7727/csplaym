package com.dev.csplaym.utils;

import com.dev.csplaym.Plugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ConfigManager {
    private final Plugin plugin;
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration statsConfig;
    private File statsFile;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        setupFiles();
    }

    private void setupFiles() {
        // Setup main config
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");
        statsFile = new File(plugin.getDataFolder(), "stats.yml");

        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create stats.yml!");
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
    }

    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public void loadPlayerStats(UUID playerId) {
        String path = "players." + playerId;
        if (statsConfig.contains(path)) {
            // Load player stats from config
            // Implementation depends on what stats you want to track
        }
    }

    public void savePlayerStats(UUID playerId) {
        try {
            statsConfig.save(statsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save stats for player " + playerId);
        }
    }
}