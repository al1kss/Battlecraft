package com.al1kss.battlecraft.listeners;

import com.al1kss.battlecraft.Battlecraft;
import com.al1kss.battlecraft.events.Events;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;


import static com.al1kss.battlecraft.events.Events.createStand;


public class Listeners implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if (event.getBlock().getType().equals(Material.RED_BANNER) || event.getBlock().getType().equals(Material.BLUE_BANNER) || event.getBlock().getType().equals(Material.BEACON)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();

        String hcMode = player.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "hcMode"), PersistentDataType.STRING);
        if (hcMode == "on"){
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("Вы умерли, но уже навсегда!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player p = event.getEntity();
        PersistentDataContainer data = p.getPersistentDataContainer();

        Player killer = p.getKiller();
        if (killer instanceof Player){
            int kills = killer.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER);
            kills ++;
            killer.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER, kills);
            String killerName = killer.getName();
            String weapon = killer.getInventory().getItemInMainHand().getType().toString();

            // Create player head item
            ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
            meta.setOwningPlayer(p);
            meta.setDisplayName("§l§3"+p.getName() + "'s Head");
            meta.setLore(Arrays.asList("Killed by: " + killerName, "\nWith: " + weapon));
            playerHead.setItemMeta(meta);
            p.getWorld().dropItemNaturally(p.getLocation(), playerHead);
        }

        int deathCount = data.get(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER);
        deathCount++;
        data.set(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER, deathCount);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getBlock().getType().equals(Material.BEACON)){
            Player player = event.getPlayer();
            PersistentDataContainer data = player.getPersistentDataContainer();
            String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
            if (team == "red" && Battlecraft.getRedBeacon().equals(event.getBlock().getLocation())){
                event.setCancelled(false);
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(ChatColor.GOLD+ "Красная команда поставила маяк! Теперь игроки Синей Команды" + ChatColor.RED + " уязвимы!");
                    String teamCheck = p.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
                    if (teamCheck == "blue"){
                        p.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "hcMode"), PersistentDataType.STRING, "on");
                    }
                }
            } else if (team == "blue" && Battlecraft.getBlueBeacon().equals(event.getBlock().getLocation())){
                event.setCancelled(false);
                for (Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(ChatColor.GOLD+ "Синяя команда поставила маяк! Теперь игроки Красной Команды" + ChatColor.RED + " уязвимы!");
                    String teamCheck = p.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
                    if (teamCheck == "red"){
                        p.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "hcMode"), PersistentDataType.STRING, "on");

                    }
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        //invCheck(player.getInventory());



        if (!player.getPersistentDataContainer().has(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER)){
            player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER, 0);
            player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER, 0);
            player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "none");
            player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "hcMode"), PersistentDataType.STRING, "off");
        }



        //почему то при перезаходе нельзя захватить флаг, временный фикс

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.instance, new Runnable() {
            @Override
            public void run() {
                if (player.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING).equalsIgnoreCase("red")){
                    player.getPersistentDataContainer().remove(new NamespacedKey(Battlecraft.getPlugin(), "team"));
                    player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "red");
                    Events.addRedT(player);
                    player.sendMessage("RED");
                } else if (player.getPersistentDataContainer().get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING).equalsIgnoreCase("blue")){
                    player.getPersistentDataContainer().remove(new NamespacedKey(Battlecraft.getPlugin(), "team"));
                    player.getPersistentDataContainer().set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "blue");
                    Events.addBlueT(player);
                    player.sendMessage("BLUE");
                }
            }
        }, 6);


    }

    @EventHandler
    public void onDroppedItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            PersistentDataContainer data = player.getPersistentDataContainer();
            String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);

            if (event.getItem().getItemStack().getType().equals(Material.RED_BANNER) && (team.equalsIgnoreCase("red") || player.getInventory().contains(Material.RED_BANNER))){
                event.setCancelled(true);
            } else if (event.getItem().getItemStack().getType().equals(Material.BLUE_BANNER) && (team.equalsIgnoreCase("blue") || player.getInventory().contains(Material.BLUE_BANNER))) {
                event.setCancelled(true);
            }
        }
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
