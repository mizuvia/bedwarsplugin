package inventories;

import game.ItemPrice;
import game.Participant;
import main.Plugin;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryCustom;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SimpleInventory extends CraftInventoryCustom {

    protected Plugin plugin;

    public SimpleInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
    }

    @NotNull
    protected ItemStack[] createItem(int amount, Material mat, int amountInStack, boolean ench, String name, String ...lore){
        ItemStack[] itemList = new ItemStack[amount];
        for(int i = 0; i < amount; i++){
            ItemStack item = new ItemStack(mat, amountInStack);
            ItemMeta meta = item.getItemMeta();

            int localName = (int) (Math.random() * 100000);

            meta.setDisplayName(name);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            if(!lore[0].equals("")) meta.setLore(Arrays.asList(lore));
            if(ench) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true);
            if(ench) meta.addEnchant(Enchantment.DURABILITY, 10, true);
            meta.setLocalizedName(String.valueOf(localName));
            item.setItemMeta(meta);

            itemList[i] = item;
        }

        return itemList;
    }

    public boolean makeTrade(Participant p, ItemStack oldItem){
        Material mat = Material.AIR;
        int amount = 0;
        ItemStack item = new ItemStack(oldItem.getType(), oldItem.getAmount());
        if(oldItem.getType().name().matches("(.*)WOOL")) item.setType(Material.getMaterial(p.getTeam().getColor().toUpperCase(Locale.ROOT) + "_WOOL"));
        if(oldItem.getType().name().matches("(.*)TERRACOTTA")) item.setType(Material.getMaterial(p.getTeam().getColor().toUpperCase(Locale.ROOT) + "_TERRACOTTA"));
        ItemPrice price = ShopItem.getPriceByMaterial(oldItem.getType());

        item.setItemMeta(oldItem.getItemMeta());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(null);
        if(item.getType().isBlock()) itemMeta.setDisplayName(null);
        item.setItemMeta(itemMeta);

        boolean take = p.takeItem(price.getMaterial(), price.getPrice());

        if(take){
            p.giveItem(item);
        } else p.getPlayer().sendMessage("§cНедостаточно ресурсов");

        return take;
    }

    public Plugin getPlugin() { return this.plugin; }
}
