package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPlayerFish extends SimpleListener implements Listener, EventExecutor {

    public onPlayerFish(Plugin plugin) {
        super(plugin);
    }

    private PlayerFishEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        this.e = (PlayerFishEvent) event;

        e.setCancelled(true);
    }
}
