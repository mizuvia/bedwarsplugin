package events;

import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onFoodLevelChange extends SimpleListener implements Listener, EventExecutor {

    public onFoodLevelChange(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        FoodLevelChangeEvent e = (FoodLevelChangeEvent) event;

        e.setCancelled(false);
        e.setFoodLevel(20);
    }
}
