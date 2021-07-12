package inventories;

import game.Participant;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Tools implements IGUI{
    public Participant player;

    public Tools(Participant p) {
        this.player = p;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot){
            case 10: whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getBlocksInventory()); break;
            case 11: whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getSwordsInventory()); break;
            case 12: whoClicked.openInventory(this.getPlayer().getArmorInventory()); break;
            case 13: whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getBowsInventory()); break;
            case 14: whoClicked.openInventory(this.getPlayer().getToolsInventory()); break;
            case 15: whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getPotionsInventory()); break;
            case 16: whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getOthersInventory()); break;

            case 30:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(this.getPlayer(), clickedItem)){
                    switch (clickedItem.getType()) {
                        case WOODEN_PICKAXE: this.getInventory().setItem(slot, ShopItems.STONE_PICKAXE); break;
                        case STONE_PICKAXE: this.getInventory().setItem(slot, ShopItems.IRON_PICKAXE); break;
                        case IRON_PICKAXE: this.getInventory().setItem(slot, ShopItems.DIAMOND_PICKAXE); break;
                        case DIAMOND_PICKAXE: this.getInventory().setItem(slot, ShopItems.GOLDEN_PICKAXE); break;
                        case GOLDEN_PICKAXE: this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
                        default: break;
                    }
                }
                break;
            case 31:
                if(clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if(this.getInventory().makeTrade(this.getPlayer(), clickedItem)){
                    switch (clickedItem.getType()) {
                        case WOODEN_AXE: this.getInventory().setItem(slot, ShopItems.STONE_AXE); break;
                        case STONE_AXE: this.getInventory().setItem(slot, ShopItems.IRON_AXE); break;
                        case IRON_AXE: this.getInventory().setItem(slot, ShopItems.DIAMOND_AXE); break;
                        case DIAMOND_AXE: this.getInventory().setItem(slot, ShopItems.GOLDEN_AXE); break;
                        case GOLDEN_AXE: this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
                        default: break;
                    }
                }
                break;
            case 28:
            case 33:
            case 34:
                this.getInventory().makeTrade(this.getPlayer(), clickedItem);
        }
    }

    @NotNull
    @Override
    public ToolsInventory getInventory() {
        return this.getPlayer().getToolsInventory();
    }

    private Participant getPlayer(){return this.player; }
}
