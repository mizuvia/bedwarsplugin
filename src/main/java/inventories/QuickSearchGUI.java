package inventories;

import game.Participant;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class QuickSearchGUI implements InventoryHolder {

    private final Plugin plugin;
    private ShopInventory inventory;
    public static int i = 0;

    public void setInventory(ShopInventory inventory) {
        this.inventory = inventory;
    }

    public QuickSearchGUI(Plugin plugin){
        this.plugin = plugin;
    }

    public void onClick(Player whoClicked, int slot, ClickType type){
        if (inventory.getItem(slot) == null) return;

        Participant p = plugin.getPlayer(whoClicked);
        if (ShopGUI.openShopInventory(p, slot)) return;

        if (inventory.shopItems.containsKey(slot)) {
            LinkedList<ShopItem> item = inventory.shopItems.get(slot);
            if (item.getFirst() == ShopItem.SEARCH_SLOT) {
                ChangeItemGUI.UPDATING_SLOTS.put(whoClicked.getUniqueId(), slot);
                p.getPlayer().openInventory(plugin.getChangeItemInventory());
            } else {
                if (type.isRightClick()) {
                    ChangeItemGUI.UPDATING_SLOTS.put(whoClicked.getUniqueId(), slot);
                    p.getPlayer().openInventory(plugin.getChangeItemInventory());
                } else if (type.isLeftClick()) {
                    if (inventory.makeTrade(p, inventory.getItem(slot)))
                        inventory.updateSlot(p, slot);
                }
            }
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
