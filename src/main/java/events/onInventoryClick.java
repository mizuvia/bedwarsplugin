package events;

import inventories.IGUI;
import main.Plugin;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class onInventoryClick extends SimpleListener implements Listener, EventExecutor {

    public onInventoryClick(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        InventoryClickEvent e = (InventoryClickEvent) event;

        if(e.getClickedInventory() == null) return;
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
}
