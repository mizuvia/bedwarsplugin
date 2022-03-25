package events;

import com.hoshion.mongoapi.MongoService;
import com.hoshion.mongoapi.docs.Party;
import game.Participant;
import game.Team;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import util.*;

import java.util.ArrayList;

public class onPlayerJoin extends SimpleListener implements Listener, EventExecutor {

    public onPlayerJoin(Plugin plugin){
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerJoinEvent e = (PlayerJoinEvent) event;
        Player pl = e.getPlayer();
        if (pl.getGameMode() == GameMode.SURVIVAL || pl.getGameMode() == GameMode.ADVENTURE) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                pl.getWorld().getPlayers().forEach(p -> {
                    p.hidePlayer(plugin, pl);
                    pl.hidePlayer(plugin, p);
                });
                pl.getWorld().getPlayers().forEach(p -> {
                    p.showPlayer(plugin, pl);
                    pl.showPlayer(plugin, p);
                });
            });
        }
        if (this.getPlugin().isLoading()){
            Bukkit.getServer().getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getWaiting().checkAmount(), 5);
            pl.teleport(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
            pl.setGameMode(GameMode.ADVENTURE);
            
            Participant p = new Participant(pl, this.getPlugin());

            this.setNames(p);
            PlayerInv.setWaitingInventory(p);

            changePlayerAmount(e, pl);
            addToTeam(p);
            pl.teleport(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
        }
        if (this.getPlugin().isWorking()){
            pl.setGameMode(GameMode.SPECTATOR);
            pl.setPlayerListName("§7Наблюдатель " + pl.getName());
            pl.setScoreboard(plugin.getSidebar().getSpectatorScoreboard());

            e.setJoinMessage(null);
        }

        pl.setCollidable(false);
        pl.setLevel(0);
        pl.setExp(0);
        pl.setHealth(pl.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        pl.setFoodLevel(20);
    }

    private void changePlayerAmount(PlayerJoinEvent e, Player pl) {
        this.getPlugin().getSidebar().changePlayersAmount();
        e.setJoinMessage(pl.getDisplayName() + " §eприсоединился к игре §f[§b" + this.getPlugin().getOnlinePlayers() + "§f/§b" + Config.getMaxPlayers() + "§f]");
        this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());

    }

    private void setNames(Participant p){
        p.getPlayer().setDisplayName(Colors.replaceHex(PlayerManager.getGroupDisplayName(p)) + p.getPlayer().getName());
        p.getPlayer().setPlayerListName(p.getPlayer().getDisplayName());
        String serverName = ChatColor.GOLD + "" + ChatColor.BOLD + "Mizuvia";
        p.getPlayer().setPlayerListHeader(serverName + "\n ");
        p.getPlayer().setPlayerListFooter("\n§e§lСайт: §6§lmizuvia.fun");
    }

    private void addToTeam(Participant p) {
        Party party = MongoService.findOneParty("id", MongoService.findByUUID(p.getPlayer().getUniqueId()).gen$party_id);
        if(party != null){
            PartyManager.addPlayer(this.getPlugin(), party, MongoService.findByUUID(p.getPlayer().getUniqueId()));
        } else {
            Team freeTeam = null;
            for(Team team : this.getPlugin().getTeams().values()){
                if(team.getTeammatesAmount() != Config.getPlayersPerTeam()){
                    if (freeTeam == null) {
                        freeTeam = team;
                    }else {
                        if (freeTeam.getTeammatesAmount() > team.getTeammatesAmount()) {
                            freeTeam = team;
                        }
                    }
                }
            }
            p.setTeam(freeTeam);
        }
    }
}
