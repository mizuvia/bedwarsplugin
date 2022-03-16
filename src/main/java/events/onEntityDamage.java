package events;

import game.Participant;
import inventories.ShopItem;
import inventories.ShopItems;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import util.PlayerInv;
import util.Utils;
import util.WorldManager;

import java.util.*;

public class onEntityDamage extends SimpleListener implements Listener, EventExecutor {

    public onEntityDamage(Plugin plugin) {
        super(plugin);
    }
    private EntityDamageEvent e;
    private Player player;
    private Participant p;

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {

        this.e = (EntityDamageEvent) event;

        if(!(e.getEntity() instanceof Player player)) return;
        if(e.getFinalDamage() < player.getHealth()) return;

        e.setCancelled(true);

        this.p = getPlugin().getPlayers().get(player.getUniqueId());
        this.player = player;

        if (p == null) return;

        if (p.isInvisible()) {
            p.showArmor();
        }

        if(this.getPlugin().isLoading()) {
            if(this.getPlugin().isLoading()) PlayerInv.setWaitingInventory(this.getPlugin().getPlayers().get(e.getEntity().getUniqueId()));
            e.getEntity().teleport(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
        } else {
            killInGame();
        }
    }

    public void killInGame() {
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
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                player.setGameMode(GameMode.SURVIVAL);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(plugin, player);
                    player.showPlayer(plugin, p);
                }
                player.teleport(p.getTeam().getSpawnLocation());
                player.setCanPickupItems(true);
                PlayerInv.setPlayingInventory(p);
            }, 100);
        }
    }

    private void updateToolsInventory() {
        for (LinkedList<ShopItem> list : ShopItems.TOOLS.values()){
            if (list.contains(ShopItem.SHEARS) || list.contains(ShopItem.FISHING_ROD)) continue;

            ListIterator<ShopItem> it = list.listIterator(list.size() - 1);
            while (it.hasPrevious()){
                ShopItem item = it.previous();
                if (player.getInventory().all(item.getMaterial()).size() == 0) continue;

                p.getShopInventory(ShopItem.TOOLS).setItem(ShopItems.getIndex(ShopItems.TOOLS, item), item.getItem());
                if (it.hasPrevious()) p.getRespawnItems().add(it.previous().getItem());
                break;
            }
        }
    }

    private void addRespawnedItems() {
        for (ItemStack item : player.getInventory().getContents()) {
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

    private boolean hasKiller() {
        return p.getLastDamager().get() != null;
    }


    public String killWithKiller(boolean isFinal) {
        if (getPlugin().getPlayers().containsKey(UUID.fromString(p.getLastDamager().get()))) {
            Participant killer = getPlugin().getPlayers().get(UUID.fromString(p.getLastDamager().get()));

            if (isFinal) killer.increaseFinalKills();
            else killer.increaseKilledPlayers();

            giveKillerResources(killer);

            return "§7 был зверски убит " + PlayerManager.getCodeColor(killer) + killer.getPlayer().getName();
        } else {
            return "§7 был зверски убит " + p.getLastDamager().get();
        }
    }

    public String killWithoutKiller(boolean isFinal) {
        if (isFinal) {
            p.getTeam().getBedDestroyer().increaseFinalKills();
        }
        return switch (e.getCause()) {
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
                player.getInventory().all(mat).values().forEach(res -> bukkitKiller.getInventory().addItem(res));
            }
        }
    }
}
