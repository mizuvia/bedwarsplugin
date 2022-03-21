package events;

import game.Participant;
import inventories.ShopItem;
import inventories.ShopItems;
import inventories.SimpleInventory;
import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class onEntityPickupItem extends SimpleListener implements Listener, EventExecutor {

    public onEntityPickupItem(Plugin plugin) {
        super(plugin);
    }

    private EntityPickupItemEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        this.e = (EntityPickupItemEvent) event;

        ItemStack item = e.getItem().getItemStack();

        String name = item.getItemMeta().getDisplayName();
        ShopItem shopItem = ShopItem.getShopItem(name);

        if (ShopItems.TOOLS.values().stream().noneMatch(list -> list.contains(shopItem))) return;

        Participant p = plugin.getPlayers().get(e.getEntity().getUniqueId());
        SimpleInventory inv = p.getShopInventory(ShopItem.TOOLS);
        int index = ShopItems.getIndex(ShopItems.TOOLS, shopItem);
        inv.updateSlot(index, shopItem);
    }
}
