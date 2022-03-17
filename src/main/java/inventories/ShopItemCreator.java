package inventories;

import game.ItemPrice;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ShopItemCreator {

    public static ItemStack createShopItem(ShopItem shopItem, Material mat, int amountInStack, String name, @Nullable ItemPrice price, @Nullable List<String> lore) {
        ItemStack item = new ItemStack(mat, amountInStack);
        PotionMeta meta = (PotionMeta) item.getItemMeta();

        int localName = (int) (Math.random() * Math.random() * 1000);

        switch (shopItem) {
            case POTIONS -> meta.setColor(Color.YELLOW);
            case BOW2 -> meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            case BOW3 -> {
                meta.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
            }
            case STICK -> meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            case ARROW2 -> {
                meta.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
                meta.setColor(Color.ORANGE);
            }
            case ARROW3 -> {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5, 0, false), true);
                meta.setColor(Color.PURPLE);
            }
            case WOODEN_PICKAXE, WOODEN_AXE, STONE_AXE -> meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
            case STONE_PICKAXE, IRON_PICKAXE, IRON_AXE -> meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
            case DIAMOND_AXE, DIAMOND_PICKAXE -> meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
            case INVISIBILITY_POTION -> {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 600, 0, true), true);
                meta.setColor(Color.PURPLE);
            }
            case JUMPING_POTION -> {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 900, 4, true), true);
                meta.setColor(Color.LIME);
            }
            case SWIFTNESS_POTION -> {
                meta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 900, 1, true), true);
                meta.setColor(Color.AQUA);
            }
            case CROSSBOW1 -> {
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
                meta.addEnchant(Enchantment.QUICK_CHARGE, 1, true);
            }
            case CROSSBOW2 -> {
                meta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
                meta.addEnchant(Enchantment.QUICK_CHARGE, 2, true);
                meta.addEnchant(Enchantment.MULTISHOT, 1, true);
            }
        }

        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        List<String> metaLore = new ArrayList<>();
        if(price != null) {
            metaLore.add(price.toString());
            metaLore.add(" ");
        }
        if (lore != null)
            metaLore.addAll(lore);
        meta.setLore(metaLore);
        meta.setLocalizedName(String.valueOf(localName));
        item.setItemMeta(meta);

        return item;
    }
}
