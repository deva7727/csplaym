package com.dev.csplaym.player;

import org.bukkit.entity.Player;
import java.util.UUID;

public class BattlePlayer {
    private final UUID uuid;
    private final Player player;
    private int kills;
    private int deaths;
    private PlayerStats stats;

    public BattlePlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.kills = 0;
        this.deaths = 0;
        this.stats = new PlayerStats(uuid);
    }

    public void addKill() {
        kills++;
        stats.addKill();
    }

    public void addDeath() {
        deaths++;
        stats.addDeath();
    }

    public void resetMatchStats() {
        kills = 0;
        deaths = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public UUID getUuid() {
        return uuid;
    }
}