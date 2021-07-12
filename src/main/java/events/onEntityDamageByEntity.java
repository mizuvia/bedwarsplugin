package events;

import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onEntityDamageByEntity extends SimpleListener implements Listener, EventExecutor {
    public onEntityDamageByEntity(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if(!(event instanceof EntityDamageByEntityEvent)) return;

        EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;

        if(this.getPlugin().isLoading()) {
            e.setCancelled(true);
            return;
        }
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player){
                if(this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getColor().equals(this.getPlugin().getPlayers().get(e.getDamager().getName()).getTeam().getColor())) e.setCancelled(true);
                if(!e.getEntity().getUniqueId().equals(e.getDamager().getUniqueId())){
                    this.getPlugin().getGame().getPlayersDamagers().remove(e.getEntity().getName());
                    this.getPlugin().getGame().getPlayersDamagers().put(e.getEntity().getName(), e.getDamager().getName());
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> this.getPlugin().getGame().getPlayersDamagers().remove(e.getEntity().getName()), 400);
                }
            }
        }
        if(e.getDamager() instanceof IronGolem){
            e.setDamage(8.0);
        }
        if(e.getDamager() instanceof Player){
            if(e.getEntity() instanceof IronGolem){
                if(this.getPlugin().getPlayers().get(e.getDamager().getName()).getTeam().getIronGolem() == null) return;
                if(this.getPlugin().getPlayers().get(e.getDamager().getName()).getTeam().getIronGolem().equals(e.getEntity())) e.setCancelled(true);
            }
        }
    }
}
