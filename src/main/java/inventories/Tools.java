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
            case 30, 31 -> {
                if (clickedItem.getType().equals(Material.LIGHT_GRAY_STAINED_GLASS_PANE)) break;
                if (this.getInventory().makeTrade(this.getPlayer(), clickedItem)) {
                    switch (clickedItem.getType()) {
                        case DIAMOND_PICKAXE, DIAMOND_AXE -> this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
                        default -> {
                            String nextToolTier = ShopItems.NEXT_TOOLS_TIER.get(clickedItem.getType()).name();
                            this.getInventory().setItem(slot, ShopItem.valueOf(nextToolTier).getItem());
                        }
                    }
                }
            }
            case 28, 33 -> {
                this.getInventory().makeTrade(this.getPlayer(), clickedItem);
                this.getInventory().setItem(slot, this.getInventory().createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " ")[0]);
            }
        }
    }

    @NotNull
    @Override
    public ToolsInventory getInventory() {
        return this.getPlayer().getToolsInventory();
    }

    private Participant getPlayer(){return this.player; }
}
