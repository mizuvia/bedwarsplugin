package inventories;

import com.hoshion.library.MizuviaLibrary.MineColor;
import com.hoshion.mongoapi.MongoService;
import game.Participant;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChangeItemGUI implements IGUI {

    public static final Map<UUID, Integer> UPDATING_SLOTS = new HashMap<>();
    private final Plugin plugin;
    public static final List<String> ADDITIONAL_LORE = List.of(
            "",
            MineColor.AQUA + "ЛКМ - Купить предмет",
            MineColor.AQUA + "ПКМ - Изменить предмет"
    );

    public ChangeItemGUI(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGUIClick(Player whoClicked, int slot, Inventory i) {
        if (i.getItem(slot) == null) return;

        ChangeItemInventory inv = (ChangeItemInventory) i;
        ShopItem item = inv.shopItems.get(slot).getFirst();
        Participant p = plugin.getPlayer(whoClicked);
        ShopInventory shopInv = p.getShopInventory();

        int index = UPDATING_SLOTS.get(whoClicked.getUniqueId());
        UPDATING_SLOTS.remove(whoClicked.getUniqueId());

        MongoService.getTable(com.hoshion.mongoapi.docs.Player.class).update(ShopInventory.DATABASE_FIELDS.get(index), item.name());

        shopInv.shopItems.put(slot, shopInv.getItem(item.name()));
        shopInv.setItem(index, Utils.addLore(item.getItem(), ADDITIONAL_LORE));

        p.getPlayer().openInventory(shopInv);
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }
}
