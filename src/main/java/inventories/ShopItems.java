package inventories;

import org.bukkit.Material;

import java.util.*;

public class ShopItems {

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

    public static LinkedList<ShopItem> getList(Map<Integer, LinkedList<ShopItem>> map, ShopItem value) {
        int key = getIndex(map, value);
        return map.get(key);
    }

    public static int getIndex(Map<Integer, LinkedList<ShopItem>> map, ShopItem value){
        int key = 0;

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

    @SafeVarargs
    public static <T> LinkedList<T> getList(T... items) {
        return new LinkedList<>(List.of(items));
    }

    public static boolean isArmor(Material mat) {
        String name = mat.name();
        return name.matches(".+HELMET") || name.matches(".+CHESTPLATE") ||
               name.matches(".+LEGGINGS") || name.matches(".+BOOTS");
    }

    public static boolean isSword(Material mat) {
        return mat.name().matches(".+SWORD");
    }

    public static boolean isTool(Material mat) {
        String name = mat.name();
        return name.matches(".+AXE");
    }

}
