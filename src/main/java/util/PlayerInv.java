package util;

import game.Participant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;

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

        PlayerInventory inventory = p.getPlayer().getInventory();
        inventory.clear();

        Iterator<ItemStack> iterator = p.getRespawnItems().iterator();

        while(iterator.hasNext()){
            ItemStack item = iterator.next();
            p.giveItem(item);
            iterator.remove();
        }

    }

}
