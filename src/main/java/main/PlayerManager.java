package main;

import game.Participant;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Objects;

public class PlayerManager {

    public static String getPlayerGroupName(Player player){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        LuckPerms api = provider.getProvider();
        return Objects.requireNonNull(api.getUserManager().getUser(player.getName())).getPrimaryGroup();
    }

    public static String getGroupDisplayName(Participant p){
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        LuckPerms api = provider.getProvider();
        String displayNameRaw = api.getGroupManager().getGroup(p.getGroup()).getDisplayName();
        if(displayNameRaw == null) displayNameRaw = "";

        return displayNameRaw.replaceAll("&", "§");
    }

    public static String getCodeColor(Participant p){
        switch (p.getTeam().getColor()){
            case "red": return "§4";
            case "green": return "§2";
            case "yellow": return "§e";
            case "cyan": return "§3";
            case "orange": return "§6";
            case "pink": return "§c";
            case "light_blue": return "§b";
            case "purple": return "§5";
            case "magenta": return "§d";
            case "black": return "§0";
            case "blue": return "§9";
            case "gray": return "§8";
            case "light_gray": return "§7";
            case "lime": return "§a";
            case "white": return "§f";
            default: return "§1";
        }
    }

    public static String getCodeColor(String color){
        switch (color){
            case "red": return "§4";
            case "green": return "§2";
            case "yellow": return "§e";
            case "cyan": return "§3";
            case "orange": return "§6";
            case "pink": return "§c";
            case "light_blue": return "§b";
            case "purple": return "§5";
            case "magenta": return "§d";
            case "black": return "§0";
            case "blue": return "§9";
            case "gray": return "§8";
            case "light_gray": return "§7";
            case "lime": return "§a";
            case "white": return "§f";
            default: return "§1";
        }
    }

    public static Color getColor(Participant p){
        switch (p.getTeam().getColor()){
            case "red": return Color.MAROON;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "cyan": return Color.TEAL;
            case "orange": return Color.ORANGE;
            case "pink": return Color.RED;
            case "light_blue": return Color.AQUA;
            case "purple": return Color.PURPLE;
            case "magenta": return Color.FUCHSIA;
            case "black": return Color.BLACK;
            case "blue": return Color.BLUE;
            case "gray": return Color.GRAY;
            case "light_gray": return Color.SILVER;
            case "lime": return Color.LIME;
            case "white": return Color.WHITE;
            default: return Color.NAVY;
        }
    }

    public static Color getColor(String color){
        switch (color){
            case "red": return Color.MAROON;
            case "green": return Color.GREEN;
            case "yellow": return Color.YELLOW;
            case "cyan": return Color.TEAL;
            case "orange": return Color.ORANGE;
            case "pink": return Color.RED;
            case "light_blue": return Color.AQUA;
            case "purple": return Color.PURPLE;
            case "magenta": return Color.FUCHSIA;
            case "black": return Color.BLACK;
            case "blue": return Color.BLUE;
            case "gray": return Color.GRAY;
            case "light_gray": return Color.SILVER;
            case "lime": return Color.LIME;
            case "white": return Color.WHITE;
            default: return Color.NAVY;
        }
    }
}
