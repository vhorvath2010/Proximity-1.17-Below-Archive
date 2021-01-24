package io.github.vhorvath2010.proximity.events;

import io.github.vhorvath2010.proximity.Proximity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatEvents implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("proximity.bypass.speak")) {
            return;
        }
        // Clear recipients and add recipients in radius/with bypass
        Set<Player> recipients = event.getRecipients();
        recipients.clear();
        double radius = Proximity.getPlugin().getConfig().getDouble("chat_range");
        for (Entity near : getEntitiesAroundPoint(event.getPlayer().getLocation(), radius)) {
            if (near instanceof Player) {
                recipients.add((Player) near);
            }
        }
        // Add admins
        for (Player online: Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("proximity.bypass.listen") && recipients.contains(online)) {
                recipients.add(online);
            }
        }
    }

    // This method will get the entities within a sphere of radius r around a location
    private static List<Entity> getEntitiesAroundPoint(Location location, double radius) {
        List<Entity> entities = new ArrayList<Entity>();
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            if (entity.getLocation().distanceSquared(location) <= radius * radius) {
                entities.add(entity);
            }
        }
        return entities;
    }

}
