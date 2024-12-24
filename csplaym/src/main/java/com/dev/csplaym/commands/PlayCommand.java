package com.dev.csplaym.commands;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.game.GameMode;
import com.dev.csplaym.map.BattleMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import java.util.*;

public class PlayCommand implements CommandExecutor {
    private final Plugin plugin;
    private final Map<String, List<Player>> matchmaking = new HashMap<>(); // Key: "1v1", "2v2", "4v4"
    private final Map<Player, BukkitRunnable> matchmakingTimers = new HashMap<>();

    public PlayCommand(Plugin plugin) {
        this.plugin = plugin;
        // Initialize matchmaking queues
        matchmaking.put("1v1", new ArrayList<>());
        matchmaking.put("2v2", new ArrayList<>());
        matchmaking.put("4v4", new ArrayList<>());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (plugin.getGameManager().isPlayerInGame(player)) {
            player.sendMessage("§cYou are already in a game!");
            return true;
        }

        if (args.length > 0 && (args[0].equals("1v1") || args[0].equals("2v2") || args[0].equals("4v4"))) {
            joinMatchmaking(player, args[0]);
        } else {
            sendMatchmakingOptions(player);
        }

        return true;
    }

    private void sendMatchmakingOptions(Player player) {
        player.sendMessage("§6§l=== Select Match Type ===");

        // Create clickable options
        TextComponent v1 = new TextComponent("§a[1v1] ");
        v1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play 1v1"));
        v1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder("§7Click to join 1v1 matchmaking").create()));

        TextComponent v2 = new TextComponent("§e[2v2] ");
        v2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play 2v2"));
        v2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder("§7Click to join 2v2 matchmaking").create()));

        TextComponent v4 = new TextComponent("§c[4v4]");
        v4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play 4v4"));
        v4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder("§7Click to join 4v4 matchmaking").create()));

        player.spigot().sendMessage(v1, v2, v4);
    }

    private void joinMatchmaking(Player player, String mode) {
        // Remove from other queues if present
        for (List<Player> queue : matchmaking.values()) {
            queue.remove(player);
        }

        // Cancel existing timer if any
        if (matchmakingTimers.containsKey(player)) {
            matchmakingTimers.get(player).cancel();
            matchmakingTimers.remove(player);
        }

        List<Player> queue = matchmaking.get(mode);
        queue.add(player);

        // Send message to player
        player.sendMessage("§aJoined " + mode + " matchmaking queue! Waiting for opponents...");
        player.sendTitle("§6Matchmaking", "§7Waiting for opponents...", 10, 70, 20);

        // Start 20-second timer
        BukkitRunnable timer = new BukkitRunnable() {
            int timeLeft = 20;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    // Time's up - no match found
                    queue.remove(player);
                    matchmakingTimers.remove(player);
                    player.sendMessage("§cNo opponents found! Matchmaking cancelled.");
                    player.sendTitle("§cMatchmaking Failed", "§7No opponents found", 10, 70, 20);
                    this.cancel();
                    return;
                }

                // Check if we have enough players
                int requiredPlayers = getRequiredPlayers(mode);
                if (queue.size() >= requiredPlayers) {
                    // We have enough players! Start the match
                    startMatch(mode, queue);
                    this.cancel();
                    return;
                }

                // Update actionbar with time remaining
                if (timeLeft <= 5) {
                    player.sendTitle("", "§c" + timeLeft + "s", 0, 20, 0);
                }
                
                timeLeft--;
            }
        };

        timer.runTaskTimer(plugin, 0L, 20L);
        matchmakingTimers.put(player, timer);
    }

    private int getRequiredPlayers(String mode) {
        switch (mode) {
            case "1v1": return 2;
            case "2v2": return 4;
            case "4v4": return 8;
            default: return 2;
        }
    }

    private void startMatch(String mode, List<Player> players) {
        // Get required number of players
        int required = getRequiredPlayers(mode);
        List<Player> matchPlayers = new ArrayList<>(players.subList(0, required));
        
        // Remove these players from queue
        for (Player p : matchPlayers) {
            if (matchmakingTimers.containsKey(p)) {
                matchmakingTimers.get(p).cancel();
                matchmakingTimers.remove(p);
            }
            players.remove(p);
        }

        // Get a map
        String defaultMapName = plugin.getConfig().getString("settings.default-map", "map1");
        BattleMap map = plugin.getMapManager().getMap(defaultMapName);

        if (map == null || !map.isComplete()) {
            for (Player p : matchPlayers) {
                p.sendMessage("§cError: No valid map available!");
            }
            return;
        }

        // Create the match
        try {
            plugin.getGameManager().createMatch(GameMode.TEAMBATTLE, matchPlayers, map);
            
            // Notify players
            for (Player p : matchPlayers) {
                p.sendMessage("§a§lMatch Found!");
                p.sendTitle("§6Match Starting!", "§e" + mode + " Battle", 10, 70, 20);
            }
        } catch (Exception e) {
            for (Player p : matchPlayers) {
                p.sendMessage("§cError creating match: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }
}