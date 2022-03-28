package events;

import game.Participant;
import game.PlayerKiller;
import main.Plugin;
import org.bukkit.entity.*;
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
        if(!(event instanceof EntityDamageByEntityEvent e)) return;

        if(this.getPlugin().isLoading()) {
            e.setCancelled(true);
            return;
        }

        if (e.getDamager() instanceof IronGolem) e.setDamage(8.0);

        if (e.getEntity() instanceof Player pl) {
            Participant player = plugin.getPlayer(pl);
            Participant damager;
            DamageCause cause = null;

            if (e.getDamager() instanceof TNTPrimed tnt) {

                damager = plugin.getPlayer(tnt.getSource());
                cause = DamageCause.TNT;

            } else if (e.getDamager() instanceof Projectile proj) {

                damager = plugin.getPlayer((Entity) proj.getShooter());
                if (proj instanceof Arrow) cause = DamageCause.ARROW;
                else if (proj instanceof Fireball) cause = DamageCause.FIREBALL;

            } else if (e.getDamager() instanceof IronGolem golem) {

                damager = null;
                cause = DamageCause.IRONGOLEM;
                plugin.getTeams().stream()
                        .filter(t -> t.getIronGolem() == golem)
                        .findFirst()
                        .ifPresent(golemTeam ->
                            player.getLastDamager().put("големом команды " + golemTeam.getName())
                        );

            } else if (e.getDamager() instanceof Player bukkitDamager) {

                damager = plugin.getPlayer(bukkitDamager);
                cause = DamageCause.PLAYER;
                if (damager.isInvisible()) damager.showArmor();

            } else return;

            prepareToKill(e, player, damager, cause);

        } else if (e.getEntity() instanceof IronGolem golem) {
            if (!(e.getDamager() instanceof Player bukkitDamager)) return;

            Participant damager = plugin.getPlayer(bukkitDamager);
            if (golem == damager.getTeam().getIronGolem()) e.setCancelled(true);
        }
    }

    private void prepareToKill(EntityDamageByEntityEvent e, Participant player, Participant damager, DamageCause cause) {
        if (damager != player) {
            if (damager.getTeam() == player.getTeam()) {
                switch (cause) {
                    case TNT, FIREBALL -> e.setDamage(0);
                    case PLAYER, ARROW -> e.setCancelled(true);
                }
                return;
            }
        } else e.setCancelled(true);
        if (damager != null) player.getLastDamager().put(damager.getPlayer().getUniqueId().toString());
        if (e.getFinalDamage() >= player.getPlayer().getHealth()) {
            e.setCancelled(true);
            new PlayerKiller(player, e.getCause()).killInGame();
        }
    }

    private enum DamageCause {
        TNT,
        FIREBALL,
        ARROW,
        PLAYER,
        IRONGOLEM
    }
}
