package game;

import inventories.ShopItem;
import inventories.ShopItems;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.PlayerInventory;
import com.hoshion.library.MizuviaLibrary.MineColor;
import util.PlayerInv;
import util.Utils;

import java.text.DecimalFormat;
import java.util.*;

public class PlayerKiller {

    private static Plugin plugin;
    private final Participant p;
    private final EntityDamageEvent.DamageCause cause;

    public PlayerKiller(Participant p, EntityDamageEvent.DamageCause cause) {
        this.p = p;
        this.cause = cause;
    }

    public static void createInstance(Plugin pl) {
        plugin = pl;
    }

    public void killInGame() {
        Player player = p.getPlayer();
        boolean isFinal = p.getTeam().isBroken();
        String finalMessage = isFinal ? " §b§lФинальное убийство!" : "";
        String deathMessage = hasKiller() ? killWithKiller(isFinal) : killWithoutKiller(isFinal);
        Utils.sendToAll(PlayerManager.getCodeColor(p) + player.getName() + deathMessage + finalMessage);

        player.setGameMode(GameMode.SPECTATOR);
        player.setCanPickupItems(false);
        player.setHealth(20.0);
        player.teleport(Config.getCenter());
        p.clearPotionEffects();
        if (p.isInvisible()) p.showArmor();

        if (isFinal) {
            player.sendTitle("§cВы умерли и больше не возродитесь", "§7Наблюдайте за игрой.", 10, 70, 20);
            player.setPlayerListName("§7Наблюдатель " + player.getName());
            PlayerInv.clear(p);
            p.setDead(true);
            p.getTeam().kill();
            for (Player p : Bukkit.getOnlinePlayers()) {
                Participant par = plugin.getPlayer(p);
                if (par != null) {
                    if (!par.isDead()) p.hidePlayer(plugin, player);
                }
            }
        } else {
            player.sendTitle("§cВозрождение через 5 секунд", "§7Ожидайте.", 10, 70, 20);
            updateToolsInventory();
            addRespawnedItems();
            PlayerInv.clear(p);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.teleport(p.getTeam().getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(plugin, player);
                    player.showPlayer(plugin, p);
                }
                player.setCanPickupItems(true);
                PlayerInv.setPlayingInventory(p);
            }, 100);
        }
    }


    private void updateToolsInventory() {
        PlayerInventory inv = p.getPlayer().getInventory();
        for (LinkedList<ShopItem> list : ShopItems.TOOLS.values()){
            if (list.contains(ShopItem.SHEARS) || list.contains(ShopItem.FISHING_ROD)) continue;

            for (ShopItem item : list) {
                if (!PlayerInv.hasShopItem(inv, item)) continue;
                p.getShopInventory(ShopItem.TOOLS).setItem(ShopItems.getIndex(ShopItems.TOOLS, item), item.getItem());

                int index = list.indexOf(item) - 1;
                if (index >= 0) p.getRespawnItems().add(list.get(index).getItem());
                break;
            }
        }
    }

    private void addRespawnedItems() {
        PlayerInventory inv = p.getPlayer().getInventory();

        List<ShopItem> respawnedItems = List.of(
                ShopItem.CHAINMAIL_BOOTS, ShopItem.IRON_BOOTS,
                ShopItem.DIAMOND_BOOTS, ShopItem.FISHING_ROD,
                ShopItem.WOODEN_AXE, ShopItem.SHEARS,
                ShopItem.WOODEN_PICKAXE, ShopItem.LEATHER_HELMET,
                ShopItem.LEATHER_CHESTPLATE, ShopItem.LEATHER_LEGGINGS,
                ShopItem.LEATHER_BOOTS
        );

        for (ShopItem item : respawnedItems) {
            if (!PlayerInv.hasShopItem(inv, item)) continue;
            p.getRespawnItems().add(item.getItem());
        }

        p.getRespawnItems().add(ShopItem.WOODEN_SWORD.getItem());
    }

    private boolean hasKiller() {
        return p.getLastDamager().get() != null;
    }

    private String killWithKiller(boolean isFinal) {
        if (Utils.isUUID(p.getLastDamager().get())) {
            Participant killer = plugin.getPlayer(UUID.fromString(p.getLastDamager().get()));

            if (isFinal) killer.increaseFinalKills();
            else killer.increaseKilledPlayers();

            giveKillerResources(killer);

            String health = MineColor.RED + (new DecimalFormat("##.#").format(killer.getPlayer().getHealth()));

            return "§7 был зверски убит "
                    + PlayerManager.getCodeColor(killer) + killer.getPlayer().getName()
                    + MineColor.LIGHT_GRAY + " ("
                    + health
                    + MineColor.LIGHT_GRAY + ")";
        } else {
            return "§7 был зверски убит " + p.getLastDamager().get();
        }
    }

    public String killWithoutKiller(boolean isFinal) {
        if (isFinal) {
            Participant destroyer = p.getTeam().getBedDestroyer();
            if (destroyer != null) destroyer.increaseFinalKills();
        }
        return switch (cause) {
            case VOID -> "§7 упал в бездну.";
            case FALL -> "§7 разбился.";
            default -> "§7 умер.";
        };
    }

    private void giveKillerResources(Participant killer) {

        Player bukkitKiller = killer.getPlayer();
        if (bukkitKiller != null && bukkitKiller.isOnline()) {
            List<Material> resources = Arrays.asList(
                    Material.IRON_INGOT, Material.GOLD_INGOT,
                    Material.DIAMOND, Material.EMERALD
            );

            for (Material mat : resources) {
                p.getPlayer().getInventory().all(mat).values().forEach(res -> bukkitKiller.getInventory().addItem(res));
            }
        }
    }
}
