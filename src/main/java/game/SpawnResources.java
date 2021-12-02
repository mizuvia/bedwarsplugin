package game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tasks.TaskGUI;
import util.Utils;
import util.WorldManager;

public class SpawnResources extends TaskGUI {

    private int silverCounter = (int) (1.5 * 20);
    private int goldCounter = 6 * 20;
    private static final int MAX_AMOUNT_OF_SILVER = 52;
    private static final int MAX_AMOUNT_OF_GOLD = 6;
    private final Team team;

    public SpawnResources(Team team){
        this.team = team;
        this.period = 1;
    }

    @Override
    public void run() {
        if(!team.getPlugin().isWorking()) return;

        spawnSilver();
        spawnGold();
    }

    public void spawnSilver(){
        if(team.silverCounter <= 0) {
            if(!WorldManager.canDropResource(team.getResourceLocation(), Material.IRON_INGOT, MAX_AMOUNT_OF_SILVER)) return;
            ItemStack silver = Utils.createItem(Material.IRON_INGOT, 1, "§eЖелезо");
            Bukkit.getServer().getWorld("world").dropItem(team.getResourceLocation(), silver);
            team.silverCounter = team.silverTimeout;
        }
        team.silverCounter--;
    }

    public void spawnGold(){
        if(team.goldCounter <= 0) {
            if(!WorldManager.canDropResource(team.getResourceLocation(), Material.GOLD_INGOT, MAX_AMOUNT_OF_GOLD)) return;
            ItemStack gold = Utils.createItem(Material.GOLD_INGOT, 1, "§eЗолото");
            Bukkit.getServer().getWorld("world").dropItem(team.getResourceLocation(), gold);
            team.goldCounter = team.goldTimeout;
        }
        team.goldCounter--;
    }

}
