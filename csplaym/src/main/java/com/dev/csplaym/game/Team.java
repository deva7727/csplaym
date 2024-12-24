package com.dev.csplaym.game;

import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private final String name;
    private final List<Player> players;
    private int score;

    public Team(String name) {
        this.name = name;
        this.players = new ArrayList<>();
        this.score = 0;
    }

    public Team(String name, List<Player> players) {
        this.name = name;
        this.players = new ArrayList<>(players);
        this.score = 0;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean containsPlayer(UUID playerId) {
        return players.stream().anyMatch(p -> p.getUniqueId().equals(playerId));
    }

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }
}