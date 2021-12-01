package events;

import main.Plugin;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onExplosionPrime extends SimpleListener implements Listener, EventExecutor {
    public onExplosionPrime(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        ExplosionPrimeEvent e = (ExplosionPrimeEvent) event;


        if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
            e.setRadius(3F);
            e.setFire(false);
        }
        if (e.getEntityType().equals(EntityType.FIREBALL)) {
            e.setRadius(2F);
            e.setFire(false);
        }
    }
}
