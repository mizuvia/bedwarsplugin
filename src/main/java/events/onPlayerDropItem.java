package events;

import game.Participant;
import inventories.ShopItem;
import inventories.ShopItems;
import inventories.ToolsInventory;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class onPlayerDropItem extends SimpleListener implements Listener, EventExecutor {

    public onPlayerDropItem(Plugin plugin) {
        super(plugin);
    }

    private PlayerDropItemEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.e = (PlayerDropItemEvent) event;

        Material droppedType = e.getItemDrop().getItemStack().getType();

        if (!ShopItems.TOOLS_ITEMS_INDEXES.containsKey(droppedType)) return;

        Participant p = plugin.getPlayers().get(e.getPlayer().getUniqueId());
        ToolsInventory inv = p.getToolsInventory();

        int index = ShopItems.TOOLS_ITEMS_INDEXES.get(droppedType);
        ItemStack item = switch (droppedType) {
            case WOODEN_AXE, STONE_AXE, IRON_AXE, DIAMOND_AXE -> ShopItem.WOODEN_AXE.getItem();
            case WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, DIAMOND_PICKAXE -> ShopItem.WOODEN_PICKAXE.getItem();
            default -> ShopItem.valueOf(droppedType.name()).getItem();
        };

        inv.setItem(index, item);
    }
}
