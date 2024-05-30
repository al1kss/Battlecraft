package com.al1kss.battlecraft.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIBanners implements InventoryHolder{
    private Inventory inv;

    public GUIBanners() {
        inv = Bukkit.createInventory(this, 9, "Akbiy lox");
        init();
    }

    private void init() {
        ItemStack item;
        for (int i = 0; i<9; i++){
            item = createItem("Нажми на меня!", Material.WHITE_WOOL, ChatColor.WHITE);
            inv.setItem(i, item);
        }
    }

    private ItemStack createItem(String name, Material mat, ChatColor color){
        ItemStack item = new ItemStack(mat, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color+name);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
