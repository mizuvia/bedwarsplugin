package events;

import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
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

        if(e.getAction().equals(Action.PHYSICAL)) return;

        if (e.getMaterial().name().equals("RED_BED")) {
            e.setCancelled(true);

            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            e.getPlayer().openInventory(this.getPlugin().getTeamSelectionInventory());
        }
        if (e.getMaterial().name().equals("DARK_OAK_DOOR")) {
            e.setCancelled(true);

            Utils.connectToHub(e.getPlayer());
        }
        if(e.getMaterial().equals(Material.GHAST_SPAWN_EGG)){
            e.setCancelled(true);

            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            if(this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getIronGolem() != null) return;

            IronGolem golem = (IronGolem) Bukkit.getWorld("world").spawnEntity(new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getX() + 0.5, e.getClickedBlock().getLocation().getY() + 1, e.getClickedBlock().getLocation().getZ() + 0.5), EntityType.IRON_GOLEM);
            golem.setCustomName(PlayerManager.getCodeColor(this.getPlugin().getPlayers().get(e.getPlayer().getName())) + "§lСтраж команды " + this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getName());
            golem.setCustomNameVisible(true);

            this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().setIronGolem(golem);

            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getItem().getAmount() - 1);
        }
        if(e.getMaterial().equals(Material.GUNPOWDER)){
            e.setCancelled(true);

            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            if(this.getPlugin().getPlayers().get(e.getPlayer().getName()).isTeleporting()) return;

            this.getPlugin().getPlayers().get(e.getPlayer().getName()).setTeleporting(true);

            e.getPlayer().sendMessage("§eНе двигайтесь! Телепортация через:");

            final boolean[] shouldTeleport = {true};

            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.getPlugin(), new Runnable() {
                public double oldLoc = 0;
                public int time = 79;

                @Override
                public void run() {
                    double location = e.getPlayer().getLocation().getX() + e.getPlayer().getLocation().getY() + e.getPlayer().getLocation().getZ();
                    if(shouldTeleport[0]) {
                        if(oldLoc != 0 && location != oldLoc){
                            e.getPlayer().sendMessage("§cТелепортация отменена!");
                            shouldTeleport[0] = false;
                            getPlugin().getPlayers().get(e.getPlayer().getName()).setTeleporting(false);
                        } else if(time % 20 == 0) e.getPlayer().sendMessage("§e" + time / 20);
                    }
                    oldLoc = location;
                    time--;
                }
            }, 0L, 1L);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                if(shouldTeleport[0]) {
                    e.getPlayer().teleport(getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getSpawnLocation());
                    e.getPlayer().getInventory().getItem(e.getPlayer().getInventory().first(Material.GUNPOWDER)).setAmount(e.getItem().getAmount() - 1);
                    getPlugin().getPlayers().get(e.getPlayer().getName()).setTeleporting(false);
                }
                Bukkit.getServer().getScheduler().cancelTask(id);
            }, 79L);

        }
        if(e.getMaterial().equals(Material.FIRE_CHARGE)){
            e.setCancelled(true);

            if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                Location loc = e.getPlayer().getLocation();
                Location newLoc = new Location(loc.getWorld(), loc.getX() - (1 * Math.sin(Math.toRadians(loc.getYaw()))), loc.getY() + 1.0, loc.getZ() + (1 * Math.cos(Math.toRadians(loc.getYaw()))), loc.getYaw(), loc.getPitch());
                Bukkit.getWorld("world").spawnEntity(newLoc, EntityType.FIREBALL);
                e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getItem().getAmount() - 1);
            }
        }
        if(e.getMaterial().equals(Material.MILK_BUCKET)){
            e.setCancelled(true);

            if(!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

            if(this.getPlugin().getPlayers().get(e.getPlayer().getName()).isUnderMilk()) return;

            this.getPlugin().getPlayers().get(e.getPlayer().getName()).setUnderMilk(true);

            e.getItem().setAmount(e.getItem().getAmount() - 1);
            e.getPlayer().sendMessage("§cТеперь ловушки не действуют на вас в течении 60 секунд!");

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> getPlugin().getPlayers().get(e.getPlayer().getName()).setUnderMilk(false), 1200L);
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().endsWith("_BED")) {
        	e.setCancelled(true);
        }
    }
}
