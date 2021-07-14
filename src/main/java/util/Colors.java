package util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;

public class Colors{

    public static boolean validateHex(String hex){
        return hex.matches("[a-fA-F0-9]{6}");
    }

    public static Color HexToColor(String hex)
    {
        hex = hex.replace("#", "");
        try{
            return Color.fromRGB(
                    Integer.valueOf(hex.substring(0, 2), 16),
                    Integer.valueOf(hex.substring(2, 4), 16),
                    Integer.valueOf(hex.substring(4, 6), 16));
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String HexToString(String hex){
        Color color = Colors.HexToColor(hex);
        if(color == null) return "";
        return String.valueOf(color);
    }

    /**
     * Translating HEX Code to ChatColor String
     * @param hex HEX Code
     * @return ChatColor String or #hexcode
     * */

    public static String fromHex(String hex){
        hex = hex.replaceAll("#", "");
        if(!validateHex(hex)) return "#".concat(hex);
        return ChatColor.of("#" + hex).toString();
    }

    /**
     * Replacing #FFFFFF with ColorChat string §x§f§f§f§f§f§f
     * @param str String that contains HEX code
     * @return String that contains ChatColor code
     */

    public static String replaceHex(String str){
        if(!str.contains("#")) return str;
        StringBuilder s = new StringBuilder();
        s.append(str.split("#")[0]);
        for(int i = 1; i <= str.split("#").length - 1; i++){

            String part = str.split("#")[i];
            String hex = part.substring(0, 6);
            String msg = part.substring(6);

            s.append(Colors.fromHex(hex));
            s.append(msg);
        }

        return s.toString();
    }

    /**
     * Transform string with color codes to string without it
     * @param str String with color codes
     * @return String without color codes
     */

    public static String clearColors(String str){
        return str.replaceAll("§.", "");
    }
}
