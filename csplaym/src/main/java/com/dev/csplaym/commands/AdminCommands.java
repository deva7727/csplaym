package com.dev.csplaym.commands;

import com.dev.csplaym.Plugin;
import com.dev.csplaym.map.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommands implements CommandExecutor {

    private final Plugin plugin;

    public AdminCommands(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("battlegrounds.admin")) {
            sender.sendMessage("§cYou don't have permission to use this command!");
            return true;
        }

        if (args.length < 1) {
            sendHelpMessage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                return handleCreate(sender, args);
            case "setspawn":
                return handleSetSpawn(sender, args);
            case "setmap":
                return handleSetMap(sender, args);
            case "forcestart":
                return handleForceStart(sender, args);
            default:
                sendHelpMessage(sender);
                return true;
        }
    }

    private boolean handleCreate(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can create maps!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /bg create <mapName>");
            return true;
        }

        Player player = (Player) sender;
        String mapName = args[1];
        plugin.getMapManager().createMap(mapName, player.getLocation());
        player.sendMessage("§aMap '" + mapName + "' has been created!");
        return true;
    }

    private boolean handleSetSpawn(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can set spawn points!");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /bg setspawn <mapName> <team1/team2>");
            return true;
        }

        Player player = (Player) sender;
        String mapName = args[1];
        String team = args[2].toLowerCase();

        if (!team.equals("team1") && !team.equals("team2")) {
            player.sendMessage("§cInvalid team! Use 'team1' or 'team2'");
            return true;
        }

        plugin.getMapManager().setSpawnPoint(mapName, team, player.getLocation());
        player.sendMessage("§aSpawn point for " + team + " has been set on map '" + mapName + "'!");
        return true;
    }

    private boolean handleSetMap(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /bg setmap <mapName>");
            return true;
        }

        String mapName = args[1];
        if (plugin.getMapManager().setDefaultMap(mapName)) {
            sender.sendMessage("§aDefault map has been set to '" + mapName + "'!");
        } else {
            sender.sendMessage("§cMap '" + mapName + "' does not exist!");
        }
        return true;
    }

    private boolean handleForceStart(CommandSender sender, String[] args) {
        // Implementation for force starting a match
        sender.sendMessage("§cForce start feature is not implemented yet!");
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§6=== BattleGrounds Admin Commands ===");
        sender.sendMessage("§e/bg create <mapName> §7- Create a new map");
        sender.sendMessage("§e/bg setspawn <mapName> <team1/team2> §7- Set team spawn points");
        sender.sendMessage("§e/bg setmap <mapName> §7- Set the default map");
        sender.sendMessage("§e/bg forcestart §7- Force start a match");
    }
}