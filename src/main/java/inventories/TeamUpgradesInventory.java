package inventories;

import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;

public class TeamUpgradesInventory extends SimpleInventory {

    public TeamUpgradesInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
        this.setMaxStackSize(1);
        this.addItems();
    }

    public void addItems(){
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false,  " ", ""));
        this.addItem(this.createItem(1, Material.IRON_SWORD, 1, false,  "§eОстрота I", "§8Стоимость: §b4 алмаза", " ", "§r§5Накладывает на мечи всех", "§r§5союзников §dОстроту.", " ", "§7[Уровень: I]", "§7[Уровень: II]"));
        this.addItem(this.createItem(1, Material.IRON_CHESTPLATE, 1, false,  "§eЗащита I", "§8Стоимость: §b2 алмаза", " ", "§r§5Накладывает на броню всех", "§r§5союзников §dЗащиту.", " ", "§7[Уровень: I]", "§7[Уровень: II]", "§7[Уровень: III]", "§7[Уровень: IV]"));
        this.addItem(this.createItem(1, Material.IRON_PICKAXE, 1, false, "§eЭффективность I", "§8Стоимость: §b2 алмаза", " ", "§r§5Накладывает на топор и кирку всех", "§r§5союзников §dЭффективность.", " ", "§7[Уровень: I]", "§7[Уровень: II]"));
        this.addItem(this.createItem(1, Material.BEDROCK, 1, false, "§eНесокрушимость", "§8Стоимость: §b6 алмазов", " ", "§r§5Создаёт вокруг вашей кровати", "§r§d8 §5блоков §dБедрока §5на §d90 §5секунд.", " ", "§7Количество использований: §b2"));
        this.addItem(this.createItem(1, Material.BLAST_FURNACE, 1, false, "§eКузня I", "§8Стоимость: §b2 алмаза", " ", "§r§5Ускоряет время выпадения", "§r§dресурсов §5на базе.", " ", "§7[Уровень: I]", "§7[Уровень: II]", "§7[Уровень: III]"));
        this.addItem(this.createItem(1, Material.APPLE, 1, false, "§eИсцеление", "§8Стоимость: §b2 алмаза", " ", "§r§5Накладывает на всех находящихся", "§r§5на базе союзников §dРегенерацию", " ", "§7[Уровень: I]"));
        this.addItem(this.createItem(1, Material.TRIPWIRE_HOOK, 1, false, "§eЛовушка I", "§8Стоимость: §b1 алмаз", " ", "§r§5Накладывает на первого", "§r§5зашедшего на базу врага", "§r§dСлепоту §5и §dМедлительность", " ", "§7Ловушки:", "§8[ПУСТО]", "§8[ПУСТО]", "§8[ПУСТО]"));
        this.addItem(this.createItem(10, Material.GRAY_STAINED_GLASS_PANE, 1, false, " ", ""));
    }
}
