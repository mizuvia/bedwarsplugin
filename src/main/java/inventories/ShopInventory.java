package inventories;

import com.hoshion.library.MizuviaLibrary.Donate;
import com.hoshion.library.MizuviaLibrary.DonateManager;
import com.hoshion.mongoapi.MongoService;
import com.hoshion.mongoapi.docs.Player;
import game.Participant;
import main.Plugin;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class ShopInventory extends SimpleInventory {

    public Map<Integer, LinkedList<ShopItem>> items = new HashMap<>();
    private final Participant p;

    public ShopInventory(ShopGUI holder, Participant p) {
        super(holder, 54, "Магазин");
        this.p = p;
        createItems();
        holder.setShopItems(shopItems);
        this.addItems();
    }

    public LinkedList<ShopItem> getItem(String name) {
        ShopItem item;
        try {
            item = ShopItem.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
        if (ShopItems.getList(ShopItems.TOOLS, item) != null) {
            return ShopItems.getList(ShopItems.TOOLS, item);
        }
        return ShopItems.getList(item);
    }

    private void createItems() {
        UUID uuid = p.getPlayer().getUniqueId();
        Player pl = MongoService.findByUUID(uuid);
        if (DonateManager.higherOrEqual(p.getGroup(), Donate.PRINCE)) {
            shopItems.put(29, getItem(pl.bw$don11));
            shopItems.put(33, getItem(pl.bw$don13));
            shopItems.put(38, getItem(pl.bw$don12));
            shopItems.put(42, getItem(pl.bw$don14));
            if (DonateManager.higherOrEqual(p.getGroup(), Donate.EARL)) {
                shopItems.put(28, getItem(pl.bw$don21));
                shopItems.put(34, getItem(pl.bw$don23));
                shopItems.put(37, getItem(pl.bw$don22));
                shopItems.put(43, getItem(pl.bw$don24));
            }
        }
        shopItems.put(30, getItem(pl.bw$def1));
        shopItems.put(31, getItem(pl.bw$def3));
        shopItems.put(32, getItem(pl.bw$def5));
        shopItems.put(39, getItem(pl.bw$def2));
        shopItems.put(40, getItem(pl.bw$def4));
        shopItems.put(41, getItem(pl.bw$def6));
    }

    private void addItems() {
        SimpleInventory.SHOPS.forEach((index, item) -> this.setItem(index, item.getItem()));

        items.forEach((index, item) -> {
            if (item == null) this.setItem(index, null);
            else this.setItem(index, item.getFirst().getItem());
        });
    }
}