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
import util.PlayerInv;

import java.util.LinkedList;
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

//        InventoryView view = e.getView();
//        Participant p = plugin.getPlayers().get(e.getWhoClicked().getUniqueId());

//        ItemStack updatedItem = new ItemStack(Material.DIAMOND_BLOCK);
//        InventoryHolder holder = e.getClickedInventory().getHolder();
//        InventoryAction act = e.getAction();
//
//        if (view.getTopInventory().getHolder() instanceof Chest) {
//            ItemStack item = null;
//
//            if (holder instanceof Chest && act == InventoryAction.MOVE_TO_OTHER_INVENTORY)
//                item = e.getCurrentItem();
//                updatedItem = item.clone();
//                e.setCancelled(true);
//            if (holder instanceof Player) {
//                switch (act) {
//                    case PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR -> {
//                        item = e.getCursor();
//                        e.getWhoClicked().setItemOnCursor(act == InventoryAction.SWAP_WITH_CURSOR ? null : e.getCurrentItem());
//                        e.setCancelled(true);
//                    }
//                    case PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME, DROP_ALL_SLOT, DROP_ONE_SLOT -> item = e.getCurrentItem();
//                }
//                updatedItem = item.clone();
//                List<InventoryAction> list = List.of(
//                        InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.PICKUP_ALL,
//                        InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ONE,
//                        InventoryAction.PICKUP_SOME, InventoryAction.DROP_ALL_SLOT,
//                        InventoryAction.DROP_ONE_SLOT
//                );
//                if (list.contains(act)) updatedItem.setAmount(0);
//            }
//
//            if (item == null || !ShopItems.isTool(item.getType())) {
//                e.setCancelled(false);
//                return;
//            }
//
//            ItemStack item2 = findTool(view.getBottomInventory(), item);
//            if (item2 != null) {
//                List<InventoryAction> list = List.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME)
//                if ((holder instanceof Chest && act == InventoryAction.MOVE_TO_OTHER_INVENTORY)
//                || (holder instanceof Player && ))
//            }
//
//        }

//        switch (e.getAction()) {
//            case MOVE_TO_OTHER_INVENTORY -> {
//                if (holder instanceof Chest) {
//                    ItemStack item = e.getCurrentItem();
//                    if (!ShopItems.isTool(item.getType())) return;
//                    e.setCancelled(true);
//                    ItemStack item2 = findTool(view.getBottomInventory(), item);
//                    if (item2 != null) swapItem(view.getBottomInventory(), item2);
//                    updatedItem = item;
//                } else {
//                    updatedItem = e.getCurrentItem().clone();
//                    updatedItem.setAmount(0);
//                }
//            }
//            case PLACE_ALL, PLACE_ONE, PLACE_SOME -> {
//                if (holder instanceof Player) {
//                    ItemStack item = e.getCursor();
//                    if (!ShopItems.isTool(item.getType())) return;
//                    e.setCancelled(true);
//                    e.getCursor().setAmount(0);
//                    ItemStack item2 = findTool(view.getBottomInventory(), item);
//                    if (item2 != null) swapItem(view.getBottomInventory(), item2);
//                    updatedItem = item;
//                }
//            }
//            case SWAP_WITH_CURSOR -> {
//                if (holder instanceof Player) {
//                    ItemStack item = e.getCursor();
//                    if (!ShopItems.isTool(item.getType())) return;
//                    e.setCancelled(true);
//                    e.getWhoClicked().setItemOnCursor(e.getCurrentItem());
//                    ItemStack item2 = findTool(view.getBottomInventory(), item);
//                    if (item2 != null && item2 != e.getCurrentItem()) swapItem(view.getBottomInventory(), item2);
//                    updatedItem = item;
//                }
//            }
//            case PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME, DROP_ALL_SLOT, DROP_ONE_SLOT -> {
//                if (holder instanceof Player) {
////                    ItemStack item = e.getCurrentItem();
////                    if (!ShopItems.isTool(item.getType())) return;
////                    updatedItem = item.clone();
////                    updatedItem.setAmount(0);
//                }
//            }
//        }


//        if (updatedItem.getType() != Material.DIAMOND) {
//            ShopItem item = ShopItem.getShopItem(updatedItem.getItemMeta().getDisplayName());
//            int index = ShopItems.getIndex(ShopItems.TOOLS, item);
//            SimpleInventory inv = p.getShopInventory(ShopItem.TOOLS);
//
//            if (updatedItem.getAmount() == 0) inv.updateSlot(index, null);
//            else {
//                p.giveItem(item.getItem());
//                inv.updateSlot(index, item);
//            }
//        }
//
//        ((Player) e.getWhoClicked()).updateInventory();
//
////
//            Inventory inv = e.getClickedInventory();
//            Inventory opposite = view.getTopInventory() == inv ? view.getBottomInventory() : view.getTopInventory();
//            Participant p = plugin.getPlayers().get(e.getWhoClicked().getUniqueId());
//
//            ItemStack chestItem = null;
//            ItemStack playerItem = null;
//
//            ItemStack item1 = switch (e.getAction()) {
//                case MOVE_TO_OTHER_INVENTORY -> e.getCurrentItem();
//                case PLACE_ALL, SWAP_WITH_CURSOR -> e.getCursor();
//                default -> null;
//            };
//            if (item1 == null) return;
//            if (ShopItems.isTool(item1.getType())) return;
//            ItemStack item2 = findTool(opposite, ShopItems.getList(ShopItems.TOOLS, ShopItem.getShopItem(item1.getItemMeta().getDisplayName())));
//
//            switch (e.getAction()) {
//                case MOVE_TO_OTHER_INVENTORY -> {
//                    if (inv.getHolder() instanceof Player) {
//                        chestItem = item1;
//                        playerItem = item2;
//                    } else {
//                        playerItem = item1;
//                        chestItem = item2;
//                    }
//                }
//                case PLACE_ALL, SWAP_WITH_CURSOR -> {
//                    if (inv.getHolder() instanceof Player) {
//                        chestItem = item2;
//                        playerItem = item1;
//                    } else {
//                        playerItem = item2;
//                        chestItem = item1;
//                    }
//                }
//            }
//        }


        if(e.getInventory().getHolder() instanceof IGUI gui){
            e.setCancelled(true);
            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getClickedInventory());
        }
        if(e.getInventory().getHolder() instanceof HumanEntity){
            switch (e.getRawSlot()) {
                case 0, 1, 2, 3, 4, 5, 6, 7, 8-> e.setCancelled(true);
            }
        }
    }

//    public ItemStack findTool(Inventory inv, ItemStack i) {
//        ShopItem shopItem = ShopItem.getShopItem(i.getItemMeta().getDisplayName());
//        List<ShopItem> list = ShopItems.getList(ShopItems.TOOLS, shopItem);
//
//        for (ShopItem item : list) {
//            int index = inv.first(item.getMaterial());
//            if (index == -1) continue;
//            return inv.getItem(index);
//        }
//        return null;
//    }

//    public void swapItem(Inventory inv, ItemStack item) {
//
//    }
}
