package util;

import game.Participant;
import inventories.ShopItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.logging.Logger;

public class PlayerInv {

    public static void setWaitingInventory(Participant p){

        PlayerInventory inv = p.getPlayer().getInventory();
        inv.clear();

        ItemStack chooseTeam = new ItemStack(Material.RED_BED, 1);
        ItemMeta ctMeta = chooseTeam.getItemMeta();
        ctMeta.setDisplayName("Выбор команды");
        ctMeta.setLocalizedName("choosing_team");

        chooseTeam.setItemMeta(ctMeta);
        inv.setItem(0, chooseTeam);

        ItemStack leave = new ItemStack(Material.DARK_OAK_DOOR, 1);
        ItemMeta lMeta = leave.getItemMeta();
        lMeta.setDisplayName("Выйти");
        lMeta.setLocalizedName("leave");

        leave.setItemMeta(lMeta);
        inv.setItem(8, leave);
    }

    public static void setPlayingInventory(Participant p){

        clear(p);

        Iterator<ItemStack> iterator = p.getRespawnItems().iterator();

        while(iterator.hasNext()){
            ItemStack item = iterator.next();
            p.giveItem(item);
            iterator.remove();
        }

    }

    public static boolean hasShopItem(PlayerInventory inv, ShopItem item) {
        Material mat = item.getMaterial();
        if (inv.all(mat).size() != 0) return true;
        if (inv.getItemInOffHand() != null && inv.getItemInOffHand().getType() == mat) return true;
        return inv.getHolder().getItemOnCursor() != null && inv.getHolder().getItemOnCursor().getType() == mat;
    }

    public static void removeShopItem(PlayerInventory inv, ShopItem item, int amount) {
        Material mat = item.getMaterial();
        ItemStack i = null;

        if (inv.first(mat) != -1) {
            i = inv.getItem(inv.first(mat));
        }
        if (i == null) {
            i = inv.getItemInOffHand();
            if (i == null || i.getType() != mat)
                i = null;
        }
        if (i == null) {
            i = inv.getHolder().getItemOnCursor();
            if (i == null || i.getType() != mat)
                i = null;
        }

        if (i != null) {
            if (amount == 0) i.setAmount(amount);
            else i.setAmount(i.getAmount() - amount);

            ((Player) inv.getHolder()).updateInventory();
        }
    }

    public static void clear(Participant p) {
        PlayerInventory inv = p.getPlayer().getInventory();
        inv.clear();
        p.getPlayer().getItemOnCursor().setAmount(0);
        p.getPlayer().updateInventory();
    }
}
