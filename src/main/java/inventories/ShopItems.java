package inventories;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ShopItems {

    private static ItemStack createShopItem(Material mat, int amountInStack, String name, @Nullable String... lore) {
        ItemStack item = new ItemStack(mat, amountInStack);
        ItemMeta meta = item.getItemMeta();

        int localName = (int) (Math.random() * Math.random() * 1000);

        switch (name) {
            case "§e§lЗелья" -> ((PotionMeta) meta).setColor(Color.YELLOW);
            case "§eЛук II уровня" -> meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            case "§eЛук III уровня" -> {
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
            }
            case "§eПалка" -> meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            case "§eСтрела урона" -> {
                ((PotionMeta) meta).setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
                ((PotionMeta) meta).setColor(Color.ORANGE);
            }
            case "§eСтрела слабости" -> {
                ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5, 0, false), true);
                ((PotionMeta) meta).setColor(Color.PURPLE);
            }
            case "§eКирка I уровня", "§eТопор I уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            case "§eКирка II уровня", "§eТопор II уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
            case "§eКирка III уровня", "§eТопор III уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
            case "§eКирка IV уровня", "§eТопор IV уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 4, true);
            case "§eКирка V уровня", "§eТопор V уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
            case "§eЗелье невидимости" -> {
                ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true), true);
                ((PotionMeta) meta).setColor(Color.PURPLE);
            }
            case "§eЗелье прыгучести" -> {
                ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 900, 4, true), true);
                ((PotionMeta) meta).setColor(Color.LIME);
            }
            case "§eЗелье скорости" -> {
                ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1, true), true);
                ((PotionMeta) meta).setColor(Color.AQUA);
            }
            case "§eАрбалет I уровня" -> {
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
                meta.addEnchant(Enchantment.QUICK_CHARGE, 1, true);
            }
            case "§eАрбалет II уровня" -> {
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
                meta.addEnchant(Enchantment.QUICK_CHARGE, 2, true);
                meta.addEnchant(Enchantment.MULTISHOT, 1, true);
            }
        }

        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        if(!mat.equals(Material.SHIELD)) meta.setUnbreakable(true);
        else ((Damageable) meta).setDamage(236);
        if (!lore[0].equals("")) meta.setLore(Arrays.asList(lore));
        meta.setLocalizedName(String.valueOf(localName));
        item.setItemMeta(meta);

        return item;

    }

    public static ItemStack BLOCKS = ShopItems.createShopItem(Material.YELLOW_WOOL, 1, "§e§lБлоки", "");
    public static ItemStack SWORDS = ShopItems.createShopItem(Material.GOLDEN_SWORD, 1, "§e§lМечи", "");
    public static ItemStack ARMOR = ShopItems.createShopItem(Material.GOLDEN_CHESTPLATE, 1, "§e§lБроня", "");
    public static ItemStack BOWS = ShopItems.createShopItem(Material.BOW, 1, "§e§lЛуки", "");
    public static ItemStack TOOLS = ShopItems.createShopItem(Material.GOLDEN_PICKAXE, 1, "§e§lИнструменты", "");
    public static ItemStack POTIONS = ShopItems.createShopItem(Material.POTION, 1, "§e§lЗелья", "");
    public static ItemStack OTHERS = ShopItems.createShopItem(Material.GOLDEN_APPLE, 1, "§e§lРазное", "");

    ////////////
    // BLOCKS //
    ////////////

    public static ItemStack WOOL = ShopItems.createShopItem(Material.WHITE_WOOL, 16, "§eШерсть", "§8Стоимость: §74 железа", " ", "§5Отлично подходит для стройки мостов.", "§dПри покупке красится в цвет вашей команды.");
    public static ItemStack TERRACOTTA = ShopItems.createShopItem(Material.WHITE_TERRACOTTA, 24, "§eТерракота", "§8Стоимость: §716 железа", " ", "§5Базовый блок для защиты кровати.", "§dБыстро ломается киркой.");
    public static ItemStack LADDER = ShopItems.createShopItem(Material.LADDER, 8, "§eЛестница", "§8Стоимость: §74 железа", " ", "§5Отлично подходит для спасения", "§кошек, застрявших на дереве.", "§d:3");
    public static ItemStack GLASS = ShopItems.createShopItem(Material.GLASS, 4, "§eСтекло", "§8Стоимость: §712 железа", " ", "§5Блок для защиты кровати.", "§dВзрывоустойчивый.");
    public static ItemStack BIRCH_PLANKS = ShopItems.createShopItem(Material.BIRCH_PLANKS, 16, "§eДоски", "§8Стоимость: §e4 золота", " ", "§5Блок для защиты кровати.", "§dОтлично защищает кровать", "§dот игроков без топоров.");
    public static ItemStack REDSTONE_ORE = ShopItems.createShopItem(Material.REDSTONE_ORE, 16, "§eРедстоуновая руда", "§8Стоимость: §e4 золота", " ", "§5Хороший блок для защиты", "§5кровати на начальной стадии игры.", "§dДолго ломается деревянной", "§dи каменной кирками.");
    public static ItemStack OBSIDIAN = ShopItems.createShopItem(Material.OBSIDIAN, 4, "§eОбсидиан", "§8Стоимость: §24 изумруда", " ", "§5Лучший блок для защиты кровати.", "§dДолго ломается любой киркой.");

    ////////////
    // WEAPON //
    ////////////

    public static ItemStack STICK = ShopItems.createShopItem(Material.STICK, 1, "§eПалка", "§8Стоимость: §e5 золота", " ", "§5Потерянная трость", "§5Друга сервера §dKernox'a§5.");
    public static ItemStack STONE_SWORD = ShopItems.createShopItem(Material.STONE_SWORD, 1, "§eКаменный меч", "§8Стоимость: §710 железа", " ", "§5Неплохой меч для сражений", "§5на начальной стадии игры.");
    public static ItemStack IRON_SWORD = ShopItems.createShopItem(Material.IRON_SWORD, 1, "§eЖелезный меч", "§8Стоимость: §e7 золота", " ", "§5Железный меч для тех, кто", "§5с железными... нервами!");
    public static ItemStack DIAMOND_SWORD = ShopItems.createShopItem(Material.DIAMOND_SWORD, 1, "§eАлмазный меч", "§8Стоимость: §24 изумруда", " ", "§5Алмазный меч для тех,", "§5чей глаз - алмаз!", "§dСамый лучший меч.");

    ///////////
    // ARMOR //
    ///////////

    public static ItemStack GOLDEN_BOOTS = ShopItems.createShopItem(Material.GOLDEN_BOOTS, 1, "§eЗолотая броня", "§8Стоимость: §624 бронзы", " ", "§5Простенькая броня, дающая", "§5минимальную защиту.", "§dВыдаётся навсегда.");
    public static ItemStack CHAINMAIL_BOOTS = ShopItems.createShopItem(Material.CHAINMAIL_BOOTS, 1, "§eКольчуга", "§8Стоимость: §78 железа", " ", "§5Кольчуга для друга,", "§5кольчужка для подружки.", "§dВыдаётся навсегда.");
    public static ItemStack IRON_BOOTS = ShopItems.createShopItem(Material.IRON_BOOTS, 1, "§eЖелезная броня", "§8Стоимость: §e6 золота", " ", "§5Куй железо, пока горячо.", "§dВыдаётся навсегда.");
    public static ItemStack DIAMOND_BOOTS = ShopItems.createShopItem(Material.DIAMOND_BOOTS, 1, "§eАлмазная броня", "§8Стоимость: §24 изумруда", " ", "§5Я - бриллиант,", "§5вы - серые камешки.", "§dЛучшая броня.", "§dВыдаётся навсегда.");

    //////////
    // BOWS //
    //////////

    public static ItemStack BOW1 = ShopItems.createShopItem(Material.BOW, 1, "§eЛук I уровня", "§8Стоимость: §76 железа", " ", "§5Простой, базовый лук.", "§dХорош, чтобы скидывать", "§dигроков в бездну.");
    public static ItemStack BOW2 = ShopItems.createShopItem(Material.BOW, 1, "§eЛук II уровня", "§8Стоимость: §e9 золота", " ", "§5Круче, чем лук I уровня", "§dХорош, чтобы скидывать", "§dигроков в бездну.");
    public static ItemStack BOW3 = ShopItems.createShopItem(Material.BOW, 1, "§eЛук III уровня", "§8Стоимость: §26 изумрудов", " ", "§5Самый крутой лук.", "§dПросто идеальный!");
    public static ItemStack ARROW1 = ShopItems.createShopItem(Material.ARROW, 8, "§eСтрела", "§8Стоимость: §72 железа", " ", "§5Без неё твой лук бесполезен.", "§dПомни: стрелы кончаются.");
    public static ItemStack ARROW2 = ShopItems.createShopItem(Material.TIPPED_ARROW, 2, "§eСтрела урона", "§8Стоимость: §22 изумруда", " ", "§5Наносит моментальный урок игроку.");
    public static ItemStack ARROW3 = ShopItems.createShopItem(Material.TIPPED_ARROW, 2, "§eСтрела слабости", "§8Стоимость: §22 изумруда", " ", "§5Уменьшает урон, наносимый", "§5игроком, на 5 секунд.");
    public static ItemStack CROSSBOW1 = ShopItems.createShopItem(Material.CROSSBOW, 1, "§eАрбалет I уровня", "§8Стоимость: §212 изумрудов", " ", "§5Более прокаченный лук.", "§dМожно сразу зарядить стрелу,", "§dи в бою натягивать тетиву", "§dне придётся.");
    public static ItemStack CROSSBOW2 = ShopItems.createShopItem(Material.CROSSBOW, 1, "§eАрбалет II уровня", "§8Стоимость: §224 изумруда", " ", "§5Лучший арбалет.", "§dМожно сразу зарядить стрелу,", "§dи в бою натягивать тетиву", "§dне придётся.");

    ///////////
    // TOOLS //
    ///////////

    public static ItemStack SHEARS = ShopItems.createShopItem(Material.SHEARS, 1, "§eНожницы ", "§8Стоимость: §720 железа", " ", "§5Хорошо ломают шерсть.", "§dВыдаётся навсегда.");
    public static ItemStack WOODEN_PICKAXE = ShopItems.createShopItem(Material.WOODEN_PICKAXE, 1, "§eКирка I уровня", "§8Стоимость: §710 железа", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.", "§dI уровень остается навсегда.");
    public static ItemStack STONE_PICKAXE = ShopItems.createShopItem(Material.STONE_PICKAXE, 1, "§eКирка II уровня", "§8Стоимость: §710 железа", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack IRON_PICKAXE = ShopItems.createShopItem(Material.IRON_PICKAXE, 1, "§eКирка III уровня", "§8Стоимость: §e3 золота", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack DIAMOND_PICKAXE = ShopItems.createShopItem(Material.DIAMOND_PICKAXE, 1, "§eКирка IV уровня", "§8Стоимость: §e6 золота", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack GOLDEN_PICKAXE = ShopItems.createShopItem(Material.GOLDEN_PICKAXE, 1, "§eКирка V уровня", "§8Стоимость: §25 изумрудов", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack WOODEN_AXE = ShopItems.createShopItem(Material.WOODEN_AXE, 1, "§eТопор I уровня", "§8Стоимость: §620 бронзы", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.", "§dI уровень остается навсегда.");
    public static ItemStack STONE_AXE = ShopItems.createShopItem(Material.STONE_AXE, 1, "§eТопор II уровня", "§8Стоимость: §78 железа", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack IRON_AXE = ShopItems.createShopItem(Material.IRON_AXE, 1, "§eТопор III уровня", "§8Стоимость: §e6 золота", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack DIAMOND_AXE = ShopItems.createShopItem(Material.DIAMOND_AXE, 1, "§eТопор IV уровня", "§8Стоимость: §e10 золота", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack GOLDEN_AXE = ShopItems.createShopItem(Material.GOLDEN_AXE, 1, "§eТопор V уровня", "§8Стоимость: §25 изумрудов", " ", "§5Уровень предмета может", "§5быть повышен.", "§5После смерти вы теряете", "§5один уровень улучшения.");
    public static ItemStack FISHING_ROG = ShopItems.createShopItem(Material.FISHING_ROD, 1, "§eУдочка", "§8Стоимость: §e6 золота", " ", "§5Удобна для рыбалки и", "§5хороша чтобы скидывать", "§5игроков в бездну.", "§dнавсегда.");

    ////////////
    // POTION //
    ////////////

    public static ItemStack INVISIBILITY_POTION = ShopItems.createShopItem(Material.POTION, 1, "§eЗелье невидимости", "§8Стоимость: §22 изумруда", " ", "§5Хорошее зелье, чтобы ломать кровати", "§dПолная невидимость на 30 секунд.");
    public static ItemStack JUMPING_POTION = ShopItems.createShopItem(Material.POTION, 1, "§eЗелье прыгучести", "§8Стоимость: §21 изумруд", " ", "§5Идеально подходит для тех,", "§5кто мечтал стать кроликом!", "§dВы прыгаете на 6 метров в высоту", "§dна протяжении 45 секунд.");
    public static ItemStack SWIFTNESS_POTION = ShopItems.createShopItem(Material.POTION, 1, "§eЗелье скорости", "§8Стоимость: §21 изумруд", " ", "§5С этим зельем убегать от противников,", "§5становится гораздо легче!", "§dВы ускоряетесь на 45 секунд.");

    ///////////////////
    // MISCELLANEOUS //
    ///////////////////

    public static ItemStack GOLDEN_APPLE = ShopItems.createShopItem(Material.GOLDEN_APPLE, 1, "§eЗолотое яблоко", "§8Стоимость: §75 железа.", " ", "§5Восстанавливает здоровье.");
    public static ItemStack TNT = ShopItems.createShopItem(Material.TNT, 1, "§eДинамит", "§8Стоимость: §74 железа", " ", "§5Помогает в разрушении кровати", "§dНе взрывает стекло." );
    public static ItemStack GOLEM = ShopItems.createShopItem(Material.GHAST_SPAWN_EGG, 1, "§eСтраж", "§8Стоимость: §6120 бронзы", " ", "§5Помогает с защитой вашей базы.", "§dЖивет не больше 4 минут.");
    public static ItemStack MILK = ShopItems.createShopItem(Material.MILK_BUCKET, 1, "§eЧудо-молоко", "§8Стоимость: §e3 золота", " ", "§5После употребления ни одна", "§5ловушка не сработает на вас.", "§dДействует 60 секунд.");
    public static ItemStack TELEPORT = ShopItems.createShopItem(Material.GUNPOWDER, 1, "§eТелепортатор", "§8Стоимость: §e3 золота", " ", "§5Через три секунды после использования", "§5вы возвращаетесь к себе на базу.", "§dУдобно, если на вас нападают враги.");
    public static ItemStack ENDER_PEARL = ShopItems.createShopItem(Material.ENDER_PEARL, 1, "§eЖемчуг Эндера", "§8Стоимость: §24 изумруда", " ", "§5Самый быстрый способ", "§5попасть на базу противника.", "§dНе промахнитесь, пока кидаете.");
    public static ItemStack WATER_BUCKER = ShopItems.createShopItem(Material.WATER_BUCKET, 1, "§eВедро с водой", "§8Стоимость: §e3 золота", " ", "§5Замедляет врагов и", "§5защищает вас от взрывов." );
    public static ItemStack FIRE_CHARGE = ShopItems.createShopItem(Material.FIRE_CHARGE, 1, "§eОгненный шар", "§8Стоимость: §640 бронзы", " ", "§5Нажмите ПКМ чтобы выстрелить.", "§dХорошее противодействие игрокам,", "§dстроящим тонкие мосты.");
}
