package inventories;

import game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Others implements IGUI{
    private final Game game;

    public Others(Game game) {
        this.game = game;
    }

    public Game getGame(){ return this.game; }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot) {
            case 10 -> whoClicked.openInventory(this.getGame().getBlocksInventory());
            case 11 -> whoClicked.openInventory(this.getGame().getSwordsInventory());
            case 12 -> whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()).getArmorInventory());
            case 13 -> whoClicked.openInventory(this.getGame().getBowsInventory());
            case 14 -> whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()).getToolsInventory());
            case 15 -> whoClicked.openInventory(this.getGame().getPotionsInventory());
            case 16 -> whoClicked.openInventory(this.getGame().getOthersInventory());
            case 29, 30, 32, 33, 38, 39, 41, 42 -> this.getInventory().makeTrade(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()), clickedItem);
        }
    }

    @NotNull
    @Override
    public OthersInventory getInventory() {
        return this.getGame().getOthersInventory();
    }
}
