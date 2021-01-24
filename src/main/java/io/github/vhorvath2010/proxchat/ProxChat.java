package io.github.vhorvath2010.proxchat;

import org.bukkit.plugin.java.JavaPlugin;

public class ProxChat extends JavaPlugin {

    private static ProxChat plugin;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
    }

    public static ProxChat getPlugin() {
        return plugin;
    }
}
