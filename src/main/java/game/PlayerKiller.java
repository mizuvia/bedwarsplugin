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
import org.bukkit.inventory.ItemStack;
import util.PlayerInv;
import util.Utils;

import java.util.*;

public class PlayerKiller {

    private static Plugin plugin;
    private static Participant p;
    private static EntityDamageEvent.DamageCause cause;

    public static void createInstance(Plugin pl) {
        plugin = pl;
    }

    public static void killInGame(Player player, EntityDamageEvent.DamageCause c) {
        p = plugin.getPlayers().get(player.getUniqueId());
        cause = c;
        boolean isFinal = p.getTeam().isBroken();
        String finalMessage = isFinal ? " §b§lФинальное убийство!" : "";
        String deathMessage = hasKiller() ? killWithKiller(isFinal) : killWithoutKiller(isFinal);
        Utils.sendToAll(PlayerManager.getCodeColor(p) + player.getName() + deathMessage + finalMessage);

        player.setGameMode(GameMode.SPECTATOR);
        player.setCanPickupItems(false);
        player.setHealth(20.0);
        player.teleport(Config.getCenter());
        p.clearPotionEffects();

        if (isFinal) {
            player.sendTitle("§cВы умерли и больше не возродитесь", "§7Наблюдайте за игрой.", 10, 70, 20);
            player.setPlayerListName("§7Наблюдатель " + player.getName());
            p.getTeam().kill(p);
        } else {
            player.sendTitle("§cВы возродитесь через 5 секунд", "§7Ожидайте.", 10, 70, 20);
            updateToolsInventory();
            addRespawnedItems();
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


    private static void updateToolsInventory() {
        for (LinkedList<ShopItem> list : ShopItems.TOOLS.values()){
            if (list.contains(ShopItem.SHEARS) || list.contains(ShopItem.FISHING_ROD)) continue;

            ListIterator<ShopItem> it = list.listIterator(list.size());
            while (it.hasPrevious()){
                ShopItem item = it.previous();
                if (p.getPlayer().getInventory().all(item.getMaterial()).size() == 0) continue;

                p.getShopInventory(ShopItem.TOOLS).setItem(ShopItems.getIndex(ShopItems.TOOLS, item), item.getItem());
                if (it.hasPrevious()) p.getRespawnItems().add(it.previous().getItem());
                break;
            }
        }
    }

    private static void addRespawnedItems() {
        for (ItemStack item : p.getPlayer().getInventory().getContents()) {
            if (item == null) continue;

            List<Material> respawnedItems = List.of(
                    Material.GOLDEN_BOOTS, Material.CHAINMAIL_BOOTS,
                    Material.IRON_BOOTS, Material.DIAMOND_BOOTS,
                    Material.FISHING_ROD, Material.LEATHER_BOOTS,
                    Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS,
                    Material.LEATHER_HELMET, Material.SHEARS,
                    Material.WOODEN_PICKAXE, Material.WOODEN_AXE
            );

            if (respawnedItems.contains(item.getType())) {
                p.getRespawnItems().add(item);
            }
        }
    }

    private static boolean hasKiller() {
        return p.getLastDamager().get() != null;
    }


    private static String killWithKiller(boolean isFinal) {
        if (Utils.isUUID(p.getLastDamager().get())) {
            Participant killer = plugin.getPlayers().get(UUID.fromString(p.getLastDamager().get()));

            if (isFinal) killer.increaseFinalKills();
            else killer.increaseKilledPlayers();

            giveKillerResources(killer);

            return "§7 был зверски убит " + PlayerManager.getCodeColor(killer) + killer.getPlayer().getName();
        } else {
            return "§7 был зверски убит " + p.getLastDamager().get();
        }
    }

    public static String killWithoutKiller(boolean isFinal) {
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

    private static void giveKillerResources(Participant killer) {

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
