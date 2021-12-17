package events;

import com.hoshion.mongoapi.MongoService;
import com.hoshion.mongoapi.docs.Party;
import game.Participant;
import game.Team;
import inventories.TeamSelection;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.*;

public class onPlayerJoin extends SimpleListener implements Listener, EventExecutor {

    public onPlayerJoin(Plugin plugin){
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerJoinEvent e = (PlayerJoinEvent) event;
        if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
        	e.getPlayer().getWorld().getPlayers().forEach(p -> p.showPlayer(e.getPlayer()));
        }
        if(this.getPlugin().isLoading()){
            this.getPlugin().increaseOnlinePlayers();
            this.getPlugin().getWaiting().	checkAmount();

            e.getPlayer().teleport(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
            e.getPlayer().setGameMode(GameMode.ADVENTURE);
            
            Participant p = new Participant(e.getPlayer(), this.getPlugin());

            this.setNames(p);
            if (!this.plugin.getPlayers().containsKey(e.getPlayer().getName())) {
            	e.getPlayer().getEnderChest().clear();
            }
            this.getPlugin().getPlayers().put(e.getPlayer().getName(), p);
            PlayerInv.setWaitingInventory(p);


            e.getPlayer().setScoreboard(p.getScoreboard());
            this.getPlugin().getSidebar().fillPlayerSidebar(p);
            this.getPlugin().getTab().addPlayerToTabs(p);

            this.getPlugin().getSidebar().changePlayersAmount();

            e.setJoinMessage(e.getPlayer().getDisplayName() + " §eприсоединился к игре §f[§b" + this.getPlugin().getOnlinePlayers() + "§f/§b" + Config.getMaxPlayers() + "§f]");

            this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());

            Party party = MongoService.findOneParty("id", MongoService.findByUUID(e.getPlayer().getUniqueId()).gen$party_id);
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
              TeamManager.addPlayerToTeam(this.getPlugin(), freeTeam, p);
              return;
            }
        }
        if(this.getPlugin().isWorking()){
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.getPlayer().setPlayerListName("§7Наблюдатель " + e.getPlayer().getName());
            e.getPlayer().setScoreboard(this.getPlugin().getScoreboard());

            e.setJoinMessage(null);
        }

        e.getPlayer().setHealth(20.0);
        e.getPlayer().setFoodLevel(20);
    }

    private void setNames(Participant p){
        p.getPlayer().setDisplayName(Colors.replaceHex(PlayerManager.getGroupDisplayName(p)) + p.getPlayer().getName());
        p.getPlayer().setPlayerListName(p.getPlayer().getDisplayName());
        String serverName = ChatColor.GOLD + "" + ChatColor.BOLD + "Mizuvia";
        p.getPlayer().setPlayerListHeader(serverName + "\n ");
        p.getPlayer().setPlayerListFooter("\n§e§lСайт: §6§lmizuvia.fun");
    }
}
