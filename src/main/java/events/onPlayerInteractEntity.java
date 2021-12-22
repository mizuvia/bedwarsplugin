package events;

import main.Plugin;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Villager;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class onPlayerInteractEntity extends SimpleListener implements Listener, EventExecutor {

    public onPlayerInteractEntity(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerInteractEntityEvent e = (PlayerInteractEntityEvent) event;

        if(e.getRightClicked() instanceof Villager) {
            e.setCancelled(true);
            if(e.getRightClicked().getCustomName().equals("§e§lМАГАЗИН")) e.getPlayer().openInventory(this.getPlugin().getPlayers().get(e.getPlayer().getUniqueId()).getShopInventory());
            if(e.getRightClicked().getCustomName().equals("§e§lУЛУЧШЕНИЯ")) {

                if(this.getPlugin().getPlayers().get(e.getPlayer().getUniqueId()).hasTeam()) e.getPlayer().openInventory(this.getPlugin().getPlayers().get(e.getPlayer().getUniqueId()).getTeam().getUpgradesInventory());
                else e.getPlayer().sendMessage("§cВы не находитесь в команде!");
            }
        }else if (e.getRightClicked() instanceof IronGolem) {
        	e.setCancelled(true);
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        }
    }
}
