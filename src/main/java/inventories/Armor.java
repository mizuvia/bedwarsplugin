package inventories;

import game.Participant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Armor implements IGUI{
    public Participant player;

    public Armor(Participant p) {
        this.player = p;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot) {
            case 10 -> whoClicked.openInventory(this.player.getPlugin().getGame().getBlocksInventory());
            case 11 -> whoClicked.openInventory(this.player.getPlugin().getGame().getSwordsInventory());
            case 12 -> whoClicked.openInventory(this.player.getArmorInventory());
            case 13 -> whoClicked.openInventory(this.player.getPlugin().getGame().getBowsInventory());
            case 14 -> whoClicked.openInventory(this.player.getToolsInventory());
            case 15 -> whoClicked.openInventory(this.player.getPlugin().getGame().getPotionsInventory());
            case 16 -> whoClicked.openInventory(this.player.getPlugin().getGame().getOthersInventory());
            case 28 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(player, clickedItem)) {
                    this.getInventory().setItem(28, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
            }
            case 29 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(player, clickedItem)) {
                    this.getInventory().setItem(28, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
            }
            case 30 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(player, clickedItem)) {
                    this.getInventory().setItem(28, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(30, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
            }
        }
    }

    @NotNull
    @Override
    public ArmorInventory getInventory() {
        return this.player.getArmorInventory();
    }
}
