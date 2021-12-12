package inventories;

import game.Participant;
import game.Team;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import util.TeamManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class TeamSelection implements IGUI{
    private final Plugin plugin;

    public TeamSelection(Plugin plugin){
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public Inventory getInventory(){
        return this.getPlugin().choose_team;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        if(clickedItem.getType().name().equals("GRAY_STAINED_GLASS_PANE")) return;

        String color = clickedItem.getType().name().replace("_WOOL", "").toLowerCase(Locale.ROOT);
        Team team = this.getPlugin().getTeams().get(color);
        Participant participant = this.getPlugin().getPlayers().get(whoClicked.getName());

        if(team.getTeammatesAmount() == Config.getPlayersPerTeam()){
            whoClicked.closeInventory();
            whoClicked.sendMessage("§cКоманда заполнена!");
            return;
        }

        if(participant.hasTeam()) {
            String oldColor = participant.getTeam().getColor();

            if(oldColor.equals(color)){
                whoClicked.closeInventory();
                whoClicked.sendMessage("§eВы уже присоединились к данной команде!");
                return;
            }

            TeamManager.removePlayerFromTeam(plugin, participant);
        }

        TeamManager.addPlayerToTeam(plugin, team, participant);

        participant.getPlayer().closeInventory();
        participant.getPlayer().sendMessage("§eВы успешно присоединились к команде " + team.getName() + "§e!");
    }

    public static void addPlayerToItem(Plugin plugin, Team team, Player player){
        String color = team.getColor();
        int index = plugin.getTeamSelectionInventory().first(Material.getMaterial(color.toUpperCase(Locale.ROOT) + "_WOOL"));

        ItemStack item = plugin.getTeamSelectionInventory().getItem(index);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add("§r" + PlayerManager.getCodeColor(color) + player.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);

        plugin.getTeamSelectionInventory().setItem(index, item);
    }

    public static void removePlayerFromItem(Plugin plugin, Participant participant){
        String color = participant.getTeam().getColor();
        int index = plugin.getTeamSelectionInventory().first(Material.getMaterial(color.toUpperCase(Locale.ROOT) + "_WOOL"));

        ItemStack item = plugin.getTeamSelectionInventory().getItem(index);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        lore.remove(PlayerManager.getCodeColor(color) + participant.getPlayer().getName());
        meta.setLore(lore);
        item.setItemMeta(meta);

        plugin.getTeamSelectionInventory().setItem(index, item);
    }

    public Plugin getPlugin(){ return this.plugin; }

}
