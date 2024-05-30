package com.al1kss.battlecraft.listeners;

import com.al1kss.battlecraft.Battlecraft;
import com.al1kss.battlecraft.events.Events;
import com.al1kss.battlecraft.gui.GUIBanners;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class GUIListener implements Listener {


    //removing banners recipe
    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {
        Material itemType = e.getRecipe().getResult().getType();
        Byte itemData = e.getRecipe().getResult().getData().getData();
        if(itemType==Material.RED_BANNER || itemType==Material.BLUE_BANNER || itemType == Material.BEACON) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            for(HumanEntity he:e.getViewers()) {
                if(he instanceof Player) {
                    ((Player)he).sendMessage(ChatColor.RED+"Хотел обхитрить меня?! ;)");
                }
            }
        }
    }

    @EventHandler
    public static void bannerPickUp(PlayerInteractEvent event){
        if (event.getClickedBlock() != null){
        Material block = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();
        String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);

        Location location = event.getClickedBlock().getLocation();
        GUIBanners gui = new GUIBanners();

        if (block.equals(Material.BLUE_BANNER) && team.equalsIgnoreCase("red") && !player.getInventory().contains(Material.BLUE_BANNER)){
            player.sendMessage(ChatColor.BLUE + "Вы захватываете Синий Флаг!");
            player.openInventory(gui.getInventory());

            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerX"), PersistentDataType.DOUBLE, location.getX());
            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerY"), PersistentDataType.DOUBLE, location.getY());
            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerZ"), PersistentDataType.DOUBLE, location.getZ());

        } else if (block.equals(Material.RED_BANNER) && team.equalsIgnoreCase("blue") && !player.getInventory().contains(Material.RED_BANNER)) {
            player.sendMessage(ChatColor.RED + "Вы захватываете Красный флаг!");
            player.openInventory(gui.getInventory());

            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerX"), PersistentDataType.DOUBLE, location.getX());
            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerY"), PersistentDataType.DOUBLE, location.getY());
            data.set(new NamespacedKey(Battlecraft.getPlugin(), "bannerZ"), PersistentDataType.DOUBLE, location.getZ());
        }}
    }


    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getClickedInventory() == null){ return; }
        if (event.getClickedInventory().getHolder() instanceof GUIBanners){ //проверяем наш ли инвентарь, а не обычны й сундук
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            PersistentDataContainer data = player.getPersistentDataContainer();
            String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);

            if (event.getCurrentItem() == null) { return; }
            if (event.getCurrentItem().getType() == Material.WHITE_WOOL){
                if (team == "red"){
                    event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.BLUE_WOOL));//change depending on a team
                    if (event.getClickedInventory().contains(Material.BLUE_WOOL, 9)){
                        player.closeInventory();
                        player.getInventory().addItem(new ItemStack(Material.BLUE_BANNER));
                        //destroy banner that was clicked on in bannerPickUp
                        Events.removeBanner(player);
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.sendMessage(ChatColor.DARK_RED + "Флаг Синей команды был украден!");
                        }
                    }
                } else if (team == "blue") {
                    event.getClickedInventory().setItem(event.getSlot(), new ItemStack(Material.RED_WOOL)); //change depending on a team
                    if (event.getClickedInventory().contains(Material.RED_WOOL, 9)){
                        player.closeInventory();
                        player.getInventory().addItem(new ItemStack(Material.RED_BANNER));
                        Events.removeBanner(player);
                        for (Player p : Bukkit.getOnlinePlayers()){
                            p.sendMessage(ChatColor.DARK_RED + "Флаг Красной команды был украден!");
                        }
                    }
                } else if (team == "none") {
                    player.sendMessage(ChatColor.RED+"Вы ни в какой команде");
                }
            }
        }
    }

}
