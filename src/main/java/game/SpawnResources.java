package game;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import tasks.TaskGUI;
import util.Utils;
import util.WorldManager;

public class SpawnResources extends TaskGUI {

    private static final int START_TIMEOUT_OF_SILVER = 30;
    private static final int START_TIMEOUT_OF_GOLD = 120;
    private int silverCounter = START_TIMEOUT_OF_SILVER;
    private int goldCounter = START_TIMEOUT_OF_GOLD;
    private static final int MAX_AMOUNT_OF_SILVER = 52;
    private static final int MAX_AMOUNT_OF_GOLD = 6;
    private final Team team;

    public SpawnResources(Team team){
        this.team = team;
        this.period = 1;
    }

    @Override
    public void execute() {
        if (!plugin.getTeams().contains(team)){
            this.cancel();
        }
        if (!team.getPlugin().isWorking()) return;

        spawnSilver();
        spawnGold();
    }

    public void spawnSilver(){
        if(silverCounter <= 0) {
            if(WorldManager.canDropResource(team.getResourceLocation(), Material.IRON_INGOT, MAX_AMOUNT_OF_SILVER)) {
                ItemStack silver = Utils.createItem(Material.IRON_INGOT, 1, "§eЖелезо");
                Item item = WorldManager.dropItem(team.getResourceLocation(), silver);
                item.setVelocity(new Vector());
            }
            silverCounter = team.silverTimeout;
        }
        silverCounter--;
    }

    public void spawnGold(){
        if(goldCounter <= 0) {
            if(WorldManager.canDropResource(team.getResourceLocation(), Material.GOLD_INGOT, MAX_AMOUNT_OF_GOLD)) {
                ItemStack gold = Utils.createItem(Material.GOLD_INGOT, 1, "§eЗолото");
                Item item = WorldManager.dropItem(team.getResourceLocation(), gold);
                item.setVelocity(new Vector());
            }
            goldCounter = team.goldTimeout;
        }
        goldCounter--;
    }

    public void resetCounter(){
        this.silverCounter = START_TIMEOUT_OF_SILVER;
        this.goldCounter = START_TIMEOUT_OF_GOLD;
    }

}
