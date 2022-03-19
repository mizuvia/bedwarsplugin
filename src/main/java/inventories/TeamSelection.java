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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    public void onGUIClick(Player player, int slot, Inventory inventory) {
        ItemStack clickedItem = inventory.getItem(slot);
        if(clickedItem.getType().name().equals("GRAY_STAINED_GLASS_PANE")) return;

        String color = clickedItem.getType().name().replace("_WOOL", "").toLowerCase(Locale.ROOT);
        Team team = this.getPlugin().getTeams().get(color);
        Participant participant = this.getPlugin().getPlayers().get(player.getUniqueId());
        player.closeInventory();

        if (team.getTeammatesAmount() == Config.getPlayersPerTeam()){
            player.sendMessage("§cКоманда заполнена!");
            return;
        }

        if (participant.hasTeam()) {
            if(participant.getTeam() == team){
                player.sendMessage("§eВы уже присоединились к данной команде!");
                return;
            }

            participant.setTeam(null);
        }

        participant.setTeam(team);
        player.sendMessage("§eВы успешно присоединились к команде " + team.getName() + "§e!");
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
