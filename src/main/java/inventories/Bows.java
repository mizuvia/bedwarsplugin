package inventories;

import game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Bows implements IGUI{
    private final Game game;

    public Bows(Game game) {
        this.game = game;
    }

    public Game getGame(){ return this.game; }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot){
            case 10: whoClicked.openInventory(this.getGame().getBlocksInventory()); break;
            case 11: whoClicked.openInventory(this.getGame().getSwordsInventory()); break;
            case 12: whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()).getArmorInventory()); break;
            case 13: whoClicked.openInventory(this.getGame().getBowsInventory()); break;
            case 14: whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()).getToolsInventory()); break;
            case 15: whoClicked.openInventory(this.getGame().getPotionsInventory()); break;
            case 16: whoClicked.openInventory(this.getGame().getOthersInventory()); break;

            case 28:
            case 29:
            case 30:
            case 32:
            case 33:
            case 34:
            case 37:
            case 38:
                this.getInventory().makeTrade(this.getGame().getPlugin().getPlayers().get(whoClicked.getName()), clickedItem);
        }
    }

    @NotNull
    @Override
    public BowsInventory getInventory() {
        return this.getGame().getBowsInventory();
    }
}
