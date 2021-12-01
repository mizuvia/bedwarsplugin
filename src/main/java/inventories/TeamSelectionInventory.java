package inventories;

import game.Participant;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;

public class TeamSelectionInventory extends SimpleInventory {

    public TeamSelectionInventory(InventoryHolder owner, int size, String title, Plugin plugin) {
        super(owner, size, title);
        this.setMaxStackSize(1);
        this.plugin = plugin;
        this.addItems();
    }

    public void addItems() {
        List<String> teams = Config.getTeamsNames();
        if(Config.getTeamsAmount() == 8){
            this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(0).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(0) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(1).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(1) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(2).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(2) + ".display_name"), ""));
            this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(3).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(3) + ".display_name"), ""));
            this.addItem(this.createItem(7, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(4).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(4) + ".display_name"), ""));
            this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(5).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(5) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(6).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(6) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(7).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(7) + ".display_name"), ""));
            this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        }
        if(Config.getTeamsAmount() == 4){
            this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(0).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(0) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(1).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(1) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(2).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(2) + ".display_name"), ""));
            this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(3).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(3) + ".display_name"), ""));
            this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        }
        if(Config.getTeamsAmount() == 2){
            this.addItem(this.createItem(11, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(0).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(0) + ".display_name"), ""));
            this.addItem(this.createItem(3, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
            this.addItem(this.createItem(1, Material.getMaterial(teams.get(1).toUpperCase(Locale.ROOT) + "_WOOL"), 1, false, this.plugin.getConfig().getString("teams." + teams.get(1) + ".display_name"), ""));
            this.addItem(this.createItem(11, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        }
    }

    public void removePlayer(Participant participant) {

        int slot = this.first(Material.getMaterial(participant.getTeam().getColor().toUpperCase(Locale.ROOT) + "_WOOL"));
        ItemStack item = this.getItem(slot);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        lore.remove("§r" + PlayerManager.getCodeColor(participant) + participant.getPlayer().getName());

        meta.setLore(lore);
        item.setItemMeta(meta);
    }
}
