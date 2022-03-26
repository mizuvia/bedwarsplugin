package events;

import main.Plugin;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;


public class onBlockFromTo extends SimpleListener implements Listener, EventExecutor {
    public onBlockFromTo(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockFromToEvent e = (BlockFromToEvent) event;

        for (ItemStack i : e.getToBlock().getDrops()) {
            i.setType(Material.AIR);
        }
    }
}
