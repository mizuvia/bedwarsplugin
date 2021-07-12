package events;

import main.Plugin;
import org.bukkit.block.Chest;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onInventoryOpen extends SimpleListener implements Listener, EventExecutor {
    public onInventoryOpen(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        InventoryOpenEvent e = (InventoryOpenEvent) event;

        if(e.getInventory().getHolder() instanceof Chest){
            if(!this.getPlugin().getGame().getChestsInventories().contains(e.getInventory()))this.getPlugin().getGame().getChestsInventories().add(e.getInventory());
        }
    }
}
