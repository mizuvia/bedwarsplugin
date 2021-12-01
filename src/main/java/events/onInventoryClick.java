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

public class onInventoryClick extends SimpleListener implements Listener, EventExecutor {

    public onInventoryClick(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        InventoryClickEvent e = (InventoryClickEvent) event;

        if(e.getClickedInventory() == null) return;
        if(e.getInventory().getHolder() instanceof IGUI){
            e.setCancelled(true);
            IGUI gui = (IGUI) e.getInventory().getHolder();
            gui.onGUIClick((Player) e.getWhoClicked(), e.getRawSlot(), e.getCurrentItem());
        }
        if(e.getInventory().getHolder() instanceof HumanEntity){
            switch (e.getSlot()) {
                case 36, 37, 38, 39 -> e.setCancelled(true);
            }
        }
    }
}
