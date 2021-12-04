package events;

import game.Participant;
import game.Team;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.WorldManager;

import java.util.Locale;

public class onBlockBreak extends SimpleListener implements Listener, EventExecutor {
    public onBlockBreak(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockBreakEvent e = (BlockBreakEvent) event;

        if(e.getBlock().getType().name().matches("(.*)BED")){
            String color = e.getBlock().getType().name().replace("_BED", "").toLowerCase(Locale.ROOT);

            if(color.equals(this.getPlugin().getPlayers().get(e.getPlayer().getName()).getTeam().getColor())) {
                e.setCancelled(true);
                return;
            }

            e.setDropItems(false);

            Team team = this.getPlugin().getTeams().get(color);
            if(team.isBroken()) return;
            team.setBroken(true);

            for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§7Кровать команды " + team.getName() + "§r§7 была разрушена игроком " + PlayerManager.getCodeColor(this.getPlugin().getPlayers().get(e.getPlayer().getName())) + e.getPlayer().getName());

            for(Participant participant : team.getTeammates().values()){
                participant.getPlayer().sendTitle("§4Ваша кровать разрушена!", "§cВы больше не возродитесь!", 10, 70, 20);
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 10F, 1F);
            }

            this.getPlugin().getPlayers().get(e.getPlayer().getName()).increaseBrokenBeds();

            team.setBedDestroyer(this.getPlugin().getPlayers().get(e.getPlayer().getName()));

            return;
        }
        if(e.getBlock().getType().equals(Material.REDSTONE_ORE)){
            e.setDropItems(false);
            Bukkit.getWorld("world").dropItem(WorldManager.getLocation(e.getBlock().getLocation().getBlockX() + " " + e.getBlock().getLocation().getBlockY() + " " + e.getBlock().getLocation().getBlockZ()), new ItemStack(Material.REDSTONE_ORE, 1));
        }
        if(!this.getPlugin().getGame().getBlockList().contains(e.getBlock())) e.setCancelled(true);

    }
}
