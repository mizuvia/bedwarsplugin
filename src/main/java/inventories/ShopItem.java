package inventories;

import game.ItemPrice;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum ShopItem {

    // blocks

    WOOL
            (Material.WHITE_WOOL, 16, "§eШерсть", new ItemPrice(Material.IRON_INGOT, 4), "§5Отлично подходит для стройки мостов.", "§dПри покупке красится в цвет вашей команды."),
    TERRACOTTA
            (Material.WHITE_TERRACOTTA, 24, "§eТерракота", new ItemPrice(Material.IRON_INGOT, 16), "§5Базовый блок для защиты кровати.", "§dБыстро ломается киркой."),
    LADDER
            (Material.LADDER, 8, "§eЛестница", new ItemPrice(Material.IRON_INGOT, 4), "§5Отлично подходит для спасения", "§кошек, застрявших на дереве.", "§d:3"),
    GLASS
            (Material.GLASS, 4, "§eСтекло", new ItemPrice(Material.IRON_INGOT, 12), "§5Блок для защиты кровати.", "§dВзрывоустойчивый."),
    BIRCH_PLANKS
            (Material.BIRCH_PLANKS, 16, "§eДоски", new ItemPrice(Material.GOLD_INGOT, 4), "§5Блок для защиты кровати.", "§dОтлично защищает кровать", "§dот игроков без топоров."),
    REDSTONE_ORE
            (Material.REDSTONE_ORE, 16, "§eРедстоуновая руда", new ItemPrice(Material.GOLD_INGOT, 4), "§5Хороший блок для защиты", "§5кровати на начальной стадии игры.", "§dДолго ломается деревянной", "§dи каменной кирками."),
    OBSIDIAN
            (Material.OBSIDIAN, 4, "§eОбсидиан", new ItemPrice(Material.EMERALD, 4), "§5Лучший блок для защиты кровати.", "§dДолго ломается любой киркой."),

    // weapons

    STICK
            (Material.STICK, 1, "§eПалка", new ItemPrice(Material.GOLD_INGOT, 5), "§5Потерянная трость", "§5Друга сервера §dKernox'a§5."),
    STONE_SWORD
            (Material.STONE_SWORD, 1, "§eКаменный меч", new ItemPrice(Material.IRON_INGOT, 10), "§5Неплохой меч для сражений", "§5на начальной стадии игры."),
    IRON_SWORD
            (Material.IRON_SWORD, 1, "§eЖелезный меч", new ItemPrice(Material.GOLD_INGOT, 7), "§5Железный меч для тех, кто", "§5с железными... нервами!"),
    DIAMOND_SWORD
            (Material.DIAMOND_SWORD, 1, "§eАлмазный меч", new ItemPrice(Material.EMERALD, 6), "§5Алмазный меч для тех,", "§5чей глаз - алмаз!", "§dСамый лучший меч."),

    // armor

    CHAINMAIL_BOOTS
            (Material.CHAINMAIL_BOOTS, 1, "§eКольчуга", new ItemPrice(Material.IRON_INGOT, 25), "§5Кольчуга для друга,", "§5кольчужка для подружки.", "§dВыдаётся навсегда."),
    IRON_BOOTS
            (Material.IRON_BOOTS, 1, "§eЖелезная броня", new ItemPrice(Material.GOLD_INGOT, 12), "§5Куй железо, пока горячо.", "§dВыдаётся навсегда."),
    DIAMOND_BOOTS
            (Material.DIAMOND_BOOTS, 1, "§eАлмазная броня", new ItemPrice(Material.EMERALD, 6), " ", "§5Я - бриллиант,", "§5вы - серые камешки.", "§dЛучшая броня.", "§dВыдаётся навсегда."),

    // ranged

    BOW1
            (Material.BOW, 1, "§eЛук I уровня", new ItemPrice(Material.GOLD_INGOT, 10), "§5Простой, базовый лук.", "§dХорош, чтобы скидывать", "§dигроков в бездну."),
    BOW2
            (Material.BOW, 1, "§eЛук II уровня", new ItemPrice(Material.GOLD_INGOT, 16), "§5Круче, чем лук I уровня", "§dХорош, чтобы скидывать", "§dигроков в бездну."),
    BOW3
            (Material.BOW, 1, "§eЛук III уровня", new ItemPrice(Material.EMERALD, 6), "§5Самый крутой лук.", "§dПросто идеальный!"),
    ARROW1
            (Material.ARROW, 8, "§eСтрела", new ItemPrice(Material.GOLD_INGOT, 2), "§5Без неё твой лук бесполезен.", "§dПомни: стрелы кончаются."),
    ARROW2
            (Material.TIPPED_ARROW, 2, "§eСтрела урона", new ItemPrice(Material.GOLD_INGOT, 4), "§5Наносит моментальный урок игроку."),
    ARROW3
            (Material.TIPPED_ARROW, 2, "§eСтрела слабости", new ItemPrice(Material.EMERALD, 4), "§5Уменьшает урон, наносимый", "§5игроком, на 5 секунд."),
    CROSSBOW1
            (Material.CROSSBOW, 1, "§eАрбалет I уровня", new ItemPrice(Material.EMERALD, 8), "§5Более прокаченный лук.", "§dМожно сразу зарядить стрелу,", "§dи в бою натягивать тетиву", "§dне придётся."),
    CROSSBOW2
            (Material.CROSSBOW, 1, "§eАрбалет II уровня", new ItemPrice(Material.EMERALD, 12), "§5Лучший арбалет.", "§dМожно сразу зарядить стрелу,", "§dи в бою натягивать тетиву", "§dне придётся."),

    // tools

    SHEARS
            (Material.SHEARS, 1, "§eНожницы ", new ItemPrice(Material.IRON_INGOT, 20), "§5Хорошо ломают шерсть.", "§dВыдаётся навсегда."),
    WOODEN_PICKAXE
            (Material.WOODEN_PICKAXE, 1, "§eКирка I уровня", new ItemPrice(Material.IRON_INGOT, 10), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.", "§dI уровень остается навсегда."),
    STONE_PICKAXE
            (Material.STONE_PICKAXE, 1, "§eКирка II уровня", new ItemPrice(Material.IRON_INGOT, 10), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    IRON_PICKAXE
            (Material.IRON_PICKAXE, 1, "§eКирка III уровня", new ItemPrice(Material.GOLD_INGOT, 3), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    DIAMOND_PICKAXE
            (Material.DIAMOND_PICKAXE, 1, "§eКирка IV уровня", new ItemPrice(Material.GOLD_INGOT, 6), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    WOODEN_AXE
            (Material.WOODEN_AXE, 1, "§eТопор I уровня", new ItemPrice(Material.IRON_INGOT, 10), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.", "§dI уровень остается навсегда."),
    STONE_AXE
            (Material.STONE_AXE, 1, "§eТопор II уровня", new ItemPrice(Material.IRON_INGOT, 10), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    IRON_AXE
            (Material.IRON_AXE, 1, "§eТопор III уровня", new ItemPrice(Material.GOLD_INGOT, 3), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    DIAMOND_AXE
            (Material.DIAMOND_AXE, 1, "§eТопор IV уровня", new ItemPrice(Material.GOLD_INGOT, 6), "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения."),
    FISHING_ROD
            (Material.FISHING_ROD, 1, "§eУдочка", new ItemPrice(Material.GOLD_INGOT, 6), "§5Удобна для рыбалки и", "§5хороша чтобы скидывать", "§5игроков в бездну.", "§dнавсегда."),

    // potions

    INVISIBILITY_POTION
            (Material.POTION, 1, "§eЗелье невидимости", new ItemPrice(Material.EMERALD, 2), "§5Хорошее зелье, чтобы ломать кровати", "§dПолная невидимость на 30 секунд."),
    JUMPING_POTION
            (Material.POTION, 1, "§eЗелье прыгучести", new ItemPrice(Material.EMERALD, 1), "§5Идеально подходит для тех,", "§5кто мечтал стать кроликом!", "§dВы прыгаете на 6 метров в высоту", "§dна протяжении 45 секунд."),
    SWIFTNESS_POTION
            (Material.POTION, 1, "§eЗелье скорости", new ItemPrice(Material.EMERALD, 1), "§5С этим зельем убегать от противников,", "§5становится гораздо легче!", "§dВы ускоряетесь на 45 секунд."),

    // miscellaneous

    GOLDEN_APPLE
            (Material.GOLDEN_APPLE, 1, "§eЗолотое яблоко", new ItemPrice(Material.GOLD_INGOT, 3), "§5Восстанавливает здоровье."),
    TNT
            (Material.TNT, 1, "§eДинамит", new ItemPrice(Material.GOLD_INGOT, 4), "§5Помогает в разрушении кровати", "§dНе взрывает стекло." ),
    GOLEM
            (Material.GHAST_SPAWN_EGG, 1, "§eСтраж", new ItemPrice(Material.IRON_INGOT, 120), "§5Помогает с защитой вашей базы.", "§dЖивет не больше 4 минут."),
    MILK
            (Material.MILK_BUCKET, 1, "§eЧудо-молоко", new ItemPrice(Material.GOLD_INGOT, 4), "§5После употребления ни одна", "§5ловушка не сработает на вас.", "§dДействует 60 секунд."),
    TELEPORT
            (Material.GUNPOWDER, 1, "§eТелепортатор", new ItemPrice(Material.GOLD_INGOT, 4), "§5Через три секунды после использования", "§5вы возвращаетесь к себе на базу.", "§dУдобно, если на вас нападают враги."),
    ENDER_PEARL
            (Material.ENDER_PEARL, 1, "§eЖемчуг Эндера", new ItemPrice(Material.EMERALD, 1), "§5Самый быстрый способ", "§5попасть на базу противника.", "§dНе промахнитесь, пока кидаете."),
    WATER_BUCKET
           (Material.WATER_BUCKET, 1, "§eВедро с водой", new ItemPrice(Material.EMERALD, 1), "§5Замедляет врагов и", "§5защищает вас от взрывов." ),
    FIRE_CHARGE
            (Material.FIRE_CHARGE, 1, "§eОгненный шар", new ItemPrice(Material.IRON_INGOT, 40), "§5Нажмите ПКМ чтобы выстрелить.", "§dХорошее противодействие игрокам,", "§dстроящим тонкие мосты."),

    //icons

    BLOCKS
            (Material.YELLOW_WOOL, 1, "§e§lБлоки", null),
    SWORDS
            (Material.GOLDEN_SWORD, 1, "§e§lМечи", null),
    ARMOR
            (Material.GOLDEN_CHESTPLATE, 1, "§e§lБроня", null),
    BOWS
            (Material.BOW, 1, "§e§lЛуки", null),
    TOOLS
            (Material.GOLDEN_PICKAXE, 1, "§e§lИнструменты", null),
    POTIONS
            (Material.POTION, 1, "§e§lЗелья", null),
    OTHERS
            (Material.GOLDEN_APPLE, 1, "§e§lРазное", null)
    ;


    private final Material material;
    private final ItemStack item;
    private final ItemPrice price;
    private final String name;
    private static final Map<String, ItemStack> itemMap = new HashMap<>();
    private static final Map<String, ItemPrice> priceMap = new HashMap<>();
    private static final Map<String, ShopItem> shopItemMap = new HashMap<>();
    static {
        for (ShopItem shopItem : ShopItem.values()){
            itemMap.put(shopItem.getName(), shopItem.getItem());
            priceMap.put(shopItem.getName(), shopItem.getPrice());
            shopItemMap.put(shopItem.getName(), shopItem);
        }
    }


    ShopItem(Material mat, int amountInStack, String name, ItemPrice price, @Nullable String... lore){
        this.item = ShopItemCreator.createShopItem(mat, amountInStack, name, price, lore);
        this.price = price;
        this.material = mat;
        this.name = name;
    }

    public ItemPrice getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public static ItemPrice getPriceByName(String name){
        return ShopItem.priceMap.get(name);
    }

    public static ShopItem getShopItem(String name) {
        return ShopItem.shopItemMap.get(name);
    }

    public static ItemStack getItemByName(String name){
        return ShopItem.itemMap.get(name);
    }

}
