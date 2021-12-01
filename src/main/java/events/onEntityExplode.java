package events;

import main.Plugin;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class onEntityExplode extends SimpleListener implements Listener, EventExecutor {
    public onEntityExplode(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        EntityExplodeEvent e = (EntityExplodeEvent) event;

        Iterator<Block> iterator = e.blockList().iterator();
        while(iterator.hasNext()){
            Block block = iterator.next();
            if(!this.getPlugin().getGame().getBlockList().contains(block)) iterator.remove();
            else if(block.getType().equals(Material.GLASS)) iterator.remove();
        }
        e.setYield(0);
    }
}
