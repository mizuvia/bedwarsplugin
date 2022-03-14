package events;

import game.Participant;
import inventories.ShopItem;
import inventories.ShopItems;
import inventories.ToolsInventory;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class onEntityPickupItem extends SimpleListener implements Listener, EventExecutor {

    public onEntityPickupItem(Plugin plugin) {
        super(plugin);
    }

    private EntityPickupItemEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        this.e = (EntityPickupItemEvent) event;

        Material pickedType = e.getItem().getItemStack().getType();

        if (!ShopItems.TOOLS_ITEMS_INDEXES.containsKey(pickedType)) return;

        Participant p = plugin.getPlayers().get(e.getEntity().getUniqueId());
        ToolsInventory inv = p.getToolsInventory();

        int index = ShopItems.TOOLS_ITEMS_INDEXES.get(pickedType);
        ItemStack item = switch (pickedType) {
            case SHEARS, FISHING_ROD, DIAMOND_AXE, DIAMOND_PICKAXE -> inv.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0];
            default -> ShopItem.valueOf(ShopItems.NEXT_TOOLS_TIER.get(pickedType).name()).getItem();
        };

        inv.setItem(index, item);
    }
}
