package com.dev.csplaym.player;

import java.util.UUID;

public class PlayerStats {
    private final UUID playerUUID;
    private int totalKills;
    private int totalDeaths;
    private int gamesPlayed;
    private int gamesWon;

    public PlayerStats(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.totalKills = 0;
        this.totalDeaths = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }

    public void addKill() {
        totalKills++;
    }

    public void addDeath() {
        totalDeaths++;
    }

    public void addGamePlayed() {
        gamesPlayed++;
    }

    public void addGameWon() {
        gamesWon++;
    }

    public double getKDRatio() {
        return totalDeaths == 0 ? totalKills : (double) totalKills / totalDeaths;
    }

    public double getWinRatio() {
        return gamesPlayed == 0 ? 0 : (double) gamesWon / gamesPlayed;
    }

    // Getters
    public int getTotalKills() {
        return totalKills;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}