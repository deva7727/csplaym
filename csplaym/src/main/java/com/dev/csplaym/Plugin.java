package com.dev.csplaym;

import com.dev.csplaym.commands.AdminCommands;
import com.dev.csplaym.commands.LeaveCommand;
import com.dev.csplaym.commands.PlayCommand;
import com.dev.csplaym.events.GameEvents;
import com.dev.csplaym.events.PlayerEvents;
import com.dev.csplaym.events.WeaponSelectionHandler;
import com.dev.csplaym.game.GameManager;
import com.dev.csplaym.map.MapManager;
import com.dev.csplaym.scoreboard.ScoreboardManager;
import com.dev.csplaym.utils.ConfigManager;
import com.dev.csplaym.weapons.WeaponManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    
    private static Plugin instance;
    private GameManager gameManager;
    private ConfigManager configManager;
    private MapManager mapManager;
    private WeaponManager weaponManager;
    private ScoreboardManager scoreboardManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.mapManager = new MapManager(this);
        this.weaponManager = new WeaponManager(this);
        this.gameManager = new GameManager(this);
        this.scoreboardManager = new ScoreboardManager();
        
        // Register commands
        getCommand("play").setExecutor(new PlayCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("bg").setExecutor(new AdminCommands(this));
        
        // Register events
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new WeaponSelectionHandler(this), this);
        getServer().getPluginManager().registerEvents(new GameEvents(this), this);
        
        // Load configurations
        saveDefaultConfig();
        configManager.saveDefaultConfig();
        
        getLogger().info("BattleGrounds has been enabled!");
    }

    @Override
    public void onDisable() {
        if (gameManager != null) {
            gameManager.shutdownAllGames();
        }
        getLogger().info("BattleGrounds has been disabled!");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}