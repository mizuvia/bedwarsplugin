package inventories;

import game.ItemPrice;
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
import java.util.ArrayList;
import java.util.List;

public class ShopItemCreator {
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
}
