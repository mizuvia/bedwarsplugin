package inventories;

import game.ItemPrice;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
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
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class ShopItems {

    public static ItemStack createShopItem(Material mat, int amountInStack, String name, @Nullable ItemPrice price, @Nullable String... lore) {
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
            case "§eКирка I уровня", "§eТопор I уровня", "§eТопор II уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            case "§eКирка II уровня", "§eКирка III уровня", "§eТопор III уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
            case "§eКирка IV уровня", "§eТопор IV уровня" -> meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
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
        List<String> metaLore = new ArrayList<>();
        if(price != null) {
            metaLore.add(price.toString());
            metaLore.add(" ");
        }
        if (lore != null)
            metaLore.addAll(List.of(lore));
        meta.setLore(metaLore);
        meta.setLocalizedName(String.valueOf(localName));
        item.setItemMeta(meta);


        return item;

    }

    public static final Map<Material, Integer> TOOLS_ITEMS_INDEXES = Map.ofEntries(
            Map.entry(Material.SHEARS, 28), Map.entry(Material.FISHING_ROD, 33),
            Map.entry(Material.WOODEN_PICKAXE, 30), Map.entry(Material.WOODEN_AXE, 31),
            Map.entry(Material.STONE_PICKAXE, 30), Map.entry(Material.STONE_AXE, 31),
            Map.entry(Material.IRON_PICKAXE, 30), Map.entry(Material.IRON_AXE, 31),
            Map.entry(Material.DIAMOND_PICKAXE, 30), Map.entry(Material.DIAMOND_AXE, 31)
    );

    public static final Map<ShopItem, ShopItem> NEXT_TOOLS_TIER = Map.of(
            ShopItem.WOODEN_AXE, ShopItem.STONE_AXE,
            ShopItem.STONE_AXE, ShopItem.IRON_AXE,
            ShopItem.IRON_AXE, ShopItem.DIAMOND_AXE,
            ShopItem.WOODEN_PICKAXE, ShopItem.STONE_PICKAXE,
            ShopItem.STONE_PICKAXE, ShopItem.IRON_PICKAXE,
            ShopItem.IRON_PICKAXE, ShopItem.DIAMOND_PICKAXE
    );

    public static final Map<Integer, LinkedList<ShopItem>> BLOCKS = Map.ofEntries(
            Map.entry(28, getList(ShopItem.WOOL)),
            Map.entry(29, getList(ShopItem.TERRACOTTA)),
            Map.entry(30, getList(ShopItem.LADDER)),
            Map.entry(31, getList(ShopItem.GLASS)),
            Map.entry(32, getList(ShopItem.BIRCH_PLANKS)),
            Map.entry(33, getList(ShopItem.REDSTONE_ORE)),
            Map.entry(34, getList(ShopItem.OBSIDIAN))
    );

    public static final Map<Integer, LinkedList<ShopItem>> SWORDS = Map.ofEntries(
            Map.entry(29, getList(ShopItem.STICK)),
            Map.entry(31, getList(ShopItem.STONE_SWORD)),
            Map.entry(32, getList(ShopItem.IRON_SWORD)),
            Map.entry(33, getList(ShopItem.DIAMOND_SWORD))
    );

    public static final Map<Integer, LinkedList<ShopItem>> ARMOR = Map.ofEntries(
            Map.entry(28, getList(ShopItem.CHAINMAIL_BOOTS)),
            Map.entry(29, getList(ShopItem.IRON_BOOTS)),
            Map.entry(30, getList(ShopItem.DIAMOND_BOOTS))
    );

    public static final List<ShopItem> ARMOR_ORDER = List.of(
            ShopItem.CHAINMAIL_BOOTS, ShopItem.IRON_BOOTS, ShopItem.DIAMOND_BOOTS
    );

    public static final Map<Integer, LinkedList<ShopItem>> BOWS = Map.ofEntries(
            Map.entry(28, getList(ShopItem.BOW1)),
            Map.entry(29, getList(ShopItem.BOW2)),
            Map.entry(30, getList(ShopItem.BOW3)),
            Map.entry(32, getList(ShopItem.ARROW1)),
            Map.entry(33, getList(ShopItem.ARROW2)),
            Map.entry(34, getList(ShopItem.ARROW3)),
            Map.entry(37, getList(ShopItem.CROSSBOW1)),
            Map.entry(38, getList(ShopItem.CROSSBOW2))
    );

    public static final Map<Integer, LinkedList<ShopItem>> TOOLS = Map.ofEntries(
            Map.entry(28, getList(ShopItem.SHEARS)),
            Map.entry(30, getList(ShopItem.WOODEN_PICKAXE, ShopItem.STONE_PICKAXE, ShopItem.IRON_PICKAXE, ShopItem.DIAMOND_PICKAXE)),
            Map.entry(31, getList(ShopItem.WOODEN_AXE, ShopItem.STONE_AXE, ShopItem.IRON_AXE, ShopItem.DIAMOND_AXE)),
            Map.entry(33, getList(ShopItem.FISHING_ROD))
    );

    public static final Map<Integer, LinkedList<ShopItem>> POTIONS = Map.ofEntries(
            Map.entry(30, getList(ShopItem.INVISIBILITY_POTION)),
            Map.entry(31, getList(ShopItem.JUMPING_POTION)),
            Map.entry(32, getList(ShopItem.SWIFTNESS_POTION))
    );

    public static final Map<Integer, LinkedList<ShopItem>> OTHERS = Map.ofEntries(
            Map.entry(29, getList(ShopItem.GOLDEN_APPLE)),
            Map.entry(30, getList(ShopItem.TNT)),
            Map.entry(32, getList(ShopItem.GOLEM)),
            Map.entry(33, getList(ShopItem.WATER_BUCKET)),
            Map.entry(38, getList(ShopItem.TELEPORT)),
            Map.entry(39, getList(ShopItem.ENDER_PEARL)),
            Map.entry(41, getList(ShopItem.MILK)),
            Map.entry(42, getList(ShopItem.FIRE_CHARGE))
    );

    public static int getIndex(Map<Integer, LinkedList<ShopItem>> map, ShopItem value){
        int key = Integer.parseInt(null);

        for (Map.Entry<Integer, LinkedList<ShopItem>> entry : map.entrySet()) {
            if (entry.getValue().contains(value)) {
                key = entry.getKey();
                break;
            }
        }

        return key;
    }

    public static <T> LinkedList<T> getList(T item) {
        return new LinkedList<>(Collections.singleton(item));
    }

    public static <T> LinkedList<T> getList(T... items) {
        return new LinkedList<>(List.of(items));
    }

}
