package events;

import game.Participant;
import game.PlayerKiller;
import game.Team;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class onEntityDamageByEntity extends SimpleListener implements Listener, EventExecutor {
    public onEntityDamageByEntity(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        if(!(event instanceof EntityDamageByEntityEvent e)) return;

        Participant player = null;
        if (e.getEntity() instanceof Player)
            player = getPlugin().getPlayers().get(e.getEntity().getUniqueId());

        Participant damager = null;
        if (e.getDamager() instanceof Player)
            damager = getPlugin().getPlayers().get(e.getDamager().getUniqueId());

        if(this.getPlugin().isLoading()) {
            e.setCancelled(true);
            return;
        }
        if(e.getEntity() instanceof Player){
            if(e.getDamager() instanceof Player bukkitDamager){
                if(player.getTeam().equals(damager.getTeam())) e.setCancelled(true);
                if(!e.getEntity().getUniqueId().equals(bukkitDamager.getUniqueId())){
                	player.getLastDamager().put(bukkitDamager.getUniqueId().toString());
                }
            }
        }

        if (e.getDamager() instanceof TNTPrimed tnt) {
            damager = getPlugin().getPlayers().get(tnt.getSource().getUniqueId());
            if (detectTeammate(e, player, damager)) return;
        }

        if (e.getDamager() instanceof Fireball fireball) {
            damager = getPlugin().getPlayers().get(((Player) fireball.getShooter()).getUniqueId());
            if (detectTeammate(e, player, damager)) return;
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

        if(e.getDamager() instanceof Player bukkitDamager){
        	Participant p = getPlugin().getPlayers().get(bukkitDamager.getUniqueId());
        	if (p.isInvisible()) {
        		p.showArmor();
        	}
            if(e.getEntity() instanceof IronGolem){
                IronGolem golem = damager.getTeam().getIronGolem();
                if(golem != null)
                    if(golem.equals(e.getEntity()))
                        e.setCancelled(true);
            }
        }
    }

    private boolean detectTeammate(EntityDamageByEntityEvent e, Participant player, Participant damager) {
        if (e.getEntity() instanceof Player) {
            if (damager != player) {
                if (damager.getTeam() == player.getTeam()) {
                    e.setDamage(0);
                    return true;
                }
            }
            if (e.getFinalDamage() >= ((Player) e.getEntity()).getHealth()) {
                new PlayerKiller(player, e.getCause()).killInGame();
            }
        }
        return false;
    }
}
