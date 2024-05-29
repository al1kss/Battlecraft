package com.al1kss.battlecraft.events;

import com.al1kss.battlecraft.Battlecraft;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Events {
    public static void playerGlow(Player sender){

    }

    public static void createStand(Player player, Material banners){
        World world = player.getWorld();
        Location location = player.getLocation();

        ArmorStand armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setBasePlate(false);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.setArms(true);


        ItemStack banner = new ItemStack(banners);
        armorStand.getEquipment().setItemInMainHand(banner);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || armorStand.isDead() || !player.getInventory().contains(banner)) {
                    this.cancel();
                    armorStand.remove();
                    return;
                }

                Location playerLocation = player.getLocation();
                armorStand.teleport(playerLocation.add(0, 1.0, 0)); // Adjust the Y coordinate as needed
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(Battlecraft.class), 0L, 1L);
    }
}
