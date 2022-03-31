package inventories;

import main.Plugin;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class ChangeItemInventory extends SimpleInventory {

    public ChangeItemInventory(InventoryHolder holder) {
        super(holder, 54, "Изменить предмет");
        setMaxStackSize(1);
        addItems();
    }

    public void addItems(){
        List<ShopItem> list = List.of(
                ShopItem.WOODEN_SWORD, ShopItem.LEATHER_CHESTPLATE,
                ShopItem.LEATHER_BOOTS, ShopItem.LEATHER_HELMET,
                ShopItem.LEATHER_LEGGINGS, ShopItem.STONE_PICKAXE,
                ShopItem.STONE_AXE, ShopItem.IRON_AXE,
                ShopItem.IRON_PICKAXE, ShopItem.DIAMOND_AXE,
                ShopItem.DIAMOND_PICKAXE, ShopItem.BLOCKS,
                ShopItem.SWORDS, ShopItem.ARMOR, ShopItem.BOWS,
                ShopItem.TOOLS, ShopItem.POTIONS, ShopItem.OTHERS,
                ShopItem.SEARCH_SLOT
        );
        int index = 0;

        for (ShopItem item : ShopItem.values()) {
            if (list.contains(item)) continue;
            shopItems.put(index, ShopItems.getList(item));
            setItem(index++, item.getItem());
        }
    }
}
