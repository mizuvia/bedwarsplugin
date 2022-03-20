package events;

import inventories.IGUI;
import main.Plugin;
import org.bukkit.block.Chest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
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

        Logger.getLogger("").info(e.getRawSlot() + " " + e.getSlot() + " " + e.getClickedInventory().getType() + " " + e.getInventory().getType());

        if (e.getView().getTopInventory().getHolder() instanceof Chest ) Logger.getLogger("").info("wow3");
        if (e.getView().getBottomInventory().getHolder() instanceof Player ) Logger.getLogger("").info("wow4");
        else Logger.getLogger("").info("wow5");

        Logger.getLogger("").info(e.getInventory().getHolder().toString());

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
