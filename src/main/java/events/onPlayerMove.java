package events;

import game.Participant;
import game.Team;
import main.Config;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import util.WorldManager;

import java.util.List;

public class onPlayerMove extends SimpleListener implements Listener, EventExecutor {

    public onPlayerMove(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerMoveEvent e = (PlayerMoveEvent) event;
        Player pl = e.getPlayer();

        if(!pl.getGameMode().equals(GameMode.SPECTATOR)){
            Participant p = plugin.getPlayers().get(pl.getUniqueId());
            if (!p.hasTeam()) return;
            Team t = p.getTeam();
            if (t.getTeamUpgrades().get("Healing") != 0) {
                Location playerLoc = pl.getLocation();
                Location teamLoc = t.getSpawnLocation();
                double distance = WorldManager.getDistance(playerLoc, teamLoc);
                if (distance <= 25 && playerLoc.getY() > 0 && pl.getActivePotionEffects().stream().noneMatch(pe -> pe.getType() == PotionEffectType.REGENERATION))
                    pl.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100000, 0));
                else pl.removePotionEffect(PotionEffectType.REGENERATION);
            }
            for (Team team : this.getPlugin().getTeams().values()) {
                Location playerLoc = pl.getLocation();

                if (team.getIronGolem() != null) {
                    IronGolem golem = team.getIronGolem();
                    if (t.getColor().equals(team.getColor()))
                        continue;
                    Location golemLoc = golem.getLocation();
                    double distance = Math.sqrt(Math.pow(playerLoc.getX() - golemLoc.getX(), 2.0) + Math.pow(playerLoc.getZ() - golemLoc.getZ(), 2.0));
                    if (distance <= 6) {
                        if (golem.getTarget() == null) golem.setTarget(pl);
                    }
                }

                if (team.getTraps().size() != 0) {
                    if (p.isUnderMilk()) return;
                    if (team.getColor().equals(t.getColor()))
                        continue;

                    double distance = Math.sqrt(Math.pow(playerLoc.getX() - team.getSpawnLocation().getX(), 2.0) + Math.pow(playerLoc.getZ() - team.getSpawnLocation().getZ(), 2.0));
                    if (distance <= 25) {
                        switch (team.getTraps().get(0)) {
                            case "Задержка":
                                pl.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0, false, false));
                                pl.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 0, false, false));
                                break;
                            case "Поддержка":
                                for (Participant participant : team.getTeammates()) {
                                    Location teammateLoc = participant.getPlayer().getLocation();
                                    Location teamLoc = team.getSpawnLocation();
                                    double tmdistance = Math.sqrt(Math.pow(teammateLoc.getX() - teamLoc.getX(), 2.0) + Math.pow(teammateLoc.getZ() - teamLoc.getZ(), 2.0));
                                    if (tmdistance <= 40) {
                                        participant.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0, false, false));
                                        participant.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 1, false, false));
                                    }
                                }
                                break;
                            case "Видимость":
                                pl.setGlowing(true);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> pl.setGlowing(false), 200);
                                break;
                            case "Усталость":
                                pl.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0, false, false));
                                break;
                        }

                        ItemStack item = team.getUpgradesInventory().getItem(16);
                        ItemMeta meta = item.getItemMeta();
                        List<String> lore = meta.getLore();

                        lore.remove("§b" + team.getTraps().get(0));
                        lore.add("§8[ПУСТО]");

                        team.getTraps().remove(0);

                        lore.remove(0);
                        lore.add(0, "§8Стоимость: §b" + (int) Math.pow(2, team.getTraps().size()) + " алмаз" + (team.getTraps().size() != 0 ? "а" : ""));

                        meta.setLore(lore);
                        item.setItemMeta(meta);

                        for (Participant participant : team.getTeammates()) {
                            participant.getPlayer().sendTitle("§4Ловушка активирована!", "§cВозвращайтесь на базу и защитите кровать.", 10, 70, 20);
                            participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 10F, 1F);
                        }
                    }
                }
            }
        }

        double distance = Math.sqrt(Math.pow(pl.getLocation().getX() - Config.getCenter().getX(), 2.0) + Math.pow(pl.getLocation().getZ() - Config.getCenter().getZ(), 2.0));
        double k = (pl.getLocation().getX() - Config.getCenter().getX()) / (pl.getLocation().getZ() - Config.getCenter().getZ());
        if(distance >= 300) {

            if(k < 0) k = -k;

            double x = (pl.getLocation().getX() - Config.getCenter().getX() < 0 ? -1 : 1) * (k * (distance - 1)) / Math.sqrt(Math.pow(k, 2.0) + 1);
            double z = (pl.getLocation().getZ() - Config.getCenter().getZ() < 0 ? -1 : 1) * Math.abs(x) / k;

            pl.teleport(new Location(pl.getLocation().getWorld(), Config.getCenter().getX() + x, pl.getLocation().getY(), Config.getCenter().getZ() + z, pl.getLocation().getYaw(), pl.getLocation().getPitch()));
            pl.sendMessage("§cПокидать границы карты запрещено!");
        }
    }
}
