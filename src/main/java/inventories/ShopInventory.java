package inventories;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class ShopInventory extends SimpleInventory {

    public ShopInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
        this.setMaxStackSize(1);
        this.addItems();
    }

    private void addItems() {
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(1, Material.YELLOW_WOOL, 1, false, "§e§lБлоки", ""));
        this.addItem(this.createItem(1, Material.GOLDEN_SWORD, 1, false, "§e§lМечи", ""));
        this.addItem(this.createItem(1, Material.GOLDEN_CHESTPLATE, 1, false, "§e§lБроня", ""));
        this.addItem(this.createItem(1, Material.BOW, 1, false, "§e§lЛуки", "")); // §
        this.addItem(this.createItem(1, Material.GOLDEN_PICKAXE, 1, false, "§e§lИнструменты", ""));

        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setColor(Color.YELLOW);
        meta.setDisplayName("§e§lЗелья");
        potion.setItemMeta(meta);

        this.addItem(potion);
        this.addItem(this.createItem(1, Material.GOLDEN_APPLE, 1, false, "§e§lРазное", ""));
        this.addItem(this.createItem(11, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));

        this.addItem(this.createItem(2, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(3, Material.PURPLE_DYE, 1, false, "§dЯчейка сохранения", ""));
        this.addItem(this.createItem(2, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(2, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(5, Material.PURPLE_DYE, 1, false, "§dЯчейка сохранения", ""));
        this.addItem(this.createItem(1, Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
    }
}