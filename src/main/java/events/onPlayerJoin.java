package events;

import game.Participant;
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
import util.PlayerInv;
import util.Utils;

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

            e.getPlayer().setDisplayName(PlayerManager.getGroupDisplayName(p) + e.getPlayer().getName());
            e.getPlayer().setPlayerListName(e.getPlayer().getDisplayName());

            this.getPlugin().getPlayers().put(e.getPlayer().getName(), p);
            PlayerInv.setWaitingInventory(p);

            this.getPlugin().getSidebar().changePlayersAmount(1);

            e.getPlayer().setScoreboard(this.getPlugin().getScoreboard());
            this.getPlugin().getTab().addPlayer(p);

            e.setJoinMessage(e.getPlayer().getDisplayName() + " §eприсоединился к игре §f[§b" + this.getPlugin().online_players + "§f/§b" + this.getPlugin().players_amount + "§f]");
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
