package events;

import game.Participant;
import main.Config;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.TeamManager;

public class onPlayerQuit extends SimpleListener implements Listener, EventExecutor {

    public onPlayerQuit(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerQuitEvent e = (PlayerQuitEvent) event;

        Participant p = this.getPlugin().getPlayers().get(e.getPlayer().getUniqueId());

//        this.getPlugin().getTab().removePlayerFromTabs(p);
        this.getPlugin().decreaseOnlinePlayers();
        if (p != null) {
            if (p.inInvis()) {
            	p.show();
            }
            if(p.hasTeam()){
                this.getPlugin().getTeamSelectionInventory().removePlayer(p);
                TeamManager.removePlayerFromTeam(this.getPlugin(), p);
            }
        }

        this.getPlugin().getPlayers().remove(e.getPlayer().getUniqueId());

        if(this.getPlugin().isLoading()){

//            this.getPlugin().getSidebar().changePlayersAmount();
            Bukkit.getServer().getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getWaiting().checkAmount(), 5);
            e.setQuitMessage(e.getPlayer().getDisplayName() + " §eпокинул игру §f[§b" + this.getPlugin().getOnlinePlayers() + "§f/§b" + Config.getMaxPlayers() + "§f]");

            this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());
        }
        if(this.getPlugin().isWorking()){
            e.setQuitMessage(ChatColor.DARK_GRAY + e.getPlayer().getName() + " покинул игру.");
        }
    }
}
