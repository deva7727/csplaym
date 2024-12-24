package com.dev.csplaym.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {
    private static final String PREFIX = "§8[§6BattleGrounds§8] ";

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(PREFIX + colorize(message));
    }

    public static void sendErrorMessage(Player player, String message) {
        player.sendMessage(PREFIX + "§c" + message);
    }

    public static void sendSuccessMessage(Player player, String message) {
        player.sendMessage(PREFIX + "§a" + message);
    }

    public static void broadcastMessage(String message) {
        org.bukkit.Bukkit.broadcastMessage(PREFIX + colorize(message));
    }
}