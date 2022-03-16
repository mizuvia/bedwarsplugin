package events;

import game.Participant;
import game.Team;
import inventories.ShopItem;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.Utils;
import util.WorldManager;

import java.util.Locale;

public class onBlockBreak extends SimpleListener implements Listener, EventExecutor {
    public onBlockBreak(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        BlockBreakEvent e = (BlockBreakEvent) event;

        e.setDropItems(false);

        Block b = e.getBlock();
        Participant p = plugin.getPlayers().get(e.getPlayer().getUniqueId());

        if(b.getType().name().matches("(.*)BED")){
            String color = b.getType().name().replace("_BED", "").toLowerCase(Locale.ROOT);

            if(color.equals(p.getTeam().getColor())) {
                e.setCancelled(true);
                return;
            }

            Team team = plugin.getTeams().get(color);
            if(team.isBroken()) return;
            team.setBroken(true);

            Utils.sendToAll("§7Кровать команды " + team.getName() + "§r§7 была разрушена игроком " + PlayerManager.getCodeColor(p) + e.getPlayer().getName());

            for(Participant participant : team.getTeammates().values()){
                participant.getPlayer().sendTitle("§4Ваша кровать разрушена!", "§cВы больше не возродитесь!", 10, 70, 20);
                participant.getPlayer().playSound(participant.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 10F, 1F);
            }

            p.increaseBrokenBeds();

            team.setBedDestroyer(p);

            return;
        }
        if(b.getType().equals(Material.REDSTONE_ORE)){
            Bukkit.getWorld("world").dropItem(WorldManager.getLocation(b.getLocation().getBlockX() + " " + b.getLocation().getBlockY() + " " + b.getLocation().getBlockZ()), new ItemStack(Material.REDSTONE_ORE, 1));
        }
        if(!this.getPlugin().getGame().getBlockList().contains(b)) {
            e.setCancelled(true);
            return;
        }

        String name;
        if(b.getType().name().matches("(.*)WOOL")) name = ShopItem.WOOL.getName();
        else if(b.getType().name().matches("(.*)TERRACOTTA")) name = ShopItem.TERRACOTTA.getName();
        else name = ShopItem.valueOf(b.getType().name()).getName();

        ItemStack item = Utils.createItem(b.getType(), 1, name);
        e.getPlayer().getInventory().addItem(item);

    }
}
