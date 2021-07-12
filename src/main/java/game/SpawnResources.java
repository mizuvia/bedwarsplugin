package game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tasks.TaskGUI;

import java.util.List;

public class SpawnResources extends TaskGUI {

    private int diamondCounter = 61;
    private int emeraldCounter = 91;
    private final Game game;

    public SpawnResources(Game game){
        this.game = game;
        this.period = 20;
    }

    @Override
    public void run() {
        if(!this.getGame().getPlugin().isWorking()) return;

        spawnBronze();
        spawnSilver();
        spawnGold();
        spawnDiamond();
        spawnEmerald();
    }

    public void spawnBronze(){
        for(String color : this.getGame().getPlugin().getTeamsNames()){
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
        for(String color : this.getGame().getPlugin().getTeamsNames()){
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
        for(String color : this.getGame().getPlugin().getTeamsNames()){
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

    public void spawnDiamond(){

        if(this.diamondCounter == 1) {
            List<String> diamonds = this.getGame().getPlugin().getDiamonds();
            for (String cord : diamonds) {
                Location loc = new Location(this.getGame().getPlugin().getServer().getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]) + 1.0, Double.parseDouble(cord.split(" ")[2]) + 0.5);
                ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
                ItemMeta dmeta = diamond.getItemMeta();
                dmeta.setDisplayName("§eАлмаз");
                diamond.setItemMeta(dmeta);
                Bukkit.getServer().getWorld("world").dropItem(loc, diamond);
            }
            this.diamondCounter = this.getGame().getDiamondTimeout() + 1;
        }

        this.diamondCounter--;
    }

    public void spawnEmerald(){

        if(this.emeraldCounter == 1) {
            List<String> emeralds = this.getGame().getPlugin().getEmeralds();
            for (String cord : emeralds) {
                Location loc = new Location(this.getGame().getPlugin().getServer().getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]) + 1.0, Double.parseDouble(cord.split(" ")[2]) + 0.5);
                ItemStack emerald = new ItemStack(Material.EMERALD, 1);
                ItemMeta emeta = emerald.getItemMeta();
                emeta.setDisplayName("§eИзумруд");
                emerald.setItemMeta(emeta);
                Bukkit.getServer().getWorld("world").dropItem(loc, emerald);
            }
            this.emeraldCounter = this.getGame().getEmeraldTimeout() + 1;
        }
        this.emeraldCounter--;
    }

    public Game getGame() {return this.game;}

    public void resetCounters(){
        this.emeraldCounter = 91;
        this.diamondCounter = 61;
    }

}
