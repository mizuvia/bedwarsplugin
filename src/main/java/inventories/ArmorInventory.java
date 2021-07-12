package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class ArmorInventory extends SimpleInventory{

    public ArmorInventory(InventoryHolder owner, int size, String title) {
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

        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(ShopItems.GOLDEN_BOOTS);
        this.addItem(ShopItems.CHAINMAIL_BOOTS);
        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(ShopItems.IRON_BOOTS);
        this.addItem(ShopItems.DIAMOND_BOOTS);
        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(7, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

    }
}
