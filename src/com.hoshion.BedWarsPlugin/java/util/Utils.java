package util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static Location getLocation(String cord){
        return new Location(Bukkit.getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]), Double.parseDouble(cord.split(" ")[2]) + 0.5, (cord.split(" ").length > 3) ? Float.parseFloat(cord.split(" ")[3]) : 0, (cord.split(" ").length > 4) ? Float.parseFloat(cord.split(" ")[4]) : 0);
    }

    public static Location centralizeLocation(Location loc){
        return new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5, loc.getYaw(), loc.getPitch());
    }

    public static String getTime(int seconds){
        String minutesStr = (seconds >= 60) ? String.valueOf(seconds / 60) : "0";
        String secondsStr = (seconds >= 60) ? ((seconds % 60 > 9) ? String.valueOf(seconds % 60) : "0" + seconds % 60) : ((seconds > 9) ? String.valueOf(seconds) : "0" + seconds);

        return minutesStr + ":" + secondsStr;
    }

    public static ItemStack clearItem(ItemStack item){
        ItemStack newItem = new ItemStack(item.getType(), item.getAmount());
        newItem.setItemMeta(item.getItemMeta());
        ItemMeta meta = newItem.getItemMeta();
        meta.setLore(null);
        newItem.setItemMeta(meta);

        return newItem;
    }

    public static void connectToHub(Player p) {
        ByteArrayDataOutput hub = ByteStreams.newDataOutput();
        hub.writeUTF("Connect");
        hub.writeUTF("hub");

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload(new MinecraftKey("bungeecord:main"), new PacketDataSerializer(Unpooled.wrappedBuffer(hub.toByteArray())));
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }

    private static final List<String> ROMAN_NUMERALS = Arrays.asList("0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX");

    public static String getRomanNumeral(int number){
        return ROMAN_NUMERALS.get(number);
    }
}
