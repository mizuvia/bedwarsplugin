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
        switch (slot){
            case 10: whoClicked.openInventory(this.player.getPlugin().getGame().getBlocksInventory()); break;
            case 11: whoClicked.openInventory(this.player.getPlugin().getGame().getSwordsInventory()); break;
            case 12: whoClicked.openInventory(this.player.getArmorInventory()); break;
            case 13: whoClicked.openInventory(this.player.getPlugin().getGame().getBowsInventory()); break;
            case 14: whoClicked.openInventory(this.player.getToolsInventory()); break;
            case 15: whoClicked.openInventory(this.player.getPlugin().getGame().getPotionsInventory()); break;
            case 16: whoClicked.openInventory(this.player.getPlugin().getGame().getOthersInventory()); break;

            case 29:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(player, clickedItem)){
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
                break;
            case 30:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(player, clickedItem)){
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(30, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
                break;
            case 32:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(player, clickedItem)){
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(30, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(32, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
                break;
            case 33:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(player, clickedItem)){
                    this.getInventory().setItem(29, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(30, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(32, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                    this.getInventory().setItem(33, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", "")[0]);
                }
                break;
        }
    }

    @NotNull
    @Override
    public ArmorInventory getInventory() {
        return this.player.getArmorInventory();
    }
}
