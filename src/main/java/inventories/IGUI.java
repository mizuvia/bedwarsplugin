package inventories;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface IGUI extends InventoryHolder {
    void onGUIClick(Player whoClicked, int slot, Inventory inventory);
}
