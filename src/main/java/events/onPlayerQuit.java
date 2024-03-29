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

public class onPlayerQuit extends SimpleListener implements Listener, EventExecutor {

    public onPlayerQuit(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerQuitEvent e = (PlayerQuitEvent) event;

        Participant p = this.getPlugin().getPlayer(e.getPlayer());
        if (p == null) {
            e.setQuitMessage(null);
            return;
        }
        p.destroy();

        if(this.getPlugin().isLoading()){
            Bukkit.getServer().getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getWaiting().checkAmount(), 5);
            e.setQuitMessage(e.getPlayer().getDisplayName() + " §eпокинул игру §f[§b" + (this.getPlugin().getOnlinePlayers() - 1) + "§f/§b" + Config.getMaxPlayers() + "§f]");
            Bukkit.getServer().getScheduler().runTaskLater(getPlugin(), () -> getPlugin().getSidebar().changePlayersAmount(), 15);

            this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());
        }
        if(this.getPlugin().isWorking()){
            e.setQuitMessage(ChatColor.DARK_GRAY + e.getPlayer().getName() + " покинул игру.");
        }
    }
}
