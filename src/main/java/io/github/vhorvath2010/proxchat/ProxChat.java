package io.github.vhorvath2010.proxchat;

import io.github.vhorvath2010.proxchat.events.ChatEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ProxChat extends JavaPlugin {

    private static ProxChat plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
    }

    public static ProxChat getPlugin() {
        return plugin;
    }
}
