package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class TrapsInventory extends SimpleInventory{
    public TrapsInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
        this.setMaxStackSize(1);
        this.addItems();
    }

    public void addItems(){
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.TRIPWIRE_HOOK, 1, false,  "§eЗадержка", " ", "§5Накладывает на §d8 §5секунд", "§dСлепоту §5и §dМедлительность"));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.TRIPWIRE_HOOK, 1, false,  "§eПоддержка", " ", "§5Накладывает на союзников на", "§d10 §5секунд §dСкорость I §5и §dПрыгучесть II"));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.TRIPWIRE_HOOK, 1, false,  "§eВидимость", " ", "§5Подсвечивает игроков, их имя и команду"));
        this.addItem(this.createItem(1, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.TRIPWIRE_HOOK, 1, false,  "§eУсталость", " ", "§5Даёт врагам §dУсталость §5на §d10 §5секунд"));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
    }
}