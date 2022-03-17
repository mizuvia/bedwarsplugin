package events;

import main.Plugin;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onBlockPhysics extends SimpleListener implements Listener, EventExecutor {

    public onBlockPhysics(Plugin plugin) { super(plugin); }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockPhysicsEvent e = (BlockPhysicsEvent) event;

        if(!(e.getBlock().getBlockData() instanceof Levelled data)) return;

        if (data.getLevel() == data.getMaximumLevel()) {
            e.setCancelled(true);
            plugin.getLogger().info("water moreify event canceled!");
        }

    }
}
