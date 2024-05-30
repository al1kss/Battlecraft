package com.al1kss.battlecraft;

import com.al1kss.battlecraft.commands.Commands;
import com.al1kss.battlecraft.listeners.GUIListener;
import com.al1kss.battlecraft.listeners.Listeners;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Battlecraft extends JavaPlugin {

    private static Plugin plugin;
    public static Battlecraft instance;
    private static final Map<Location, Long> bannerPlacementTimes = new HashMap<>();

    private static final List<Location> redBL = new ArrayList<Location>();
    private static final List<Location> blueBL = new ArrayList<Location>();



    public static Plugin getPlugin() {
        return instance;
    }

    public static Location getRedBeacon(){
        return new Location(Bukkit.getWorld("world"), 5, 70, 5);
    }

    public static Location getBlueBeacon(){
        return new Location(Bukkit.getWorld("world"), 7, 70, 7);
    }

    public static void setPlugin(Plugin plugin) {
        Battlecraft.plugin = plugin;
    }
    @Override
    public void onEnable() {
        World world = Objects.requireNonNull(Bukkit.getWorld("world"));

        //change coods of flags
        redBL.add(new Location(world, 0, 70, 0));
        redBL.add(new Location(world, 1,70,0));
        redBL.add(new Location(world, 2, 70, 0));

        blueBL.add(new Location(world, 0, 70, 3));
        blueBL.add(new Location(world, 0,70,1));
        blueBL.add(new Location(world, 0, 70, 2));


        // Plugin startup logic
        instance = this;
        getCommand("divide").setExecutor(new Commands());
        getCommand("red").setExecutor(new Commands());
        getCommand("blue").setExecutor(new Commands());
        getCommand("stats").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListener(), this);




        //Ставит эффект свечения если баннер брошен
        new BukkitRunnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                World world = Objects.requireNonNull(Bukkit.getWorld("world"));


                //hcmode setup if beacon is placed
                if (world.getBlockAt(getRedBeacon()).getType().equals(Material.BEACON)){
                    for (Player p : Bukkit.getOnlinePlayers()){
                        String team = p.getPersistentDataContainer().get(new NamespacedKey(instance, "team"), PersistentDataType.STRING);
                        if (team.equalsIgnoreCase("blue")){
                            p.getPersistentDataContainer().set(new NamespacedKey(instance, "hcMode"), PersistentDataType.STRING, "on");
                        }
                    }
                }
                if (world.getBlockAt(getBlueBeacon()).getType().equals(Material.BEACON)){
                    for (Player p : Bukkit.getOnlinePlayers()){
                        String team = p.getPersistentDataContainer().get(new NamespacedKey(instance, "team"), PersistentDataType.STRING);
                        if (team.equalsIgnoreCase("red")){
                            p.getPersistentDataContainer().set(new NamespacedKey(instance, "hcMode"), PersistentDataType.STRING, "on");
                        }
                    }
                }

                for (Entity entity : world.getEntities()) {
                    if (entity instanceof Item) {
                        Item item = (Item) entity;
                        Material itemType = item.getItemStack().getType();

                        if (itemType == Material.BLUE_BANNER || itemType == Material.RED_BANNER) {
                            entity.setGlowing(true);

                            if (item.isOnGround()) {
                                Location location = item.getLocation();
                                location.getBlock().setType(itemType);

                                bannerPlacementTimes.put(location, currentTime);
                                item.remove();
                                location.getWorld().playSound(location, Sound.BLOCK_ANVIL_PLACE, 1, 1);
                                location.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, location, 3);
                            }
                        }
                    }
                }

                // Check if any banners have been on the ground for more than 1 minute
                bannerPlacementTimes.entrySet().removeIf(entry -> {
                    Location location = entry.getKey();
                    long placementTime = entry.getValue();

                    if (currentTime - placementTime >= 240000) { // 4 minute in milliseconds
                        if (location.getBlock().getType() == Material.BLUE_BANNER) {
                            location.getBlock().setType(Material.AIR);
                            Location returnLocation = new Location(world, 0, 0, 0);
                            for (int i=0; i<3; i++){
                                if (blueBL.get(i).getBlock().getType() != Material.BLUE_BANNER){
                                    returnLocation = blueBL.get(i);

                                }
                            }
                            world.getBlockAt(returnLocation).setType(Material.BLUE_BANNER);
                            for (Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(ChatColor.GREEN + "Флаг Синей команды возвращён на базу!");
                            }
                        } else if (location.getBlock().getType() == Material.RED_BANNER) {
                            location.getBlock().setType(Material.AIR);
                            Location returnLocation = new Location(world, 0,0,0);
                            for (int i=0; i<3; i++){
                                if (redBL.get(i).getBlock().getType() != Material.RED_BANNER){
                                    returnLocation = redBL.get(i);

                                }
                            }
                            world.getBlockAt(returnLocation).setType(Material.RED_BANNER);
                            for (Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(ChatColor.GREEN + "Флаг Красной команды возвращён на базу!");
                            }
                        }
                        return true;
                    }
                    return false;
                });
            }
        }.runTaskTimer(this, 0L, 20L);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
