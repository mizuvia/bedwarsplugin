package inventories;

import game.Game;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Swords implements IGUI{
    private final Game game;

    public Swords(Game game) {
        this.game = game;
    }

    public Game getGame(){ return this.game; }

    @Override
    public void onGUIClick(Player whoClicked, int slot, ItemStack clickedItem) {
        switch (slot) {
            case 10 -> whoClicked.openInventory(this.getGame().getBlocksInventory());
            case 11 -> whoClicked.openInventory(this.getGame().getSwordsInventory());
            case 12 -> whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getUniqueId()).getArmorInventory());
            case 13 -> whoClicked.openInventory(this.getGame().getBowsInventory());
            case 14 -> whoClicked.openInventory(this.getGame().getPlugin().getPlayers().get(whoClicked.getUniqueId()).getToolsInventory());
            case 15 -> whoClicked.openInventory(this.getGame().getPotionsInventory());
            case 16 -> whoClicked.openInventory(this.getGame().getOthersInventory());
            case 28, 30, 31, 32, 33 -> this.getInventory().makeTrade(this.getGame().getPlugin().getPlayers().get(whoClicked.getUniqueId()), clickedItem);
        }
    }

    @NotNull
    @Override
    public SwordsInventory getInventory() {
        return this.getGame().getSwordsInventory();
    }
}
