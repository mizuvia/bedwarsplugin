package util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Utils {

    public static String getTime(int seconds){
        String minutesStr = (seconds >= 60) ? String.valueOf(seconds / 60) : "0";
        String secondsStr = (seconds >= 60) ? ((seconds % 60 > 9) ? String.valueOf(seconds % 60) : "0" + seconds % 60) : ((seconds > 9) ? String.valueOf(seconds) : "0" + seconds);

        return minutesStr + ":" + secondsStr;
    }

    public static ItemStack clearItem(ItemStack item){
        ItemStack newItem = new ItemStack(item.getType(), item.getAmount());
        ItemMeta meta = item.getItemMeta();
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

    public static ItemStack createItem(Material material, int amount, String name){
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    public static void sendToAll(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
    }

    public static void setArmor(PlayerInventory inv, ItemStack armor) {
        String name = armor.getType().name();
        if (name.matches(".+HELMET")) inv.setHelmet(armor);
        if (name.matches(".+CHESTPLATE")) inv.setChestplate(armor);
        if (name.matches(".+LEGGINGS")) inv.setLeggings(armor);
        if (name.matches(".+BOOTS")) inv.setBoots(armor);
    }

    public static boolean isRightClick(Action a){
        return a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK;
    }

    public static boolean isUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }

    }
}
