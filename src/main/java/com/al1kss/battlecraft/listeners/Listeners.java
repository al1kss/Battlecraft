package com.al1kss.battlecraft.listeners;

import com.al1kss.battlecraft.Battlecraft;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.List;
import java.util.Objects;


public class Listeners implements Listener {
    private Plugin Listeners;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {


        final Player player = event.getPlayer();

    }

    @EventHandler
    public void onDroppedItem(PlayerDropItemEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Objects.requireNonNull(Bukkit.getWorld("world")).getEntities()) {
                    if (entity instanceof Item && entity.getCustomName() != null && entity.getCustomName().equals("Blue Banner")) {
                        entity.setGlowing(true);
                    }
                }
            }
        }.runTaskTimer(Battlecraft.instance, 0L, 20L);
    }
}
