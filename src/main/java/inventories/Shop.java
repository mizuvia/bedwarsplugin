package inventories;

/* << СВЯТ НАТУРАЛ >> */

import game.Participant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Shop implements IGUI {

    private final Participant player;

    public Shop(Participant p){
        this.player = p;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, Inventory inventory) {
        if (player.getInventories().containsKey(slot)){
            SimpleInventory inv = player.getInventories().get(slot);
            player.getPlayer().openInventory(inv);
            return;
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return player.getShopInventory();
    }
}
