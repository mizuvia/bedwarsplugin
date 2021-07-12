package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class ToolsInventory extends SimpleInventory{

    public ToolsInventory(InventoryHolder owner, int size, String title) {
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

        this.addItem(ShopItems.SHEARS);

        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", " "));

        this.addItem(ShopItems.WOODEN_PICKAXE);
        this.addItem(ShopItems.WOODEN_AXE);

        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItems.FISHING_ROG);
        this.addItem(ShopItems.SHIELD);

        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(7, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

    }
}
