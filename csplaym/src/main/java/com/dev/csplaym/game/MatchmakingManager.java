package com.dev.csplaym.game;

import org.bukkit.entity.Player;
import java.util.*;

public class MatchmakingManager {
    private final Map<String, Queue<Player>> queues;
    private final Map<Player, String> playerQueues;

    public MatchmakingManager() {
        this.queues = new HashMap<>();
        this.playerQueues = new HashMap<>();
        
        // Initialize queues
        queues.put("1v1", new LinkedList<>());
        queues.put("2v2", new LinkedList<>());
        queues.put("4v4", new LinkedList<>());
    }

    public void addToQueue(Player player, String queueType) {
        removeFromAllQueues(player);
        queues.get(queueType).add(player);
        playerQueues.put(player, queueType);
    }

    public void removeFromAllQueues(Player player) {
        String currentQueue = playerQueues.remove(player);
        if (currentQueue != null) {
            queues.get(currentQueue).remove(player);
        }
    }

    public int getQueueSize(String queueType) {
        return queues.get(queueType).size();
    }

    public boolean isInQueue(Player player) {
        return playerQueues.containsKey(player);
    }

    public String getPlayerQueue(Player player) {
        return playerQueues.get(player);
    }
}