package io.github.vhorvath2010.proximity;

import io.github.vhorvath2010.proximity.events.ChatEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Proximity extends JavaPlugin {

    private static Proximity plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new ChatEvents(), this);
    }

    public static Proximity getPlugin() {
        return plugin;
    }
}
