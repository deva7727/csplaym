package com.dev.csplaym.commands;

import com.dev.csplaym.Plugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor {

    private final Plugin plugin;

    public LeaveCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        if (!plugin.getGameManager().isPlayerInGame(player)) {
            player.sendMessage("§cYou are not in a game!");
            return true;
        }

        plugin.getGameManager().removePlayer(player);
        player.sendMessage("§aYou have left the game!");
        return true;
    }
}