package com.dev.csplaym.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class BattleMap {
    private final String name;
    private final Location center;
    private Location team1Spawn;
    private Location team2Spawn;

    public BattleMap(String name, Location center) {
        this.name = name;
        this.center = center;
    }

    public static BattleMap fromConfig(ConfigurationSection config) {
        String name = config.getName();
        Location center = locationFromString(config.getString("center"));
        BattleMap map = new BattleMap(name, center);

        map.setTeam1Spawn(locationFromString(config.getString("team1-spawn")));
        map.setTeam2Spawn(locationFromString(config.getString("team2-spawn")));

        return map;
    }

    private static Location locationFromString(String str) {
        if (str == null || str.isEmpty()) return null;

        String[] parts = str.split(",");
        if (parts.length != 6) return null;

        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    public String getName() {
        return name;
    }

    public Location getCenter() {
        return center;
    }

    public Location getTeam1Spawn() {
        return team1Spawn;
    }

    public void setTeam1Spawn(Location team1Spawn) {
        this.team1Spawn = team1Spawn;
    }

    public Location getTeam2Spawn() {
        return team2Spawn;
    }

    public void setTeam2Spawn(Location team2Spawn) {
        this.team2Spawn = team2Spawn;
    }

    public boolean isComplete() {
        return team1Spawn != null && team2Spawn != null;
    }
}