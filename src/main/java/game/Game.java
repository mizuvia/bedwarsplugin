package game;

import inventories.*;
import main.Config;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;

import util.PlayerInv;
import util.WorldManager;

import java.util.*;
import java.util.logging.Logger;

public class Game {

    public static final Map<UUID, Participant> PLAYERS = new HashMap<>();

    private int matchTime = 0;
    private final List<Block> blockList = new ArrayList<>();
    private final List<Location> inaccessibleBlocks = new ArrayList<>();
    private final Plugin plugin;
    private final Time time;
    private final ArmorStandsManager armorStandsManager;
    private final List<Villager> villagers = new ArrayList<>();
    private final List<Inventory> chests = new ArrayList<>();
    private final Map<Integer, SimpleInventory> inventories = new HashMap<>();
    private int deadTeams = 0;
    private Messenger messenger;
    public static final int MAXIMUM_BUILD_HEIGHT = 100;
    
    public List<Inventory> getChestsInventories() { return this.chests; }

    public int getMatchTime(){ return this.matchTime; }

    public void increaseMatchTime(int newMatchTime){ this.matchTime += newMatchTime; }

    public Plugin getPlugin(){ return this.plugin; }

    public List<Block> getBlockList(){ return this.blockList; }

    public List<Location> getInaccessibleBlocks() { return this.inaccessibleBlocks; }

    public ArmorStandsManager getArmorStandsManager() {return this.armorStandsManager; }

    public Time getTime() {return this.time; }

    public List<Villager> getVillagers() { return this.villagers; }

    public Game(Plugin plugin){
        this.plugin = plugin;

        this.time = new Time(this);
        this.armorStandsManager = new ArmorStandsManager(this);

        this.time.startTask(plugin);
        this.armorStandsManager.startTask(plugin);

        createInventories();
    }

    public Map<Integer, SimpleInventory> getInventories() {
        return inventories;
    }

    public void createInventories() {
        inventories.put(10, new SimpleInventory(plugin, "Блоки", ShopItems.BLOCKS));
        inventories.put(11, new SimpleInventory(plugin, "Мечи", ShopItems.SWORDS));
        inventories.put(13, new SimpleInventory(plugin, "Луки", ShopItems.BOWS));
        inventories.put(15, new SimpleInventory(plugin, "Зелья", ShopItems.POTIONS));
        inventories.put(16, new SimpleInventory(plugin, "Разное", ShopItems.OTHERS));
    }

    public void start() {
        spawnUpgradeEntity();
        spawnShopEntity();
        plugin.getSidebar().fillPlayingList();
        armorStandsManager.createArmorStands();
        setInaccessibleBlocks();
        teleportPlayers();
        plugin.setWorking(true);
        plugin.getTab().updateTabs();
        checkEmptyTeams();
        Logger.getLogger("").info("Game started successfully");
        this.messenger = new Messenger(getPlugin(), 12000);
        this.messenger.addMessage("§c§lТимерство запрещено!");
    }

    private void setInaccessibleBlocks() {
        for (double x = -1; x <= 1; x++){
            for (double y = -1; y <= 2; y++){
                for (double z = -1; z <= 1; z++){
                    for (Team team : plugin.getTeams().values()) {
                        addToInaccessibleBlocks(team.getSpawnLocation(), x, y, z);
                        addToInaccessibleBlocks(team.getShopVillager(), x, y, z);
                        addToInaccessibleBlocks(team.getUpgradesVillager(), x, y, z);
                        addToInaccessibleBlocks(team.getResourceLocation(), x, y, z);
                    }
                    for (ArmorStands as : getArmorStandsManager().getDiamondArmorStands()) {
                        addToInaccessibleBlocks(as.getStage().getLocation(), x, y, z);
                    }
                    for (ArmorStands as : getArmorStandsManager().getEmeraldArmorStands()) {
                        addToInaccessibleBlocks(as.getStage().getLocation(), x, y, z);
                    }
                }
            }
        }
    }

    private void addToInaccessibleBlocks(Location loc, double x, double y, double z){
        getInaccessibleBlocks().add(new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z));
    }

    private void checkEmptyTeams() {
        for(Team team : plugin.getTeams().values()){
            if(team.getAliveTeammates() == 0) {
                team.setBroken(true);
                WorldManager.getBlock(team.getBedBottomLocation()).setType(Material.AIR);
                WorldManager.getBlock(team.getBedTopLocation()).setType(Material.AIR);
            }
        }
    }

    public void checkWin(){
        if (Config.getTeamsAmount() - 1 == this.getDeadTeams()) {
            time.finishGame(Time.FinishReason.WIN);
        }
    }

    public void teleportPlayers(){
        for(Participant participant : this.getPlugin().getPlayers().values()) {
            participant.getPlayer().setGameMode(GameMode.SURVIVAL);
            //participant.getPlayer().setDisplayName(PlayerManager.getCodeColor(participant) + participant.getTeam().getName().charAt(4) + " | " + participant.getPlayer().getName());
            participant.getPlayer().teleport(participant.getTeam().getSpawnLocation());
            PlayerInv.setPlayingInventory(participant);

            participant.giveItem(ShopItem.LEATHER_HELMET.getItem());
            participant.giveItem(ShopItem.LEATHER_CHESTPLATE.getItem());
            participant.giveItem(ShopItem.LEATHER_LEGGINGS.getItem());
            participant.giveItem(ShopItem.LEATHER_BOOTS.getItem());
            participant.giveItem(ShopItem.WOODEN_SWORD.getItem());
        }
    }

    public void spawnUpgradeEntity(){
        for(Team team : this.getPlugin().getTeams().values()){
            Villager villager = createVillager(team.getUpgradesVillager());

            villager.setCustomName("§e§lУЛУЧШЕНИЯ");
            this.getVillagers().add(villager);
        }
    }

    public void spawnShopEntity(){
        for(Team team : this.getPlugin().getTeams().values()) {
            Villager villager = createVillager(team.getShopVillager());

            villager.setCustomName("§e§lМАГАЗИН");
            this.getVillagers().add(villager);
        }
    }

    private Villager createVillager(Location loc) {
        Villager villager = (Villager) Bukkit.getWorld("world").spawnEntity(loc, EntityType.VILLAGER);

        surroundVillager(loc);

        villager.setVillagerType(Villager.Type.DESERT);
        villager.setInvulnerable(true);
        villager.setCanPickupItems(false);
        villager.setGliding(false);
        villager.setCollidable(false);
        villager.setSilent(true);
        villager.setAI(false);
        villager.setCustomNameVisible(true);
        villager.setRemoveWhenFarAway(false);

        return villager;
    }

    private void surroundVillager(Location loc) {
        for(int x = -2; x <= 2; x++){
            for(int z = -2; z <= 2; z++){
                for(int y = -1; y <= 3; y++){
                    inaccessibleBlocks.add(Bukkit.getWorld("world").getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z).getLocation());
                }
            }
        }
    }

    public void increaseDeadTeams() {
        this.deadTeams++;
        this.checkWin();
    }

    public int getDeadTeams() { return this.deadTeams; }
}
