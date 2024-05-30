package com.al1kss.battlecraft.commands;

import com.al1kss.battlecraft.Battlecraft;
import com.al1kss.battlecraft.events.Events;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commands implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        String cmd = command.getName();

        if (cmd.equalsIgnoreCase("divide")) {
            Events.divide();


        } else if (cmd.equalsIgnoreCase("red")) {
            if (args.length == 0){
                List<Player> redT = new ArrayList<>();

                for (Player p : Bukkit.getOnlinePlayers()){
                    PersistentDataContainer data = p.getPersistentDataContainer();
                    String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
                    if (team.equalsIgnoreCase("red")){
                        redT.add(p);
                    }
                }
                player.sendMessage(String.valueOf(redT));
            } else if (args.length == 2 && args[0].equalsIgnoreCase("add")){
                Player a = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "red");
                Events.addRedT(a);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                Player a = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "none");
                Events.removeTeam(a, "Red");
            }


        } else if (cmd.equalsIgnoreCase("blue")) {
            if (args.length == 0){
                List<Player> blueT = new ArrayList<>();

                for (Player p : Bukkit.getOnlinePlayers()){
                    PersistentDataContainer data = p.getPersistentDataContainer();
                    String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
                    if (team.equalsIgnoreCase("blue")){
                        blueT.add(p);
                    }
                }
                player.sendMessage(String.valueOf(blueT));
            } else if (args.length == 2 && args[0].equalsIgnoreCase("add")){
                Player a = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "blue");
                Events.addBlueT(a);
            } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
                Player a = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "none");
                Events.removeTeam(a, "Blue");
            }


        } else if (cmd.equalsIgnoreCase("stats") && sender.hasPermission("foo.bar")){
            if (args[0].equalsIgnoreCase("death")){
                PersistentDataContainer data = player.getPersistentDataContainer();
                int deathcount = data.get(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER);
                player.sendMessage(ChatColor.GREEN+ "Вы умерли "+deathcount+" раз");
            } else if(args[0].equalsIgnoreCase("kills")){
                PersistentDataContainer data = player.getPersistentDataContainer();
                int kills = data.get(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER);
                player.sendMessage(ChatColor.GREEN+ "Вы убили "+kills+" раз");
            } else if(args[0].equalsIgnoreCase("reset") && args.length == 1){
                PersistentDataContainer data = player.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER, 0);
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER, 0);

            } else if(args[0].equalsIgnoreCase("reset") && args.length == 2 && player.hasPermission("permission.node")){
                Player a = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER, 0);
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER, 0);

            }else if(args[0].equalsIgnoreCase("reset") && args.length == 3 && player.hasPermission("permission.node")){
                Player a = Bukkit.getPlayer(args[1]);  // /stats reset al1kss team
                PersistentDataContainer data = a.getPersistentDataContainer();
                data.set(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING, "none");


            } else if(args[0].equalsIgnoreCase("all") && args.length == 1){
                PersistentDataContainer data = player.getPersistentDataContainer();
                int kills = data.get(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER);
                int deaths = data.get(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER);
                String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);
                player.sendMessage(ChatColor.GREEN + "У вас "+ kills+ " убийств!");
                player.sendMessage(ChatColor.GREEN + "У вас "+ deaths+ " смертей!");
                player.sendMessage(ChatColor.GREEN + "Вы в "+ team+ " команде!");
            } else if(args[0].equalsIgnoreCase("all") && args.length == 2 && player.hasPermission("permission.node")){
                Player pl = Bukkit.getPlayer(args[1]);
                PersistentDataContainer data = pl.getPersistentDataContainer();
                int kills = data.get(new NamespacedKey(Battlecraft.getPlugin(), "kills"), PersistentDataType.INTEGER);
                int deaths = data.get(new NamespacedKey(Battlecraft.getPlugin(), "deathcount"), PersistentDataType.INTEGER);
                String team = data.get(new NamespacedKey(Battlecraft.getPlugin(), "team"), PersistentDataType.STRING);

                player.sendMessage(ChatColor.GREEN + "У "+pl.getName() +" "+ kills+ " убийств!");
                player.sendMessage(ChatColor.GREEN + "У "+pl.getName()+" "+ deaths+ " смертей!");
                player.sendMessage(ChatColor.GREEN +pl.getName()+" в " + team + " команде!");
                return false;
            }
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
            if (command.getName().equalsIgnoreCase("stats")){
                if (args.length == 1){
                    return Arrays.asList("kills", "death", "all");
                }
            } else if (command.getName().equalsIgnoreCase("red")) {
                if (args.length == 1){
                    return Arrays.asList("add", "remove");
                }
            } else if (command.getName().equalsIgnoreCase("blue")) {
                if (args.length == 1){
                    return Arrays.asList("add", "remove");
                }
            }
        return null;
    }
}
