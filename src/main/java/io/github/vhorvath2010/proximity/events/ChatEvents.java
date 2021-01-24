package io.github.vhorvath2010.proximity.events;

import io.github.vhorvath2010.proximity.Proximity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

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
        double muffleMag = Proximity.getPlugin().getConfig().getDouble("block_muffling");
        for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
            // Account for block muffling
            Vector from = location.toVector();
            Vector to = entity.getLocation().toVector();
            Vector dir = to.subtract(from);
            BlockIterator iterator = new BlockIterator(location.getWorld(), from, dir, 1.0, (int) Math.ceil(entity.getLocation().distance(location)));
            double muffle = 0;
            while (iterator.hasNext()) {
                Block block = iterator.next();
                if (!block.isPassable()) {
                    muffle += muffleMag;
                }
            }
            // Apply block muffling to the radius
            double effectiveRadius = radius - muffle;
            if (entity.getLocation().distance(location) <= effectiveRadius) {
                entities.add(entity);
                System.out.println("A " + entity.getType() + " is in range");
            }
        }
        return entities;
    }

}
