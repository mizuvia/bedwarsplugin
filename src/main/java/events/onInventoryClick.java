package events;

import game.Participant;
import inventories.IGUI;
import inventories.ShopItem;
import inventories.ShopItems;
import inventories.SimpleInventory;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

public class onInventoryClick extends SimpleListener implements Listener, EventExecutor {

    public onInventoryClick(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        InventoryClickEvent e = (InventoryClickEvent) event;

        if(e.getClickedInventory() == null) return;

        Logger.getLogger("").info(e.getAction().name());

        InventoryView view = e.getView();
        Participant p = plugin.getPlayers().get(e.getWhoClicked().getUniqueId());

        ItemStack updatedItem = new ItemStack(Material.DIAMOND_BLOCK);
        InventoryHolder holder = e.getClickedInventory().getHolder();
        InventoryAction act = e.getAction();

        if (view.getTopInventory().getHolder() instanceof Chest){

            switch (act) {
                case MOVE_TO_OTHER_INVENTORY -> {
                    if (holder instanceof Chest) {
                        ItemStack item = e.getCurrentItem();
                        if (!ShopItems.isTool(item.getType())) return;
                        e.setCancelled(true);
                        e.getClickedInventory().setItem(e.getSlot(), null);
                        ItemStack item2 = findTool(view.getBottomInventory(), item);
                        if (item2 != null) swapItem(view.getBottomInventory(), item2, view);
                        updatedItem = item.clone();
                    } else {
                        updatedItem = e.getCurrentItem().clone();
                        updatedItem.setAmount(2);
                    }
                }
                case PLACE_ALL, PLACE_ONE -> {
                    if (holder instanceof Player) {
                        ItemStack item = e.getCursor();
                        if (!ShopItems.isTool(item.getType())) return;
                        e.setCancelled(true);
                        e.getWhoClicked().setItemOnCursor(null);
                        ((Player) e.getWhoClicked()).updateInventory();
                        ItemStack item2 = findTool(view.getBottomInventory(), item);
                        if (item2 != null) swapItem(view.getBottomInventory(), item2, view);
                        updatedItem = item.clone();
                    }
                }
                case SWAP_WITH_CURSOR -> {
                    if (holder instanceof Player) {
                        ItemStack item = e.getCursor();
                        if (!ShopItems.isTool(item.getType())) return;
                        e.setCancelled(true);
                        e.getWhoClicked().setItemOnCursor(e.getCurrentItem());
                        ((Player) e.getWhoClicked()).updateInventory();
                        ItemStack item2 = findTool(view.getBottomInventory(), item);
                        if (item2 != null && item2 != e.getCurrentItem())
                            swapItem(view.getBottomInventory(), item2, view);
                        updatedItem = item.clone();
                    }
                }
                case PICKUP_ALL, PICKUP_HALF, DROP_ALL_SLOT, DROP_ONE_SLOT -> {
                    if (holder instanceof Player) {
                        ItemStack item = e.getCurrentItem();
                        if (!ShopItems.isTool(item.getType())) return;
                        Logger.getLogger("").info(item.getItemMeta().getDisplayName());
                        updatedItem = item.clone();
                        Logger.getLogger("").info(updatedItem.getItemMeta().getDisplayName());
                        updatedItem.setAmount(2);
                    }
                }
                case HOTBAR_MOVE_AND_READD, HOTBAR_SWAP -> {
                    if (holder instanceof Player) return;
                    ItemStack item = e.getCurrentItem();
                    if (item == null) {
                        item = view.getBottomInventory().getItem(e.getHotbarButton());
                    } else e.setCancelled(true);
                    if (!ShopItems.isTool(item.getType())) return;
                    if (act == InventoryAction.HOTBAR_MOVE_AND_READD)
                        swapItem(view.getBottomInventory(), view.getBottomInventory().getItem(e.getHotbarButton()), view);
                    updatedItem = item.clone();
                    if (e.getCurrentItem() == null)
                        updatedItem.setAmount(2);
                }
            }


            if (updatedItem.getType() != Material.DIAMOND_BLOCK) {
                ShopItem item = ShopItem.getShopItem(updatedItem.getItemMeta().getDisplayName());
                int index = ShopItems.getIndex(ShopItems.TOOLS, item);
                SimpleInventory inv = p.getShopInventory(ShopItem.TOOLS);

                if (updatedItem.getAmount() == 2) inv.updateSlot(index, null);
                else {
                    p.giveItem(item.getItem());
                    inv.updateSlot(index, item);
                }
            }
        }

        if(holder instanceof IGUI gui){
            e.setCancelled(true);
            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getClickedInventory());
        }
        if(holder instanceof HumanEntity){
            switch (e.getRawSlot()) {
                case 0, 1, 2, 3, 4, 5, 6, 7, 8-> e.setCancelled(true);
            }
        }
    }

    public ItemStack findTool(Inventory inv, ItemStack i) {
        ShopItem shopItem = ShopItem.getShopItem(i.getItemMeta().getDisplayName());
        List<ShopItem> list = ShopItems.getList(ShopItems.TOOLS, shopItem);

        for (ShopItem item : list) {
            int index = inv.first(item.getMaterial());
            if (index == -1) continue;
            return inv.getItem(index);
        }
        return null;
    }

    public void swapItem(Inventory inv, ItemStack item, InventoryView view) {
        Inventory opposite = view.getBottomInventory() == inv ? view.getTopInventory() : view.getBottomInventory();
        inv.remove(item);
        opposite.addItem(item);
    }
}
