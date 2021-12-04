package events;

import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.Utils;
import util.WorldManager;

public class onBlockPlace extends SimpleListener implements Listener, EventExecutor {
    public onBlockPlace(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockPlaceEvent e = (BlockPlaceEvent) event;

        if(this.getPlugin().isLoading()) e.setCancelled(true);
        e.setBuild(true);

        if(this.getPlugin().getGame().getInaccessibleBlocks().contains(e.getBlockPlaced().getLocation())){
            e.getPlayer().sendMessage(ChatColor.RED + "Вам не разрешено здесь строить");
            e.setCancelled(true);
        }

        this.getPlugin().getGame().getBlockList().add(e.getBlockPlaced());
        if(e.getBlockPlaced().getType().equals(Material.TNT)){
            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            Bukkit.getWorld("world").spawnEntity(WorldManager.centralizeLocation(e.getBlockPlaced().getLocation()), EntityType.PRIMED_TNT);
            e.setCancelled(true);
        }
    }
}
