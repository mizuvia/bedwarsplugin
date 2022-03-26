package events;

import game.Game;
import game.Participant;
import game.Team;
import inventories.ShopItem;
import inventories.ShopItems;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import util.MineColor;
import util.PlayerInv;
import util.Utils;
import util.WorldManager;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class onPlayerInteract extends SimpleListener implements Listener, EventExecutor {

    public onPlayerInteract(Plugin plugin) {
        super(plugin);
    }

    private PlayerInteractEvent e;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException  {
        this.e = (PlayerInteractEvent) event;

        if(e.getAction().equals(Action.PHYSICAL)) return;
        Player p = e.getPlayer();
        Participant par = getPlugin().getPlayers().get(p.getUniqueId());

        List<Material> interactiveItems = List.of(
                Material.RED_BED, Material.DARK_OAK_DOOR,
                Material.GHAST_SPAWN_EGG, Material.GUNPOWDER,
                Material.FIRE_CHARGE, Material.MILK_BUCKET
        );

        if(interactiveItems.contains(e.getMaterial())) {
            e.setCancelled(true);
        }

        switch (e.getMaterial()) {
            case RED_BED -> {
                if(!Utils.isRightClick(e.getAction())) return;
                p.openInventory(this.getPlugin().getTeamSelectionInventory());
            }
            case DARK_OAK_DOOR -> Utils.connectToHub(p);
            case GHAST_SPAWN_EGG -> {
                if(!Utils.isRightClick(e.getAction()) && e.getAction() == Action.RIGHT_CLICK_AIR) return;
                if(par.getTeam().getIronGolem() != null && !par.getTeam().getIronGolem().isDead()) {
                    p.sendMessage(MineColor.RED + "Нельзя создать больше одного голема на команду!");
                    return;
                }

                Location blockLoc = e.getClickedBlock().getLocation();
                Location golemLoc = new Location(blockLoc.getWorld(), blockLoc.getX() + 0.5, blockLoc.getY() + 1, blockLoc.getZ() + 0.5);

                double distance = WorldManager.getDistance(golemLoc, par.getTeam().getSpawnLocation());
                if (distance > 25) {
                    p.sendMessage(MineColor.RED + "Вы можете создавать голема только в пределах своей базы!");
                    return;
                }

                IronGolem golem = (IronGolem) Bukkit.getWorld("world").spawnEntity(new Location(e.getClickedBlock().getLocation().getWorld(), e.getClickedBlock().getLocation().getX() + 0.5, e.getClickedBlock().getLocation().getY() + 1, e.getClickedBlock().getLocation().getZ() + 0.5), EntityType.IRON_GOLEM);
                golem.setCustomName(PlayerManager.getCodeColor(par) + "§lСтраж команды " + par.getTeam().getName());
                golem.setCustomNameVisible(true);
                Team parTeam = par.getTeam();
                Game game = plugin.getGame();
                new BukkitRunnable() {
                    public void run() {
                    if (game != plugin.getGame()) {
                        cancel();
                        return;
                    }
                    if (golem.isDead()) {
                        parTeam.setIronGolem(null);
                        cancel();
                        return;
                    }
                    LivingEntity target = golem.getTarget();
                    if (target != null)
                        if (WorldManager.getDistance(target.getLocation(), golem.getLocation()) > 15 || target.isDead())
                            golem.setTarget(null);
                    for (Team team : plugin.getTeams().values()) {
                        if (parTeam == team) continue;
                        if (team.getIronGolem() == null || team.getIronGolem().isDead()) continue;
                        IronGolem ironGolem = team.getIronGolem();
                        if (WorldManager.getDistance(ironGolem.getLocation(), golem.getLocation()) > 6) continue;
                        if (target == null || target instanceof Player) golem.setTarget(ironGolem);
                    }
                    }
                }.runTaskTimer(plugin, 5, 5);
                Bukkit.getScheduler().runTaskLater(plugin, golem::remove, 20 * 240);

                par.getTeam().setIronGolem(golem);
                consumeItem(ShopItem.GOLEM);
            }
            case GUNPOWDER -> {
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
                        PlayerInv.removeShopItem(e.getPlayer().getInventory(), ShopItem.TELEPORT, 1);
                        consumeItem(ShopItem.TELEPORT);
                        par.setTeleporting(false);
                    }
                    Bukkit.getServer().getScheduler().cancelTask(id);
                }, 79L);
            }
            case FIRE_CHARGE -> {
                if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
                    Location loc = p.getLocation();
                    Location newLoc = new Location(loc.getWorld(), loc.getX() - (1 * Math.sin(Math.toRadians(loc.getYaw()))), loc.getY() + 1.0, loc.getZ() + (1 * Math.cos(Math.toRadians(loc.getYaw()))), loc.getYaw(), loc.getPitch());
                    Fireball fireball = (Fireball) Bukkit.getWorld("world").spawnEntity(newLoc, EntityType.FIREBALL);
                    fireball.setShooter(p);
                    consumeItem(ShopItem.FIRE_CHARGE);
                    Bukkit.getScheduler().runTaskLater(plugin, fireball::remove, 400);
                }
            }
            case WATER_BUCKET -> {
                if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
                Bukkit.getScheduler().runTask(plugin, () -> PlayerInv.removeMaterial(p.getInventory(), Material.BUCKET, 1));
            }
            case MILK_BUCKET -> {
                if(!Utils.isRightClick(e.getAction())) return;

                if(par.isUnderMilk()) return;
                par.setUnderMilk(true);

                consumeItem(ShopItem.MILK);
                p.sendMessage("§cТеперь ловушки не действуют на вас в течении 60 секунд!");

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> par.setUnderMilk(false), 1200L);
            }
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType().name().endsWith("_BED")) {
            e.setCancelled(true);
            Block rel = e.getClickedBlock().getRelative(e.getBlockFace());
            if (rel.getType() == Material.AIR && e.getItem() != null){
                ShopItem item = ShopItem.getShopItem(e.getItem().getItemMeta().getDisplayName());
                int index = ShopItems.getIndex(ShopItems.BLOCKS, item);
                if (index != -1) rel.setType(e.getItem().getType());
            }
        }
    }

    private void consumeItem(ShopItem item){
        PlayerInv.removeShopItem(e.getPlayer().getInventory(), item, 1);
    }
}
