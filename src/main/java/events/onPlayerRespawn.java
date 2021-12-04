package events;

import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.PlayerInv;
import util.Utils;
import util.WorldManager;

public class onPlayerRespawn extends SimpleListener implements Listener, EventExecutor {

    public onPlayerRespawn(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerRespawnEvent e = (PlayerRespawnEvent) event;

        if(this.getPlugin().isLoading()) {
            if(this.getPlugin().isLoading()) PlayerInv.setWaitingInventory(this.getPlugin().getPlayers().get(e.getPlayer().getName()));
            e.setRespawnLocation(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
        }
//        if (this.getPlugin().isWorking()) {
//            ((Player) e.getEntity()).setGameMode(GameMode.SPECTATOR);
//            ((Player) e.getEntity()).getInventory().clear();
//
//            if (this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().isBroken()) {
//                this.getPlugin().getTab().removePlayer(this.getPlugin().getPlayers().get(e.getEntity().getName()));
//                ((Player) e.getEntity()).setPlayerListName("§7Наблюдатель " + e.getEntity().getName());
//            } else {
//                e.getEntity().teleport(this.getPlugin().getCenter());
//                ((Player) e.getEntity()).sendTitle("§cВы возродитесь через 5 секунд", "§7Ожидайте.", 10, 70, 20);
//
//                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
//                    e.getEntity().teleport(this.getPlugin().getPlayers().get(e.getEntity().getName()).getTeam().getSpawnLocation());
//                    PlayerInv.setPlayingInventory(this.getPlugin().getPlayers().get(e.getEntity().getName()));
//                    ((Player) e.getEntity()).setGameMode(GameMode.SURVIVAL);
//                }, 100);
//            }
//        }
    }
}
