package main;

import game.Participant;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import util.Colors;

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

        return Colors.colorize(displayNameRaw);
    }

    public static String getCodeColor(Participant p){
        return getCodeColor(p.getTeam().getColor());
    }

    public static String getCodeColor(String color){
        return switch (color) {
            case "red" -> "§4";
            case "green" -> "§2";
            case "yellow" -> "§e";
            case "cyan" -> "§3";
            case "orange" -> "§6";
            case "pink" -> "§c";
            case "light_blue" -> "§b";
            case "purple" -> "§5";
            case "magenta" -> "§d";
            case "black" -> "§0";
            case "blue" -> "§9";
            case "gray" -> "§8";
            case "light_gray" -> "§7";
            case "lime" -> "§a";
            case "white" -> "§f";
            default -> "§1";
        };
    }

    public static Color getColor(Participant p){
        return getColor(p.getTeam().getColor());
    }

    public static Color getColor(String color){
        return switch (color) {
            case "red" -> Color.MAROON;
            case "green" -> Color.GREEN;
            case "yellow" -> Color.YELLOW;
            case "cyan" -> Color.TEAL;
            case "orange" -> Color.ORANGE;
            case "pink" -> Color.RED;
            case "light_blue" -> Color.AQUA;
            case "purple" -> Color.PURPLE;
            case "magenta" -> Color.FUCHSIA;
            case "black" -> Color.BLACK;
            case "blue" -> Color.BLUE;
            case "gray" -> Color.GRAY;
            case "light_gray" -> Color.SILVER;
            case "lime" -> Color.LIME;
            case "white" -> Color.WHITE;
            default -> Color.NAVY;
        };
    }
}
