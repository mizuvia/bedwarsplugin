package inventories;

import game.Game;
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

    private static int index = 0;
    protected Plugin plugin;
    protected Map<Integer, LinkedList<ShopItem>> shopItems;

    public SimpleInventory(InventoryHolder owner, int size, String title) {
        super(owner, size, title);
    }

    public SimpleInventory(Plugin plugin, String title, Map<Integer, LinkedList<ShopItem>> shopItems) {
        super(new ShopGUI(plugin, shopItems), 54, title);
        this.shopItems = shopItems;
        createShop();
    }

    public void createShop(){
        SimpleInventory.SHOPS.forEach((index, item) -> this.setItem(index, item.getItem()));
        shopItems.forEach((index, item) -> this.setItem(index, item.getFirst().getItem()));
    }

    @NotNull
    public ItemStack[] createItem(int amount, Material mat, int amountInStack, boolean ench, String name, String ...lore){
        ItemStack[] itemList = new ItemStack[amount];
        for(int i = 0; i < amount; i++){
            ItemStack item = new ItemStack(mat, amountInStack);
            ItemMeta meta = item.getItemMeta();

            int localName = index++;

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

        ItemStack item = new ItemStack(oldItem.getType(), oldItem.getAmount());

        if(oldItem.getType().name().matches("(.*)WOOL")) item.setType(Material.getMaterial(p.getTeam().getColor().toUpperCase(Locale.ROOT) + "_WOOL"));
        if(oldItem.getType().name().matches("(.*)TERRACOTTA")) item.setType(Material.getMaterial(p.getTeam().getColor().toUpperCase(Locale.ROOT) + "_TERRACOTTA"));
        ItemPrice price = ShopItem.getPriceByName(oldItem.getItemMeta().getDisplayName());

        item.setItemMeta(oldItem.getItemMeta());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(null);
        item.setItemMeta(itemMeta);

        boolean take = p.takeItem(price.getMaterial(), price.getPrice());

        if(take){
            p.giveItem(item);
        } else p.getPlayer().sendMessage("§cНедостаточно ресурсов");

        return take;
    }

    public void updateSlot(int slot) {
        String name = getItem(slot).getItemMeta().getDisplayName();
        ShopItem item = ShopItem.getShopItem(name);
        LinkedList<ShopItem> list = shopItems.get(slot);

        if (shopItems == ShopItems.TOOLS) {
            if (item != list.getLast()) {
                int in = list.indexOf(item);
                setItem(slot, list.get(in + 1).getItem());
            } else {
                setItem(slot, null);
            }
        }

        if (shopItems == ShopItems.ARMOR){
            int index = ShopItems.ARMOR_ORDER.indexOf(item);
            ListIterator<ShopItem> it = ShopItems.ARMOR_ORDER.listIterator(index);
            setItem(slot, null);
            while (it.hasPrevious()) {
                setItem(ShopItems.getIndex(shopItems, it.previous()), null);
            }
        }
    }

    public Plugin getPlugin() { return this.plugin; }

    public static final Map<Integer, ShopItem> SHOPS = Map.of(
            10, ShopItem.BLOCKS,
            11, ShopItem.SWORDS,
            12, ShopItem.ARMOR,
            13, ShopItem.BOWS,
            14, ShopItem.TOOLS,
            15, ShopItem.POTIONS,
            16, ShopItem.OTHERS
    );

}
