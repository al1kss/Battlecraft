package com.al1kss.battlecraft.listeners;

import com.al1kss.battlecraft.Battlecraft;
import com.al1kss.battlecraft.events.Events;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.List;
import java.util.Objects;

import static com.al1kss.battlecraft.events.Events.createStand;


public class Listeners implements Listener {
    private Plugin Listeners;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        invCheck(player.getInventory());

    }

    @EventHandler
    public void onDroppedItem(PlayerDropItemEvent event) {

    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        invCheck(player.getInventory());
    }


    public void invCheck(PlayerInventory inventory){
        Player player = (Player) inventory.getHolder();
        World world = player.getWorld();
        Material red = Material.RED_BANNER;
        Material blue = Material.BLUE_BANNER;

        // Check for existing armor stands holding red or blue banners
        boolean hasRedBannerStand = false;
        boolean hasBlueBannerStand = false;

        for (Entity entity : world.getEntities()) {
            if (entity instanceof ArmorStand) {
                ArmorStand armorStand = (ArmorStand) entity;
                ItemStack helmet = armorStand.getEquipment().getHelmet();

                if (helmet != null && helmet.getType() == red) {
                    hasRedBannerStand = true;
                } else if (helmet != null && helmet.getType() == blue) {
                    hasBlueBannerStand = true;
                }

                if (hasRedBannerStand && hasBlueBannerStand) {
                    break;
                }
            }
        }

        if (inventory.contains(red) && !hasRedBannerStand) {
            createStand(player, red);
        } else if (inventory.contains(blue) && !hasBlueBannerStand) {
            createStand(player, blue);
        }
    }
}
