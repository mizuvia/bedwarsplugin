package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPlayerDeath extends SimpleListener implements Listener, EventExecutor {
    public onPlayerDeath(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException  {
        PlayerDeathEvent e = (PlayerDeathEvent) event;

        e.setDeathMessage(null);
    }
}
