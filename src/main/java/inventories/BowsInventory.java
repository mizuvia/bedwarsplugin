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

        this.addItem(ShopItem.BOW1.getItem());
        this.addItem(ShopItem.BOW2.getItem());
        this.addItem(ShopItem.BOW3.getItem());

        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItem.ARROW1.getItem());
        this.addItem(ShopItem.ARROW2.getItem());
        this.addItem(ShopItem.ARROW3.getItem());

        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(ShopItem.CROSSBOW1.getItem());
        this.addItem(ShopItem.CROSSBOW2.getItem());

        this.addItem(this.createItem(5, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

    }
}
