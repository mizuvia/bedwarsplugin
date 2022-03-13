package game;

import inventories.TeamUpgrades;
import inventories.TeamUpgradesInventory;
import inventories.Traps;
import inventories.TrapsInventory;
import main.Config;
import main.Plugin;
import org.bukkit.Location;
import org.bukkit.entity.IronGolem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Team {

    private final Plugin plugin;
    private final String color;
    private String name;
    private Location spawnLocation;
    private Location resourceLocation;
    private HashMap<String, Integer> teamUpgrades = new HashMap<>();
    public int silverTimeout = (int) (1.5 * 20);
    public int goldTimeout = 6 * 20;
    private boolean isBroken = false;
    private boolean isDead = false;
    private TeamUpgradesInventory upgradesInventory = new TeamUpgradesInventory(new TeamUpgrades(this), 27, "Улучшение команды");
    private TrapsInventory trapsInventory = new TrapsInventory(new Traps(this), 27, "Ловушки");
    private int teammates_amount = 0;
    private HashMap<String, Participant> teammates = new HashMap<>();
    private IronGolem golem;
    private List<String> traps = new ArrayList<>();
    private Participant bedDestroyer;
    private Location shopVillager;
    private Location upgradesVillager;
    private final SpawnResources spawnResources;
    private Location bedBottomLocation;
    private Location bedTopLocation;

    public void clearAfterGame(){
        this.silverTimeout = (int) (1.5 * 20);
        this.goldTimeout = 6 * 20;
        this.isBroken = false;
        this.isDead = false;
        this.trapsInventory = new TrapsInventory(new Traps(this), 27, "Ловушки");
        this.upgradesInventory = new TeamUpgradesInventory(new TeamUpgrades(this), 27, "Улучшение команды");
        this.teammates_amount = 0;
        this.traps = new ArrayList<>();
        this.teammates = new HashMap<>();
        this.bedDestroyer = null;
        this.golem = null;
        Config.reloadTeam(this);
        this.teamUpgrades = new HashMap<>();
        this.teamUpgrades.put("Sharpness", 0);
        this.teamUpgrades.put("Protection", 0);
        this.teamUpgrades.put("Haste", 0);
        this.teamUpgrades.put("Bedrock", 0);
        this.teamUpgrades.put("Forge", 0);
        this.teamUpgrades.put("Healing", 0);
        this.teamUpgrades.put("Trap", 0);
    }

    public Team(Plugin plugin, String color) {
        this.plugin = plugin;
        this.color = color;
        this.spawnResources = new SpawnResources(this);
        this.spawnResources.startTask();
        this.teamUpgrades.put("Sharpness", 0);
        this.teamUpgrades.put("Protection", 0);
        this.teamUpgrades.put("Haste", 0);
        this.teamUpgrades.put("Bedrock", 0);
        this.teamUpgrades.put("Forge", 0);
        this.teamUpgrades.put("Healing", 0);
        this.teamUpgrades.put("Trap", 0);
    }

    public List<String> getTraps(){return this.traps;}

    public TrapsInventory getTrapsInventory() {return this.trapsInventory; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){return this.name; }

    public void setBroken(boolean isBroken) {
        if(this.isBroken) return;
        this.isBroken = isBroken;
        this.getPlugin().getSidebar().decreaseTeammatesAmount(this);
        this.checkAlive();
    }

    public boolean isBroken() {return this.isBroken; }

    public String getColor(){
        return this.color;
    }

    public void setResourceLocation(Location loc){
        this.resourceLocation = loc;
    }

    public void setSpawnLocation(Location loc){
        this.spawnLocation = loc;
    }

    public Location getSpawnLocation(){
        return this.spawnLocation;
    }

    public Location getResourceLocation(){
        return this.resourceLocation;
    }

    public HashMap<String, Integer> getTeamUpgrades(){
        return this.teamUpgrades;
    }

    public TeamUpgradesInventory getUpgradesInventory(){
        return this.upgradesInventory;
    }

    public HashMap<String, Participant> getTeammates(){ return this.teammates; }

    public Plugin getPlugin()  {return this.plugin; }

    public void setSilverTimeout(int silverTimeout) { this.silverTimeout = silverTimeout; }

    public void setGoldTimeout(int goldTimeout) { this.goldTimeout = goldTimeout; }

    public void setIronGolem(IronGolem golem) {this.golem = golem;}

    public IronGolem getIronGolem() {return this.golem;}

    public int getTeammatesAmount(){return this.teammates_amount;}

    public void decreaseTeammatesAmount() {
        this.teammates_amount--;
        if(this.getPlugin().isWorking()){
            if(this.isBroken()) this.getPlugin().getSidebar().decreaseTeammatesAmount(this);
            this.checkAlive();
        }
    }

    public void increaseTeammatesAmount(){this.teammates_amount++;}

    public void setDead(boolean isDead) {
        this.isDead = isDead;
        this.getPlugin().getSidebar().setDead(this);
    }

    public boolean isDead() {return this.isDead;}

    public void setBedDestroyer(Participant bedDestroyer){ this.bedDestroyer = bedDestroyer; }

    public Participant getBedDestroyer() {return this.bedDestroyer; }

    public void checkAlive() {
        if(this.getTeammatesAmount() == 0) {
            this.setDead(true);
            this.getPlugin().getGame().increaseDeadTeams();
        }
    }

    public void setShopVillager(Location location) {
        this.shopVillager = location;
    }

    public void setUpgradesVillager(Location location) {
        this.upgradesVillager = location;
    }

    public Location getShopVillager() {
        return shopVillager;
    }

    public Location getUpgradesVillager() {
        return upgradesVillager;
    }

    public void addTeammate(Participant p) {
        this.getTeammates().put(p.getPlayer().getName(), p);
        this.increaseTeammatesAmount();
    }

    public void removeTeammate(Participant p) {
        this.getTeammates().remove(p.getPlayer().getName());
        this.decreaseTeammatesAmount();
    }

    public void setBedBottomLocation(Location location) {
        this.bedBottomLocation = location;
    }

    public Location getBedBottomLocation() {
        return bedBottomLocation;
    }

    public void setBedTopLocation(Location location) {
        this.bedTopLocation = location;
    }

    public Location getBedTopLocation() {
        return bedTopLocation;
    }

    public SpawnResources getSpawnResources() {
        return spawnResources;
    }
}
