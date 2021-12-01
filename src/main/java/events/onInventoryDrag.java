package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onInventoryDrag extends SimpleListener implements Listener, EventExecutor {

    public onInventoryDrag(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        InventoryDragEvent e = (InventoryDragEvent) event;

        e.setCancelled(false);
    }
}
