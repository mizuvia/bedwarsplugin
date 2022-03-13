package events;

import game.Participant;
import inventories.ShopItem;
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

        if (isFinal) p.getTeam().decreaseTeammatesAmount();

        updateToolsInventory();
        addRespawnedItems();
        p.clearParticles();

        if (this.getPlugin().isWorking()) {
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
            player.setHealth(20.0);
            player.teleport(Config.getCenter());

            if (p.getTeam().isBroken()) {
//              this.getPlugin().getTab().removePlayerFromTabs(p);
                player.setPlayerListName("§7Наблюдатель " + player.getName());
            } else {
                player.sendTitle("§cВы возродитесь через 5 секунд", "§7Ожидайте.", 10, 70, 20);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getPlugin(), () -> {
                    player.teleport(p.getTeam().getSpawnLocation());
                    PlayerInv.setPlayingInventory(p);
                    player.setGameMode(GameMode.SURVIVAL);
                }, 100);
            }
        }
    }

    private void updateToolsInventory() {
        Map<Material, Material> tools = Map.of(
            Material.STONE_AXE, Material.WOODEN_AXE,
            Material.IRON_AXE, Material.STONE_AXE,
            Material.DIAMOND_AXE, Material.IRON_AXE,
            Material.GOLDEN_AXE, Material.DIAMOND_AXE,
            Material.STONE_PICKAXE, Material.WOODEN_PICKAXE,
            Material.IRON_PICKAXE, Material.STONE_PICKAXE,
            Material.DIAMOND_PICKAXE, Material.IRON_PICKAXE,
            Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE
        );

        tools.forEach((k, v) -> {
            if (player.getInventory().all(k).size() != 0) {
                p.getRespawnItems().add(Utils.clearItem(ShopItem.valueOf(v.name()).getItem()));
                p.getToolsInventory().setItem(k.name().matches(".*PICKAXE") ? 30 : 31, ShopItem.valueOf(k.name()).getItem());
            }
        });
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

            return "§7 был зверски убит." + PlayerManager.getCodeColor(killer) + killer.getPlayer().getName();
        } else {
            return "§7 был зверски убит." + p.getLastDamager().get();
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
