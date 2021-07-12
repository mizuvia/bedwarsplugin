package events;

import game.Team;
import main.Plugin;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onEntityDeath extends SimpleListener implements Listener, EventExecutor {

    public onEntityDeath(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        EntityDeathEvent e = (EntityDeathEvent) event;

        if(e.getEntity() instanceof IronGolem){
            for(Team team : this.getPlugin().getTeams().values()){
                if(e.getEntity().getCustomName().matches("(.*)" + team.getName())){
                    team.setIronGolem(null);
                }
            }
            e.getDrops().clear();
        }
    }
}
