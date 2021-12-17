package events;

import main.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class onPlayerItemConsume extends SimpleListener implements Listener, EventExecutor {
	
    public onPlayerItemConsume(Plugin plugin) {
        super(plugin);
    }
    
    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        PlayerItemConsumeEvent e = (PlayerItemConsumeEvent) event;

        if(e.getItem().getType().equals(Material.POTION)){
        	//e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

            if(((PotionMeta) e.getItem().getItemMeta()).getBasePotionData().getType().equals(PotionType.INVISIBILITY)) {
            	getPlugin().getPlayers().get(e.getPlayer().getName()).hide();
//                ItemStack helmet = e.getPlayer().getInventory().getHelmet().clone();
//                ItemStack chestplate = e.getPlayer().getInventory().getChestplate().clone();
//                ItemStack leggings = e.getPlayer().getInventory().getLeggings().clone();
//                ItemStack boots = e.getPlayer().getInventory().getBoots().clone();
//
//                e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
//                e.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
//                e.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
//                e.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
//
//                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
//                    e.getPlayer().getInventory().setHelmet(helmet);
//                    e.getPlayer().getInventory().setChestplate(chestplate);
//                    e.getPlayer().getInventory().setLeggings(leggings);
//                    e.getPlayer().getInventory().setBoots(boots);
//                }, 600);
            }
        }
    }
  
    
}
