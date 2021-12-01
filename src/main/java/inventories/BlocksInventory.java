package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class BlocksInventory extends SimpleInventory {

    public BlocksInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
        this.addItems();
    }

    private void addItems() {
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(ShopItems.BLOCKS);
        this.addItem(ShopItems.SWORDS);
        this.addItem(ShopItems.ARMOR);
        this.addItem(ShopItems.BOWS);
        this.addItem(ShopItems.TOOLS);
        this.addItem(ShopItems.POTIONS);
        this.addItem(ShopItems.OTHERS);
        this.addItem(this.createItem(11, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItem.WOOL.getItem());
        this.addItem(ShopItem.TERRACOTTA.getItem());
        this.addItem(ShopItem.LADDER.getItem());
        this.addItem(ShopItem.GLASS.getItem());
        this.addItem(ShopItem.BIRCH_PLANKS.getItem());
        this.addItem(ShopItem.REDSTONE_ORE.getItem());
        this.addItem(ShopItem.OBSIDIAN.getItem());

        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(7, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
    }
}