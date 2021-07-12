package inventories;

/* << СВЯТ НАТУРАЛ >> */

import game.Participant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Shop implements IGUI {

    private final Participant player;

    public Shop(Participant p){
        this.player = p;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot){
            case 10: this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getBlocksInventory()); break;
            case 11: this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getSwordsInventory()); break;
            case 12: this.player.getPlayer().openInventory(this.player.getArmorInventory()); break;
            case 13: this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getBowsInventory()); break;
            case 14: this.player.getPlayer().openInventory(this.player.getToolsInventory()); break;
            case 15: this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getPotionsInventory()); break;
            case 16: this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getOthersInventory()); break;
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return player.getShopInventory();
    }
}
