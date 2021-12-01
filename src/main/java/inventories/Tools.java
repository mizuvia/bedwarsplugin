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
        switch (slot) {
            case 10 -> whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getBlocksInventory());
            case 11 -> whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getSwordsInventory());
            case 12 -> whoClicked.openInventory(this.getPlayer().getArmorInventory());
            case 13 -> whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getBowsInventory());
            case 14 -> whoClicked.openInventory(this.getPlayer().getToolsInventory());
            case 15 -> whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getPotionsInventory());
            case 16 -> whoClicked.openInventory(this.getPlayer().getPlugin().getGame().getOthersInventory());
            case 30 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(this.getPlayer(), clickedItem)) {
                    switch (clickedItem.getType()) {
                        case WOODEN_PICKAXE:
                            this.getInventory().setItem(slot, ShopItem.STONE_PICKAXE.getItem());
                            break;
                        case STONE_PICKAXE:
                            this.getInventory().setItem(slot, ShopItem.IRON_PICKAXE.getItem());
                            break;
                        case IRON_PICKAXE:
                            this.getInventory().setItem(slot, ShopItem.DIAMOND_PICKAXE.getItem());
                            break;
                        case DIAMOND_PICKAXE:
                            this.getInventory().setItem(slot, ShopItem.GOLDEN_PICKAXE.getItem());
                            break;
                        case GOLDEN_PICKAXE:
                            this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
                        default:
                            break;
                    }
                }
            }
            case 31 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(this.getPlayer(), clickedItem)) {
                    switch (clickedItem.getType()) {
                        case WOODEN_AXE:
                            this.getInventory().setItem(slot, ShopItem.STONE_AXE.getItem());
                            break;
                        case STONE_AXE:
                            this.getInventory().setItem(slot, ShopItem.IRON_AXE.getItem());
                            break;
                        case IRON_AXE:
                            this.getInventory().setItem(slot, ShopItem.DIAMOND_AXE.getItem());
                            break;
                        case DIAMOND_AXE:
                            this.getInventory().setItem(slot, ShopItem.GOLDEN_AXE.getItem());
                            break;
                        case GOLDEN_AXE:
                            this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
                        default:
                            break;
                    }
                }
            }
            case 28, 33, 34 -> this.getInventory().makeTrade(this.getPlayer(), clickedItem);
        }
    }

    @NotNull
    @Override
    public ToolsInventory getInventory() {
        return this.getPlayer().getToolsInventory();
    }

    private Participant getPlayer(){return this.player; }
}
