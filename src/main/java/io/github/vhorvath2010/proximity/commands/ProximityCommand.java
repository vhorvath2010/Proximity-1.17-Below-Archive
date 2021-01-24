package io.github.vhorvath2010.proximity.commands;

import io.github.vhorvath2010.proximity.Proximity;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ProximityCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("Proximity")) {
            if (strings.length == 1 && strings[0].equalsIgnoreCase("reload")) {
                if (commandSender.hasPermission("proximity.reload")) {
                    Proximity.getPlugin().reloadConfig();
                    commandSender.sendMessage(ChatColor.GREEN + "Proximity reloaded!");
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Missing permissions!");
                }
            } else {
                commandSender.sendMessage(ChatColor.WHITE + "This server is running " + ChatColor.AQUA + "Proximity v" + Proximity.getPlugin().getDescription().getVersion());
            }
            return true;
        }
        return false;
    }
}
