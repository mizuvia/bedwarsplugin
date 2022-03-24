package events;

import game.Participant;
import inventories.IGUI;
import inventories.ShopItem;
import inventories.ShopItems;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.PlayerInv;

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

        Logger.getLogger("").info(e.getWhoClicked().getName() + " совершило действие " + e.getAction().name() + " над инвентарём " + e.getClickedInventory().getHolder().toString());
        Logger.getLogger("").info("По слоту: " + e.getSlot() + " (" + e.getRawSlot() + " по сырому слоту)");

        InventoryView view = e.getView();
        Player player = ((Player) e.getWhoClicked());
        Participant p = plugin.getPlayers().get(player.getUniqueId());

        if (!p.canInteractInInventory) {
            e.setCancelled(true);
            return;
        }

        InventoryHolder holder = e.getClickedInventory().getHolder();

        if (!(view.getTopInventory().getHolder() instanceof IGUI)){

            List<List<ShopItem>> twoTools = List.of(
                List.of( ShopItem.WOODEN_AXE, ShopItem.STONE_AXE,
                        ShopItem.IRON_AXE, ShopItem.DIAMOND_AXE),
                List.of( ShopItem.WOODEN_PICKAXE, ShopItem.STONE_PICKAXE,
                        ShopItem.IRON_PICKAXE, ShopItem.DIAMOND_PICKAXE)
            );

            for (List<ShopItem> tools : twoTools) {

                int toolIndex = -1;
                ShopItem tool = null;

                for (ShopItem item : tools) {
                    if (player.getInventory().first(item.getMaterial()) != -1) {
                        tool = item;
                        toolIndex = player.getInventory().first(item.getMaterial());
                        break;
                    } else if (PlayerInv.isInOffHand(player.getInventory(), item)) {
                        tool = item;
                        break;
                    }
                }

                int finalToolIndex = toolIndex;
                ShopItem finalTool = tool;
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        p.canInteractInInventory = false;
                        ShopItem givenTool = null;
                        int givenToolIndex = -1;
                        ShopItem inOffHand = null;
                        if (finalToolIndex == -1 && finalTool != null) inOffHand = finalTool;
                        fir:
                        for (ShopItem item : tools) {
                            for (int i : player.getInventory().all(item.getMaterial()).keySet()) {
                                if (inOffHand != null || (finalToolIndex != i)) {
                                    givenToolIndex = i;
                                    givenTool = item;
                                    break fir;
                                }
                            }
                        }
                        Logger.getLogger("").info("GivenTool: " + (givenTool == null ? "null" : givenTool.getName()));
                        Logger.getLogger("").info("FinalTool: " + (finalTool == null ? "null" : finalTool.getName()));
                        Logger.getLogger("").info("InOffHand: " + (inOffHand == null ? "null" : inOffHand.getName()));
                        if (givenTool != null) {
                            if (inOffHand != null) {
                                view.getTopInventory().addItem(player.getInventory().getItemInOffHand());
                                player.getInventory().setItemInOffHand(null);
                            } else {
                                if (finalTool != null) {
                                    view.getTopInventory().addItem(player.getInventory().getItem(finalToolIndex));
                                    player.getInventory().setItem(finalToolIndex, null);
                                }
                            }
                            p.giveItem(givenTool.getItem(), givenToolIndex, false);
                            p.getShopInventory(ShopItem.TOOLS).updateSlot(ShopItems.getIndex(ShopItems.TOOLS, givenTool), givenTool);
                        } else {
                            if (finalTool != null) {
                                ItemStack finalItem = player.getInventory().getItem(finalToolIndex);
                                if (finalItem == null) {
                                    p.getShopInventory(ShopItem.TOOLS).updateSlot(ShopItems.getIndex(ShopItems.TOOLS, finalTool), null);
                                } else {
                                    givenTool = ShopItem.getShopItem(finalItem.getItemMeta().getDisplayName());
                                    p.giveItem(finalItem, finalToolIndex, false);
                                    p.getShopInventory(ShopItem.TOOLS).updateSlot(ShopItems.getIndex(ShopItems.TOOLS, finalTool), givenTool);
                                }
                            }
                        }
                        p.canInteractInInventory = true;
                        player.updateInventory();
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                });
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
}
