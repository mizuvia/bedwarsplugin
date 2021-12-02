package events;

import main.Config;
import main.Plugin;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPlayerQuit extends SimpleListener implements Listener, EventExecutor {

    public onPlayerQuit(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerQuitEvent e = (PlayerQuitEvent) event;

        if(this.getPlugin().getPlayers().containsKey(e.getPlayer().getName())) this.getPlugin().getTab().removePlayer(this.getPlugin().getPlayers().get(e.getPlayer().getName()));
        this.getPlugin().decreaseOnlinePlayers();

        if(this.getPlugin().getPlayers().get(e.getPlayer().getName()).hasTeam()){
            this.getPlugin().getTeamSelectionInventory().removePlayer(this.getPlugin().getPlayers().get(e.getPlayer().getName()));
            this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getTeammates().remove(e.getPlayer().getName());
            this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().decreaseTeammatesAmount();
        }

        this.getPlugin().getPlayers().remove(e.getPlayer().getName());

        if(this.getPlugin().isLoading()){

            this.getPlugin().getSidebar().changePlayersAmount();
            this.getPlugin().getWaiting().checkAmount();
            e.setQuitMessage(e.getPlayer().getDisplayName() + " §eпокинул игру §f[§b" + this.getPlugin().getOnlinePlayers() + "§f/§b" + Config.getMaxPlayers() + "§f]");

            this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());
        }
    }
}
