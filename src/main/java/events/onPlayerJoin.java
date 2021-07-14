package events;

import com.hoshion.mongoapi.docs.Party;
import game.Participant;
import game.Team;
import inventories.TeamSelection;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.Colors;
import util.PlayerInv;
import util.Utils;

import java.util.logging.Logger;

public class onPlayerJoin extends SimpleListener implements Listener, EventExecutor {

    public onPlayerJoin(Plugin plugin){
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerJoinEvent e = (PlayerJoinEvent) event;

        if(this.getPlugin().isLoading()){
            this.getPlugin().increaseOnlinePlayers();
            this.getPlugin().getWaiting().checkAmount();

            e.getPlayer().teleport(Utils.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
            e.getPlayer().setGameMode(GameMode.ADVENTURE);

            Participant p = new Participant(e.getPlayer(), this.getPlugin());

            e.getPlayer().setDisplayName(Colors.replaceHex(PlayerManager.getGroupDisplayName(p)) + e.getPlayer().getName());
            e.getPlayer().setPlayerListName(e.getPlayer().getDisplayName());

            this.getPlugin().getPlayers().put(e.getPlayer().getName(), p);
            PlayerInv.setWaitingInventory(p);

            this.getPlugin().getSidebar().changePlayersAmount(1);

            e.getPlayer().setScoreboard(this.getPlugin().getScoreboard());
            this.getPlugin().getTab().addPlayer(p);

            e.setJoinMessage(e.getPlayer().getDisplayName() + " §eприсоединился к игре §f[§b" + this.getPlugin().online_players + "§f/§b" + this.getPlugin().players_amount + "§f]");

            this.getPlugin().getJedis().publish("bw", this.getPlugin().getConfig().getString("server_name") + " " + this.getPlugin().getOnlinePlayers());

            Party party = this.getPlugin().getMongo().findOneParty("id", this.getPlugin().getMongo().findOnePlayer("uuid", e.getPlayer().getUniqueId().toString()).gen$party_id);
            if(party != null){
                for(Team team : this.getPlugin().getTeams().values()){
                    for(Participant par : team.getTeammates().values()){
                        if(party.members.contains(par.getPlayer().getUniqueId().toString()) && team.getTeammatesAmount() != this.getPlugin().getPlayersPerTeam()) {
                            TeamSelection.addPlayerToTeam(plugin, team, p);
                            return;
                        }
                    }
                }
                for(Team team : this.getPlugin().getTeams().values()){
                    if(this.getPlugin().getPlayersPerTeam() - team.getTeammatesAmount() >= party.members.size()){
                        TeamSelection.addPlayerToTeam(plugin, team, p);
                        return;
                    }
                }
            }
        }
        if(this.getPlugin().isWorking()){
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().setPlayerListName("§7Наблюдатель " + e.getPlayer().getName());
            e.getPlayer().setScoreboard(this.getPlugin().getScoreboard());

            e.setJoinMessage(null);
        }

        e.getPlayer().setHealth(20.0);

        String serverName = "§6§lMizu§5§lCraft";
        e.getPlayer().setPlayerListHeader("§e§l》— ⚝ —《\n\n" + serverName + "\n ");
        e.getPlayer().setPlayerListFooter("\n§e§lСайт: §6§lmizucraft.konch\n\n§e§l》— ⚝ —《");
    }
}
