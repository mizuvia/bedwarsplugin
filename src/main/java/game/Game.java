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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import game.Messenger.Message;
import util.PlayerInv;

import java.util.*;

public class Game {

    public static final Map<UUID, Participant> PLAYERS = new HashMap<>();

    private int matchTime = 0;
    private List<Block> blockList = new ArrayList<>();
    private List<Location> inaccessibleBlocks = new ArrayList<>();
    private final Plugin plugin;
    private final Time time;
    private final ArmorStandsManager armorStandsManager;
    private List<Villager> villagers = new ArrayList<>();
    private final List<Inventory> chests = new ArrayList<>();
    private final Map<Integer, SimpleInventory> inventories = new HashMap<>();
    private int deadTeams = 0;
    private Messenger messenger;
    
    public List<Inventory> getChestsInventories() { return this.chests; }

    public int getMatchTime(){ return this.matchTime; }

    public void setMatchTime(int newMatchTime){ this.matchTime = newMatchTime; }

    public void increaseMatchTime(int newMatchTime){ this.matchTime += newMatchTime; }

    public Plugin getPlugin(){ return this.plugin; }

    public void resetMatchTime(){ this.matchTime = 0; }

    public List<Block> getBlockList(){ return this.blockList; }

    public List<Location> getInaccessibleBlocks() { return this.inaccessibleBlocks; }

    public ArmorStandsManager getArmorStandsManager() {return this.armorStandsManager; }

    public Time getTime() {return this.time; }

    public List<Villager> getVillagers() { return this.villagers; }

    public Game(Plugin plugin){
        this.plugin = plugin;

        this.time = new Time(this);
        this.armorStandsManager = new ArmorStandsManager(this);

        this.time.startTask();
        this.armorStandsManager.startTask();

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
        this.spawnUpgradeEntity();
        this.spawnShopEntity();
        this.getPlugin().getSidebar().fillPlayingList();
        this.getArmorStandsManager().createArmorStands();
        this.checkEmptyTeams();
        this.teleportPlayers();
        this.getPlugin().setWorking(true);
        this.messenger = new Messenger(getPlugin(), 12000);
        this.messenger.addMessage(new Message(new String[] {"§c§lТимерство запрещено!"}));
    }

    private void checkEmptyTeams() {
        for(Team team : this.getPlugin().getTeams().values()){
            if(team.getAliveTeammates() == 0) {
                team.setBroken(true);
            }
        }
    }

    public void stop(){
        this.getPlugin().setWorking(false);
        this.getPlugin().reloadWorld();
        Config.reloadValues();
        this.resetLists();
        this.resetMatchTime();
        this.resetTimeout();
        this.getArmorStandsManager().resetData();
        this.getTime().resetData();
        this.clearTeams();
        this.getPlugin().resetTeamSelection();
        this.getPlugin().setLoading(true);
        this.getPlugin().getJedis().publish("bw", Config.getServerName() + " " + this.getPlugin().getOnlinePlayers());
        this.messenger.stop();
    }

    public void checkWin(){
        if (Config.getTeamsAmount() - 1 == this.getDeadTeams()) {
            this.getPlugin().getGame().getTime().finishGame(Time.FinishReason.WIN);
        }
    }

    private void resetLists() {
        this.villagers = new ArrayList<>();
        this.inaccessibleBlocks = new ArrayList<>();
        this.blockList = new ArrayList<>();
    }

    private void resetTimeout() {
        this.deadTeams = 0;
    }

    private void clearTeams() {
        for(Team team : this.getPlugin().getTeams().values()){
            team.clearAfterGame();
        }
    }

    public void teleportPlayers(){
        for(Participant participant : this.getPlugin().getPlayers().values()) {
            participant.getPlayer().setGameMode(GameMode.SURVIVAL);
            participant.getPlayer().setPlayerListName(PlayerManager.getCodeColor(participant) + participant.getTeam().getName().charAt(4) + " | " + participant.getPlayer().getName());
            participant.getPlayer().setDisplayName(PlayerManager.getCodeColor(participant) + participant.getTeam().getName().charAt(4) + " | " + participant.getPlayer().getName());
            participant.getPlayer().teleport(participant.getTeam().getSpawnLocation());
            PlayerInv.setPlayingInventory(participant);

            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

            LeatherArmorMeta hm = (LeatherArmorMeta) helmet.getItemMeta();
            LeatherArmorMeta cm = (LeatherArmorMeta) chestplate.getItemMeta();
            LeatherArmorMeta lm = (LeatherArmorMeta) leggings.getItemMeta();
            LeatherArmorMeta bm = (LeatherArmorMeta) boots.getItemMeta();

            hm.setColor(PlayerManager.getColor(participant));
            cm.setColor(PlayerManager.getColor(participant));
            lm.setColor(PlayerManager.getColor(participant));
            bm.setColor(PlayerManager.getColor(participant));

            hm.setUnbreakable(true);
            cm.setUnbreakable(true);
            lm.setUnbreakable(true);
            bm.setUnbreakable(true);

            helmet.setItemMeta(hm);
            chestplate.setItemMeta(cm);
            leggings.setItemMeta(lm);
            boots.setItemMeta(bm);

            participant.getPlayer().getInventory().setHelmet(helmet);
            participant.getPlayer().getInventory().setChestplate(chestplate);
            participant.getPlayer().getInventory().setLeggings(leggings);
            participant.getPlayer().getInventory().setBoots(boots);
            ItemStack ws = ShopItemCreator.createShopItem(Material.WOODEN_SWORD, 1, "§eДеревянный меч", null);
            participant.getPlayer().getInventory().addItem(ws);
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
