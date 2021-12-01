package game;

import main.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tasks.TaskGUI;

import java.util.List;

public class SpawnResources extends TaskGUI {

    private int silverCounter = (int) (1.5 * 20);
    private int goldCounter = 6 * 20;
    private final Game game;

    public SpawnResources(Game game){
        this.game = game;
        this.period = 1;
    }

    @Override
    public void run() {
        if(!this.getGame().getPlugin().isWorking()) return;

        spawnBronze();
        spawnSilver();
        spawnGold();
    }

    public void spawnBronze(){
        for(String color : Config.getTeamsNames()){
            Team team = this.getGame().getPlugin().getTeams().get(color);
            int amount = 1 + team.getTeamUpgrades().get("Forge");

            ItemStack bronze = new ItemStack(Material.BRICK, amount);
            ItemMeta bmeta = bronze.getItemMeta();
            bmeta.setDisplayName("§eБронза");
            bronze.setItemMeta(bmeta);
            Bukkit.getServer().getWorld("world").dropItem(team.getResourceLocation(), bronze);
        }
    }

    public void spawnSilver(){
        for(String color : Config.getTeamsNames()){
            Team team = this.getGame().getPlugin().getTeams().get(color);
            if(team.silverCounter >= team.silverTimeout) {
                int amount = (team.getTeamUpgrades().get("Forge") == 3) ? 2 : 1;
                ItemStack silver = new ItemStack(Material.IRON_INGOT, amount);
                ItemMeta smeta = silver.getItemMeta();
                smeta.setDisplayName("§eЖелезо");
                silver.setItemMeta(smeta);
                Bukkit.getServer().getWorld("world").dropItem(team.getResourceLocation(), silver);
                team.silverCounter = 0;
            }
            team.silverCounter++;
        }
    }

    public void spawnGold(){
        for(String color : Config.getTeamsNames()){
            Team team = this.getGame().getPlugin().getTeams().get(color);
            if(team.goldCounter >= team.goldTimeout) {
                ItemStack gold = new ItemStack(Material.GOLD_INGOT, 1);
                ItemMeta gmeta = gold.getItemMeta();
                gmeta.setDisplayName("§eЗолото");
                gold.setItemMeta(gmeta);
                Bukkit.getServer().getWorld("world").dropItem(team.getResourceLocation(), gold);
                team.goldCounter = 0;
            }
            team.goldCounter++;
        }
    }

    public Game getGame() {return this.game;}

}
