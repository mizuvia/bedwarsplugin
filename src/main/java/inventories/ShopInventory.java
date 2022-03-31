package inventories;

import com.hoshion.library.MizuviaLibrary.Donate;
import com.hoshion.library.MizuviaLibrary.DonateManager;
import com.hoshion.mongoapi.MongoService;
import com.hoshion.mongoapi.docs.Player;
import game.Participant;
import util.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class ShopInventory extends SimpleInventory {

    private final Participant p;
    public static final Map<Integer, String> DATABASE_FIELDS = Map.ofEntries(
            Map.entry(28, "bw$don21"), Map.entry(37, "bw$don22"),
            Map.entry(29, "bw$don11"), Map.entry(38, "bw$don12"),
            Map.entry(30, "bw$def1"), Map.entry(39, "bw$def2"),
            Map.entry(31, "bw$def3"), Map.entry(40, "bw$def4"),
            Map.entry(32, "bw$def5"), Map.entry(41, "bw$def6"),
            Map.entry(33, "bw$don13"), Map.entry(42, "bw$don14"),
            Map.entry(34, "bw$don23"), Map.entry(43,"bw$don24")
    );

    public ShopInventory(QuickSearchGUI holder, Participant p) {
        super(holder, 54, "Магазин");
        shopItems = new HashMap<>();
        this.p = p;
        createItems();
        holder.setInventory(this);
        this.addItems();
    }

    public LinkedList<ShopItem> getItem(String name) {
        if (name == null)
            return ShopItems.getList(ShopItem.SEARCH_SLOT);
        ShopItem item;
        try {
            item = ShopItem.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return ShopItems.getList(ShopItem.SEARCH_SLOT);
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

        shopItems.forEach((index, item) -> {
            if (item == null) this.setItem(index, null);
            else this.setItem(index, Utils.addLore(item.getFirst().getItem(), ChangeItemGUI.ADDITIONAL_LORE));
        });
    }
}