package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onBlockSpread extends SimpleListener implements Listener, EventExecutor {

    public onBlockSpread(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockSpreadEvent e = (BlockSpreadEvent) event;

        e.setCancelled(false);
    }
}
