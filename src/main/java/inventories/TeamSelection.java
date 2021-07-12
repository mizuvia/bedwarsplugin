package inventories;

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
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        if(clickedItem.getType().name().equals("GRAY_STAINED_GLASS_PANE")) return;
        String color = clickedItem.getType().name().replace("_WOOL", "").toLowerCase(Locale.ROOT);
        if(this.getPlugin().getTeams().get(color).getTeammatesAmount() == this.getPlugin().players_per_team){
            whoClicked.closeInventory();
            whoClicked.sendMessage("§cКоманда заполнена!");
            return;
        }

        if(this.getPlugin().getPlayers().get(whoClicked.getName()).hasTeam()) {
            String oldColor = this.getPlugin().getPlayers().get(whoClicked.getName()).getTeam().getColor();

            if(oldColor.equals(color)){
                whoClicked.closeInventory();
                whoClicked.sendMessage("§eВы уже присоединились к данной команде!");
                return;
            }

            int oldSlot = this.getPlugin().getTeamSelectionInventory().first(Material.getMaterial(oldColor.toUpperCase(Locale.ROOT) + "_WOOL"));
            ItemStack item = this.getPlugin().getTeamSelectionInventory().getContents()[oldSlot];
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.remove("§r" + PlayerManager.getCodeColor(oldColor) + whoClicked.getName());
            meta.setLore(lore);
            item.setItemMeta(meta);


            this.getPlugin().getTab().removePlayer(this.getPlugin().getPlayers().get(whoClicked.getName()));

            this.getPlugin().getTeams().get(oldColor).getTeammates().remove(whoClicked.getName());
            this.getPlugin().getTeams().get(oldColor).decreaseTeammatesAmount();
            this.getPlugin().getTeamSelectionInventory().setItem(oldSlot, item);
        }

        this.getPlugin().getPlayers().get(whoClicked.getName()).setTeam(this.getPlugin().getTeams().get(color));

        ItemStack item = this.getPlugin().getTeamSelectionInventory().getContents()[slot];
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) lore = new ArrayList<>();
        lore.add("§r" + PlayerManager.getCodeColor(color) + whoClicked.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);

        whoClicked.setPlayerListName("§8§l[" + this.getPlugin().getTeams().get(color).getName() + "§8§l]§r§7 " + whoClicked.getName());

        this.getPlugin().getTab().addPlayer(this.getPlugin().getPlayers().get(whoClicked.getName()));

        this.getPlugin().getTeams().get(color).getTeammates().put(whoClicked.getName(), this.getPlugin().getPlayers().get(whoClicked.getName()));
        this.getPlugin().getTeams().get(color).increaseTeammatesAmount();
        this.getPlugin().getTeamSelectionInventory().setItem(slot, item);
        whoClicked.closeInventory();
        whoClicked.sendMessage("§eВы успешно присоединились к команде!");
    }

    public Plugin getPlugin(){ return this.plugin; }

}
