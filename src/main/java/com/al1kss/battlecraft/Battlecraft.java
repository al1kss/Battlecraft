package com.al1kss.battlecraft;

import com.al1kss.battlecraft.commands.Commands;
import com.al1kss.battlecraft.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public final class Battlecraft extends JavaPlugin {

    private static String plugin;
    public static Battlecraft instance;


    public static String getPlugin() {
        return plugin;
    }

    public static void setPlugin(String plugin) {
        Battlecraft.plugin = plugin;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getCommand("check").setExecutor(new Commands());
        getCommand("glow").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);


        //Ставит эффект свечения если баннер брошен
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Entity entity : Objects.requireNonNull(Bukkit.getWorld("world")).getEntities()) {
                    if (entity instanceof Item && (((Item) entity).getItemStack().getType().equals(Material.BLUE_BANNER) || ((Item) entity).getItemStack().getType().equals(Material.RED_BANNER))) {
                        entity.setGlowing(true);
                    }
                }
            }
        }.runTaskTimer(this, 0L, 20L);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
