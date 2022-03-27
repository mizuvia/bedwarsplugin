package inventories;

import game.Participant;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Map;

public class ShopGUI implements IGUI{

    private final Plugin plugin;
    private final Map<Integer, LinkedList<ShopItem>> shopItems;

    public ShopGUI(Plugin plugin, Map<Integer, LinkedList<ShopItem>> shopItems) {
        this.plugin = plugin;
        this.shopItems = shopItems;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, Inventory inventory) {
        if (inventory.getItem(slot) == null) return;

        Participant p = plugin.getPlayer(whoClicked);
        if (p.getInventories().containsKey(slot)) {
            SimpleInventory inv = p.getInventories().get(slot);
            p.getPlayer().openInventory(inv);
            return;
        }
        if (shopItems.containsKey(slot)) {
            SimpleInventory inv = (SimpleInventory) inventory;
            if (inv.makeTrade(p, inv.getItem(slot)))
                inv.updateSlot(slot);
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
