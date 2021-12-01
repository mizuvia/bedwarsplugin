package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onEntityTarget extends SimpleListener implements Listener, EventExecutor {
    public onEntityTarget(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        EntityTargetEvent e = (EntityTargetEvent) event;

        e.setCancelled(true);
    }
}
