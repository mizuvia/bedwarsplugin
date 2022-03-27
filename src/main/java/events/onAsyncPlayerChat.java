package events;

import game.Participant;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.Colors;

public class onAsyncPlayerChat extends SimpleListener implements Listener, EventExecutor {
    public onAsyncPlayerChat(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        AsyncPlayerChatEvent e = (AsyncPlayerChatEvent) event;

        e.setCancelled(true);
        if (this.getPlugin().isLoading()) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.sendMessage(e.getPlayer().getDisplayName() + ": " + Colors.colorize(e.getMessage()));
            }
        }
        if (this.getPlugin().isWorking()) {
            if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (!player.getGameMode().equals(GameMode.SPECTATOR)) continue;

                    player.sendMessage("§7[НАБЛЮДАТЕЛЬ] " + e.getPlayer().getName() + ": " + Colors.colorize(e.getMessage()));
                }
            } else {
                if (e.getMessage().startsWith("!")) {
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        player.sendMessage(PlayerManager.getCodeColor(this.getPlugin().getPlayer(e.getPlayer())) + e.getPlayer().getName() + "§7: " + Colors.colorize(e.getMessage()).replace("!", ""));
                    }
                } else {
                    for (Participant participant : this.getPlugin().getPlayer(e.getPlayer()).getTeam().getTeammates()) {
                        participant.getPlayer().sendMessage(PlayerManager.getCodeColor(participant) + "[Чат команды] " + e.getPlayer().getName() + "§7: " + Colors.colorize(e.getMessage()));
                    }
                }
            }
        }
    }
}
