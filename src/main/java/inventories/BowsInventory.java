package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class BowsInventory extends SimpleInventory{
    public BowsInventory(InventoryHolder owner, int size, String title) {
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

        this.addItem(ShopItems.BOW1);
        this.addItem(ShopItems.BOW2);
        this.addItem(ShopItems.BOW3);

        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItems.ARROW1);
        this.addItem(ShopItems.ARROW2);
        this.addItem(ShopItems.ARROW3);

        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItems.CROSSBOW1);
        this.addItem(ShopItems.CROSSBOW2);

        this.addItem(this.createItem(5, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

    }
}
