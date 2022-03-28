package events;

import game.Participant;
import main.Plugin;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onProjectileHit extends SimpleListener implements Listener, EventExecutor {

    public onProjectileHit(Plugin plugin) {
        super(plugin);
    }
    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        ProjectileHitEvent e = (ProjectileHitEvent) event;


//        if (e.getHitEntity() == null || !(e.getEntity() instanceof Arrow arrow)) return;
//        Player bukkitShooter = (Player) arrow.getShooter();
//        Participant shooter = plugin.getPlayer(bukkitShooter);
//
//        if (e.getHitEntity() instanceof IronGolem golem) {
//            if (shooter.getTeam().getIronGolem() == golem) e.setCancelled(true);
//        }
//
//        if (e.getHitEntity() instanceof Player bukkitPlayer) {
//            Participant player = plugin.getPlayer(bukkitPlayer);
//
//            if (player.getTeam() == shooter.getTeam()) {
//                e.setCancelled(true);
//                return;
//            }
//            player.getLastDamager().put(String.valueOf(bukkitShooter.getUniqueId()));
//        }
    }
}
