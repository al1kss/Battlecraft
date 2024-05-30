package com.al1kss.battlecraft.events;

import com.al1kss.battlecraft.Battlecraft;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;



public class Events {


    private static Team blueT;
    private static Team redT;
    private static Scoreboard board;

    public static void createTeams(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("Stats", "dummy", "Команды");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        redT = board.registerNewTeam("Red");
        blueT = board.registerNewTeam("Blue");

        redT.setColor(ChatColor.RED);
        blueT.setColor(ChatColor.BLUE);

        redT.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
        blueT.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OTHER_TEAMS);
    }

    public static void addRedT(Player player){
        if (redT == null) {
            createTeams();
        }
        redT.addEntry(player.getName());
        player.setScoreboard(board);
    }
    public static void addBlueT(Player player){
        if (blueT == null) {
            createTeams();
        }
        blueT.addEntry(player.getName());
        player.setScoreboard(board);
    }

    public static void removeTeam(Player player, String team){
        player.getScoreboard().getTeam(team).removeEntry(player.getName());
    }


    //dividing code
    public static void divide(){
        int red = 0;
        int blue = 1;
        for (Player p : Bukkit.getOnlinePlayers()){
            PersistentDataContainer data = p.getPersistentDataContainer();
            if (red == 0){
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "red");
                red = 1;
                blue = 0;
                addRedT(p);
            } else if (blue==0) {
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "blue");
                red = 0;
                blue = 1;
                addBlueT(p);
            }
        }
    }


    public static void createStand(Player player, Material banners){
        World world = player.getWorld();
        Location location = player.getLocation();

        ArmorStand armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setBasePlate(false);
        armorStand.setCollidable(false);
        armorStand.setInvulnerable(true);
        armorStand.setArms(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);

        ItemStack banner = new ItemStack(banners);
        armorStand.getEquipment().setHelmet(banner);



        new BukkitRunnable() {
            @Override
            public void run() {
                if (armorStand.isDead() || !player.getInventory().contains(banner)) {
                    this.cancel();
                    armorStand.remove();
                    return;
                } else if (!player.isOnline()) {
                    this.cancel();
                    armorStand.remove();
                    player.getInventory().remove(banner);
                    Bukkit.getWorlds().get(0).dropItemNaturally(player.getLocation(), banner);
                    return;
                }

                Location playerLocation = player.getLocation();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 2));
                armorStand.teleport(playerLocation.add(0, 0.08, 0)); // Adjust the Y coordinate as needed
            }
        }.runTaskTimer(JavaPlugin.getProvidingPlugin(Battlecraft.class), 0L, 1L);
    }

    public static void removeBanner(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        double x = data.get(new NamespacedKey(Battlecraft.getPlugin(), "bannerX"), PersistentDataType.DOUBLE);
        double y = data.get(new NamespacedKey(Battlecraft.getPlugin(), "bannerY"), PersistentDataType.DOUBLE);
        double z = data.get(new NamespacedKey(Battlecraft.getPlugin(), "bannerZ"), PersistentDataType.DOUBLE);

        Location bannerLocation = new Location(player.getWorld(), x, y, z);
        bannerLocation.getBlock().setType(Material.AIR);
    }
}
