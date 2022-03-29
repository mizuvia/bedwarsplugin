package inventories;

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

    public ShopInventory(Plugin plugin, Participant p) {
        super(new ShopGUI(plugin), 54, "Магазин");
        this.p = p;
        this.setMaxStackSize(1);
        createItems();
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
        Player p = MongoService.findByUUID(uuid);
        items.put(28, getItem(p.bw$don21));
        items.put(29, getItem(p.bw$don11));
        items.put(30, getItem(p.bw$def1));
        items.put(31, getItem(p.bw$def3));
        items.put(32, getItem(p.bw$def5));
        items.put(33, getItem(p.bw$don13));
        items.put(34, getItem(p.bw$don23));
        items.put(37, getItem(p.bw$don22));
        items.put(38, getItem(p.bw$don12));
        items.put(39, getItem(p.bw$def2));
        items.put(40, getItem(p.bw$def4));
        items.put(41, getItem(p.bw$def6));
        items.put(42, getItem(p.bw$don14));
        items.put(43, getItem(p.bw$don24));
    }

    private void addItems() {
        SimpleInventory.SHOPS.forEach((index, item) -> this.setItem(index, item.getItem()));

        items.forEach((index, item) -> {
            if (item == null) this.setItem(index, null);
            else this.setItem(index, item.getFirst().getItem());
        });
    }
}