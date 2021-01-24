package io.github.vhorvath2010.proxchat.events;

import io.github.vhorvath2010.proxchat.ProxChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
        double radius = ProxChat.getPlugin().getConfig().getDouble("chat_range");
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
        World world = location.getWorld();

        // To find chunks we use chunk coordinates (not block coordinates!)
        int smallX = (int) Math.floor((location.getX() - radius) / 16.0D);
        int bigX = (int) Math.floor((location.getX() + radius) / 16.0D);
        int smallZ = (int) Math.floor((location.getZ() - radius) / 16.0D);
        int bigZ = (int) Math.floor((location.getZ() + radius) / 16.0D);

        for (int x = smallX; x <= bigX; x++) {
            for (int z = smallZ; z <= bigZ; z++) {
                if (world.isChunkLoaded(x, z)) {
                    entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities())); // Add all entities from this chunk to the list
                }
            }
        }

        // Remove the entities that are within the box above but not actually in the sphere we defined with the radius and location
        // This code below could probably be replaced in Java 8 with a stream -> filter
        Iterator<Entity> entityIterator = entities.iterator(); // Create an iterator so we can loop through the list while removing entries
        while (entityIterator.hasNext()) {
            if (entityIterator.next().getLocation().distanceSquared(location) > radius * radius) { // If the entity is outside of the sphere...
                entityIterator.remove(); // Remove it
            }
        }
        return entities;
    }

}
