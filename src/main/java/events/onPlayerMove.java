package events;

import game.Participant;
import game.Team;
import main.Config;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
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

import java.util.List;

public class onPlayerMove extends SimpleListener implements Listener, EventExecutor {

    public onPlayerMove(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerMoveEvent e = (PlayerMoveEvent) event;

        if(!e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            if (!this.getPlugin().getPlayers().get(e.getPlayer().getName()).hasTeam()) return;
            if (this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getTeamUpgrades().get("Healing") != 0) {
                Location playerLoc = e.getPlayer().getLocation();
                Location teamLoc = this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getSpawnLocation();
                double distance = Math.sqrt(Math.pow(playerLoc.getX() - teamLoc.getX(), 2.0) + Math.pow(playerLoc.getZ() - teamLoc.getZ(), 2.0));
                if (distance <= 25)
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100000, 0));
                else e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
            }
            for (Team team : this.getPlugin().getTeams().values()) {
                Location playerLoc = e.getPlayer().getLocation();

                if (team.getIronGolem() != null) {
                    if (this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getColor().equals(team.getColor()))
                        continue;
                    Location golemLoc = team.getIronGolem().getLocation();
                    double distance = Math.sqrt(Math.pow(playerLoc.getX() - golemLoc.getX(), 2.0) + Math.pow(playerLoc.getZ() - golemLoc.getZ(), 2.0));
                    if (distance <= 6) {
                        if (team.getIronGolem().getTarget() == null) team.getIronGolem().setTarget(e.getPlayer());
                    }
                    if (distance >= 15) {
                        if (team.getIronGolem().getTarget() != null)
                            if (team.getIronGolem().getTarget().equals(e.getPlayer()))
                                team.getIronGolem().setTarget(null);
                    }
                }

                if (team.getTraps().size() != 0) {
                    if (this.getPlugin().getPlayers().get(e.getPlayer().getName()).isUnderMilk()) return;
                    if (team.getColor().equals(this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getColor()))
                        continue;

                    double distance = Math.sqrt(Math.pow(playerLoc.getX() - team.getSpawnLocation().getX(), 2.0) + Math.pow(playerLoc.getZ() - team.getSpawnLocation().getZ(), 2.0));
                    if (distance <= 25) {
                        switch (team.getTraps().get(0)) {
                            case "Задержка":
                                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 160, 0, false, false));
                                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 0, false, false));
                                break;
                            case "Поддержка":
                                for (Participant participant : team.getTeammates().values()) {
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
                                e.getPlayer().setGlowing(true);
                                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> e.getPlayer().setGlowing(false), 200);
                                break;
                            case "Усталость":
                                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0, false, false));
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

                        for (Participant participant : team.getTeammates().values()) {
                            participant.getPlayer().sendTitle("§4Ловушка активирована!", "§cВозвращайтесь на базу и защитите кровать.", 10, 70, 20);
                            participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.BLOCK_ANVIL_LAND, 10F, 1F);
                        }
                    }
                }
            }
        }

        double distance = Math.sqrt(Math.pow(e.getPlayer().getLocation().getX() - Config.getCenter().getX(), 2.0) + Math.pow(e.getPlayer().getLocation().getZ() - Config.getCenter().getZ(), 2.0));
        double k = (e.getPlayer().getLocation().getX() - Config.getCenter().getX()) / (e.getPlayer().getLocation().getZ() - Config.getCenter().getZ());
        if(distance >= 300) {

            if(k < 0) k = -k;

            double x = (e.getPlayer().getLocation().getX() - Config.getCenter().getX() < 0 ? -1 : 1) * (k * (distance - 1)) / Math.sqrt(Math.pow(k, 2.0) + 1);
            double z = (e.getPlayer().getLocation().getZ() - Config.getCenter().getZ() < 0 ? -1 : 1) * Math.abs(x) / k;

            e.getPlayer().teleport(new Location(e.getPlayer().getLocation().getWorld(), Config.getCenter().getX() + x, e.getPlayer().getLocation().getY(), Config.getCenter().getZ() + z, e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch()));
            e.getPlayer().sendMessage("§cПокидать границы карты запрещено!");
        }
    }
}
