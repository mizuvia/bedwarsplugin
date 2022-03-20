package events;

import game.Participant;
import game.PlayerKiller;
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
            if(this.getPlugin().isLoading()) PlayerInv.setWaitingInventory(p);
            e.getEntity().teleport(WorldManager.centralizeLocation(Bukkit.getWorld("waiting").getSpawnLocation()));
        } else {
            PlayerKiller.killInGame(player, e.getCause());
        }
    }
}
