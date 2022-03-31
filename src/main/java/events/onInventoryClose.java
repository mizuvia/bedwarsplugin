package events;

import inventories.ChangeItemGUI;
import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onInventoryClose extends SimpleListener implements Listener, EventExecutor {

    public onInventoryClose(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        InventoryCloseEvent e = (InventoryCloseEvent) event;

        if (e.getInventory().getHolder() instanceof ChangeItemGUI)
            ChangeItemGUI.UPDATING_SLOTS.remove(e.getPlayer().getUniqueId());
    }
}
