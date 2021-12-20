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

import game.Participant;
import game.Team;

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
                if(this.getPlugin().getPlayers().get(e.getEntity().getUniqueId()).getTeam().getColor().equals(this.getPlugin().getPlayers().get(e.getDamager().getUniqueId()).getTeam().getColor())) e.setCancelled(true);
                if(!e.getEntity().getUniqueId().equals(e.getDamager().getUniqueId())){
                	getPlugin().getPlayers().get(e.getEntity().getUniqueId()).getLastDamager().put(e.getDamager().getName());
                }
            }
        }
        if(e.getDamager() instanceof IronGolem){
            e.setDamage(8.0);
            if (e.getEntity() instanceof Player) {
                Team golemTeam = null;
                for (Team team : getPlugin().getTeams().values()) {
                	if (e.getDamager().equals(team.getIronGolem())) {
                		golemTeam = team;
                		break;
                	}
                }
                if (golemTeam != null) {
                	getPlugin().getPlayers().get(e.getEntity().getUniqueId()).getLastDamager().put("големом команды " + golemTeam.getName());
                }
            }
        }
        if(e.getDamager() instanceof Player){
        	Player damager = (Player) e.getDamager();
        	Participant p = getPlugin().getPlayers().get(damager.getUniqueId());
        	if (p.inInvis()) {
        		p.show();
        	}
            if(e.getEntity() instanceof IronGolem){
                if(this.getPlugin().getPlayers().get(e.getDamager().getUniqueId()).getTeam().getIronGolem() == null) return;
                if(this.getPlugin().getPlayers().get(e.getDamager().getUniqueId()).getTeam().getIronGolem().equals(e.getEntity())) e.setCancelled(true);
            }
        }
    }
}
