package events;

import game.Participant;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.Utils;

public class onPlayerInteract extends SimpleListener implements Listener, EventExecutor {

    public onPlayerInteract(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException  {
        PlayerInteractEvent e = (PlayerInteractEvent) event;

        Player p = e.getPlayer();
        Participant par = getPlugin().getPlayers().get(p.getUniqueId());

        if(e.getAction().equals(Action.PHYSICAL)) return;

        if (e.getMaterial().equals(Material.RED_BED)) {
            e.setCancelled(true);

            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            p.openInventory(this.getPlugin().getTeamSelectionInventory());
        }
        if (e.getMaterial().equals(Material.DARK_OAK_DOOR)) {
            e.setCancelled(true);

            Utils.connectToHub(p);
        }
        if(e.getMaterial().equals(Material.GHAST_SPAWN_EGG)){
            e.setCancelled(true);

            if(!Utils.isRightClick(e.getAction())) return;

            if(par.getTeam().getIronGolem() != null) return;

            IronGolem golem = (IronGolem) Bukkit.getWorld("world").spawnEntity(new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getX() + 0.5, e.getClickedBlock().getLocation().getY() + 1, e.getClickedBlock().getLocation().getZ() + 0.5), EntityType.IRON_GOLEM);
            golem.setCustomName(PlayerManager.getCodeColor(par) + "§lСтраж команды " + par.getTeam().getName());
            golem.setCustomNameVisible(true);

            par.getTeam().setIronGolem(golem);

            e.getItem().setAmount(e.getItem().getAmount() - 1);
        }
        if(e.getMaterial().equals(Material.GUNPOWDER)){
            e.setCancelled(true);

            if(!Utils.isRightClick(e.getAction())) return;

            if(par.isTeleporting()) return;
            par.setTeleporting(true);

            p.sendMessage("§eНе двигайтесь! Телепортация через:");

            final boolean[] shouldTeleport = {true};

            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), new Runnable() {
                public double oldLoc = 0;
                public int time = 79;

                @Override
                public void run() {
                    double location = p.getLocation().getX() + p.getLocation().getY() + p.getLocation().getZ();
                    if(shouldTeleport[0]) {
                        if(oldLoc != 0 && location != oldLoc){
                            p.sendMessage("§cТелепортация отменена!");
                            shouldTeleport[0] = false;
                            par.setTeleporting(false);
                        } else if(time % 20 == 0) p.sendMessage("§e" + time / 20);
                    }
                    oldLoc = location;
                    time--;
                }
            }, 0L, 1L);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                if(shouldTeleport[0]) {
                    p.teleport(par.getTeam().getSpawnLocation());
                    e.getItem().setAmount(e.getItem().getAmount() - 1);
                    par.setTeleporting(false);
                }
                Bukkit.getServer().getScheduler().cancelTask(id);
            }, 79L);

        }
        if(e.getMaterial().equals(Material.FIRE_CHARGE)){
            e.setCancelled(true);

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                Location loc = p.getLocation();
                Location newLoc = new Location(loc.getWorld(), loc.getX() - (1 * Math.sin(Math.toRadians(loc.getYaw()))), loc.getY() + 1.0, loc.getZ() + (1 * Math.cos(Math.toRadians(loc.getYaw()))), loc.getYaw(), loc.getPitch());
                Bukkit.getWorld("world").spawnEntity(newLoc, EntityType.FIREBALL);
                p.getInventory().getItemInMainHand().setAmount(e.getItem().getAmount() - 1);
            }
        }
        if(e.getMaterial().equals(Material.MILK_BUCKET)){
            e.setCancelled(true);

            if(!Utils.isRightClick(e.getAction())) return;

            if(par.isUnderMilk()) return;

            par.setUnderMilk(true);

            e.getItem().setAmount(e.getItem().getAmount() - 1);
            p.sendMessage("§cТеперь ловушки не действуют на вас в течении 60 секунд!");

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> par.setUnderMilk(false), 1200L);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().endsWith("_BED")) {
        	e.setCancelled(true);
        }
    }
}
