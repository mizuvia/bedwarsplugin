package events;

import game.Participant;
import inventories.ShopItem;
import inventories.ShopItems;
import inventories.SimpleInventory;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPlayerDropItem extends SimpleListener implements Listener, EventExecutor {

    public onPlayerDropItem(Plugin plugin) {
        super(plugin);
    }

    private PlayerDropItemEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.e = (PlayerDropItemEvent) event;

        if(plugin.isLoading()) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getItemDrop().getItemStack();

        Material droppedType = e.getItemDrop().getItemStack().getType();
        String name = item.getItemMeta().getDisplayName();
        ShopItem shopItem = ShopItem.getShopItem(name);

        if (ShopItems.TOOLS.values().stream().noneMatch(list -> list.contains(shopItem))) return;

        Participant p = plugin.getPlayers().get(e.getPlayer().getUniqueId());
        SimpleInventory inv = p.getShopInventory(ShopItem.TOOLS);

        int index = ShopItems.TOOLS_ITEMS_INDEXES.get(droppedType);
        ItemStack i = switch (shopItem) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, DIAMOND_AXE -> ShopItem.WOODEN_AXE.getItem();
            case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, DIAMOND_PICKAXE -> ShopItem.WOODEN_PICKAXE.getItem();
            default -> shopItem.getItem();
        };

        inv.setItem(index, i);
    }
}
