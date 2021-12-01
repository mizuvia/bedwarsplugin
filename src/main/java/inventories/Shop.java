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
        switch (slot) {
            case 10 -> this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getBlocksInventory());
            case 11 -> this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getSwordsInventory());
            case 12 -> this.player.getPlayer().openInventory(this.player.getArmorInventory());
            case 13 -> this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getBowsInventory());
            case 14 -> this.player.getPlayer().openInventory(this.player.getToolsInventory());
            case 15 -> this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getPotionsInventory());
            case 16 -> this.player.getPlayer().openInventory(this.player.getPlugin().getGame().getOthersInventory());
        }
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return player.getShopInventory();
    }
}
