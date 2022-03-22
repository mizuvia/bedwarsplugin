package events;

import main.Plugin;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPrepareItemCraft extends SimpleListener implements Listener, EventExecutor {

    public onPrepareItemCraft(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PrepareItemCraftEvent e = (PrepareItemCraftEvent) event;

        //e.getInventory().setResult(new ItemStack(Material.AIR));
    }
}
