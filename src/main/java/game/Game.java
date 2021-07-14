package game;

import com.hoshion.mongoapi.docs.Party;
import inventories.*;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import util.PlayerInv;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {

    private int matchTime = 0;
    private List<Block> blockList = new ArrayList<>();
    private final Plugin plugin;
    private final SpawnResources spawnResources;
    private final Time time;
    private final ArmorStandsManager armorStandsManager;
    private int diamondTimeout = 60;
    private int emeraldTimeout = 90;
    private final BlocksInventory blocksInventory = new BlocksInventory(new Blocks(this), 54, "Блоки");
    private final SwordsInventory swordsInventory = new SwordsInventory(new Swords(this), 54, "Мечи");
    private final OthersInventory othersInventory = new OthersInventory(new Others(this), 54, "Разное");
    private final BowsInventory bowsInventory = new BowsInventory(new Bows(this), 54, "Луки");
    private final PotionsInventory potionsInventory = new PotionsInventory(new Potions(this), 54, "Зелья");
    private List<Villager> villagers = new ArrayList<>();
    private List<ArmorStand> diamondArmorStands = new ArrayList<>();
    private List<ArmorStand> emeraldArmorStands = new ArrayList<>();
    private HashMap<String, String> playersDamagers = new HashMap<>();
    private final List<Inventory> chests = new ArrayList<>();
    private int deadTeams = 0;

    public SpawnResources getSpawnResources(){return this.spawnResources;}

    public List<Inventory> getChestsInventories() { return this.chests; }

    public HashMap<String, String> getPlayersDamagers() { return this.playersDamagers; }

    public List<ArmorStand> getDiamondArmorStands(){ return this.diamondArmorStands; }

    public List<ArmorStand> getEmeraldArmorStands(){ return this.emeraldArmorStands; }

    public BlocksInventory getBlocksInventory(){
        return this.blocksInventory;
    }

    public SwordsInventory getSwordsInventory(){
        return this.swordsInventory;
    }

    public BowsInventory getBowsInventory(){
        return this.bowsInventory;
    }

    public OthersInventory getOthersInventory(){
        return this.othersInventory;
    }

    public PotionsInventory getPotionsInventory(){
        return this.potionsInventory;
    }

    public int getMatchTime(){ return this.matchTime; }

    public void setMatchTime(int newMatchTime){ this.matchTime = newMatchTime; }

    public void increaseMatchTime(int newMatchTime){ this.matchTime += newMatchTime; }

    public Plugin getPlugin(){ return this.plugin; }

    public int getDiamondTimeout(){ return this.diamondTimeout; }

    public int getEmeraldTimeout(){ return this.emeraldTimeout; }

    public void setDiamondTimeout(int diamondTimeout){ this.diamondTimeout = diamondTimeout; }

    public void setEmeraldTimeout(int emeraldTimeout){ this.emeraldTimeout = emeraldTimeout; }

    public void resetMatchTime(){ this.matchTime = 0; }

    public List<Block> getBlockList(){ return this.blockList; }

    public ArmorStandsManager getArmorStandsManager() {return this.armorStandsManager; }

    public Time getTime() {return this.time; }

    public List<Villager> getVillagers() { return this.villagers; }

    public Game(Plugin plugin){
        this.plugin = plugin;

        this.spawnResources = new SpawnResources(this);
        this.time = new Time(this);
        this.armorStandsManager = new ArmorStandsManager(this);

        this.time.startTask();
        this.spawnResources.startTask();
        this.armorStandsManager.startTask();
    }

    public void start() {
        this.spawnUpgradeEntity();
        this.spawnShopEntity();
        this.createArmorStands();

        this.checkPlayersWithoutTeams();

        this.getPlugin().getSidebar().setForWorking();
        this.checkEmptyTeams();
        this.teleportPlayers();

        this.getPlugin().setWorking(true);
    }

    private void checkPlayersWithoutTeams(){
        for(Participant p : this.getPlugin().getPlayers().values()){
            if(p.hasTeam()) continue;

            Party party = this.getPlugin().getMongo().findOneParty("id", this.getPlugin().getMongo().findOnePlayer("uuid", p.getPlayer().getUniqueId().toString()).gen$party_id);
            if(party == null){
                for(Team team : this.getPlugin().getTeams().values())
                    if(team.getTeammatesAmount() != this.getPlugin().getPlayersPerTeam())
                        TeamSelection.addPlayerToTeam(this.getPlugin(), team, p);
            } else {
                for(Team team : this.getPlugin().getTeams().values()){
                    for(Participant par : team.getTeammates().values()){
                        if(party.members.contains(par.getPlayer().getUniqueId().toString()) && team.getTeammatesAmount() != this.getPlugin().getPlayersPerTeam()) {
                            TeamSelection.addPlayerToTeam(plugin, team, p);
                            break;
                        }
                    }
                }
                if(!p.hasTeam()){
                    for(Team team : this.getPlugin().getTeams().values()){
                        if(this.getPlugin().getPlayersPerTeam() - team.getTeammatesAmount() >= party.members.size()){
                            TeamSelection.addPlayerToTeam(plugin, team, p);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void checkEmptyTeams() {
        for(Team team : this.getPlugin().getTeams().values()){
            if(team.getTeammatesAmount() == 0) {
                team.setBroken(true);
            }
        }
    }

    public void stop(){

        this.getPlugin().setWorking(false);

        this.returnBeds();
        this.removeEntities();
        this.resetLists();
        this.clearChests();
        this.clearBlocks();
        this.resetMatchTime();
        this.resetTimeout();
        this.getSpawnResources().resetCounters();
        this.getArmorStandsManager().resetData();
        this.getTime().resetData();
        this.returnBeds();
        this.clearTeams();
        this.getPlugin().resetTeamSelection();
        this.getPlugin().getSidebar().clear();
        this.getPlugin().getSidebar().setForLoading();
        this.getPlugin().getWaiting().checkAmount();

        this.getPlugin().setLoading(true);
        this.getPlugin().getJedis().publish("bw", this.getPlugin().getConfig().getString("server_name") + " " + this.getPlugin().getOnlinePlayers());
    }

    public void checkWin(){
        if (this.getPlugin().getTeamsAmount() - 1 == this.getPlugin().getGame().getDeadTeams()) {
            this.getPlugin().getGame().getTime().finishGame(0);
        }
    }

    private void resetLists() {
        this.villagers = new ArrayList<>();
        this.diamondArmorStands = new ArrayList<>();
        this.emeraldArmorStands = new ArrayList<>();
        this.playersDamagers = new HashMap<>();
    }

    private void resetTimeout() {
        this.diamondTimeout = 60;
        this.emeraldTimeout = 90;
        this.deadTeams = 0;
    }

    private void clearTeams() {
        for(Team team : this.getPlugin().getTeams().values()){
            team.clearAfterGame();
        }
    }

    private void clearChests() {
        for(Inventory inventory : this.getChestsInventories()){
            inventory.clear();
        }
        for(Participant participant : this.getPlugin().getPlayers().values()){
            participant.getPlayer().getEnderChest().clear();
        }
    }

    private void clearBlocks() {
        for(Block block : this.getBlockList()){
            block.setType(Material.AIR);
        }

        this.blockList = new ArrayList<>();
    }

    private void removeEntities() {
        for(Entity ent : Bukkit.getServer().getWorld("world").getEntities()){
            switch (ent.getType()) {
                case DROPPED_ITEM:
                case ARMOR_STAND:
                case IRON_GOLEM:
                case VILLAGER: ent.remove();
                default: break;
            }
        }
    }

    public void teleportPlayers(){
        for(Participant participant : this.getPlugin().getPlayers().values()) {
            participant.getPlayer().setGameMode(GameMode.SURVIVAL);
            participant.getPlayer().setPlayerListName(PlayerManager.getCodeColor(participant) + participant.getTeam().getName().charAt(4) + " | " + participant.getPlayer().getName());
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
        }
    }

    public void spawnUpgradeEntity(){
        for(Team team : this.getPlugin().getTeams().values()){
            String cord = this.getPlugin().getConfig().getString("teams." + team.getColor() + ".upgrades_shop");
            Location loc = new Location(Bukkit.getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]), Double.parseDouble(cord.split(" ")[2]) + 0.5, Float.parseFloat(cord.split(" ")[3]), 0);
            Villager villager = (Villager) Bukkit.getWorld("world").spawnEntity(loc, EntityType.VILLAGER);

            villager.setVillagerType(Villager.Type.DESERT);
            villager.setCustomName("§e§lУЛУЧШЕНИЯ");
            villager.setInvulnerable(true);
            villager.setCanPickupItems(false);
            villager.setGliding(false);
            villager.setCollidable(false);
            villager.setSilent(true);
            villager.setAI(false);
            villager.setCustomNameVisible(true);
            villager.setRemoveWhenFarAway(false);

            this.getVillagers().add(villager);
        }
    }

    public void spawnShopEntity(){
        for(Team team : this.getPlugin().getTeams().values()) {
            String cord = this.getPlugin().getConfig().getString("teams." + team.getColor() + ".shop");
            Location loc = new Location(Bukkit.getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]), Double.parseDouble(cord.split(" ")[2]) + 0.5, Float.parseFloat(cord.split(" ")[3]), 0);
            Villager villager = (Villager) Bukkit.getWorld("world").spawnEntity(loc, EntityType.VILLAGER);

            villager.setVillagerType(Villager.Type.DESERT);
            villager.setCustomName("§e§lМАГАЗИН");
            villager.setInvulnerable(true);
            villager.setCanPickupItems(false);
            villager.setGliding(false);
            villager.setCollidable(false);
            villager.setSilent(true);
            villager.setAI(false);
            villager.setCustomNameVisible(true);
            villager.setRemoveWhenFarAway(false);

            this.getVillagers().add(villager);
        }
    }
    private void createArmorStands() {
        for(String diamondCord : this.getPlugin().getDiamonds()){
            double x = Double.parseDouble(diamondCord.split(" ")[0]) + 0.5;
            double y = Double.parseDouble(diamondCord.split(" ")[1]);
            double z = Double.parseDouble(diamondCord.split(" ")[2]) + 0.5;

            Location loc1 = new Location(Bukkit.getServer().getWorld("world"), x, y + 1.2, z);
            Location loc2 = new Location(Bukkit.getServer().getWorld("world"), x, y + 0.9, z);
            Location loc3 = new Location(Bukkit.getServer().getWorld("world"), x, y + 0.8, z);

            ArmorStand armor1 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc1, EntityType.ARMOR_STAND);
            ArmorStand armor2 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc2, EntityType.ARMOR_STAND);
            ArmorStand armor3 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc3, EntityType.ARMOR_STAND);

            armor1.setBasePlate(false);
            armor1.setAI(false);
            armor1.setVisible(false);
            armor1.setCustomNameVisible(true);
            armor1.setCustomName("§b§lАлмазы I");
            armor1.setRemoveWhenFarAway(false);
            armor1.setGravity(false);

            armor2.setBasePlate(false);
            armor2.setAI(false);
            armor2.setVisible(false);
            armor2.setCustomNameVisible(true);
            armor2.setCustomName("§e§lДо появления: 1:00");
            armor2.setRemoveWhenFarAway(false);
            armor2.setGravity(false);

            armor3.setBasePlate(false);
            armor3.setAI(false);
            armor3.setVisible(false);
            armor3.setHelmet(new ItemStack(Material.DIAMOND_BLOCK, 1));
            armor3.setRemoveWhenFarAway(false);
            armor3.setGravity(false);

            this.getDiamondArmorStands().add(armor1);
            this.getDiamondArmorStands().add(armor2);
            this.getDiamondArmorStands().add(armor3);
        }

        for(String emeraldCord : this.getPlugin().getEmeralds()){
            double x = Double.parseDouble(emeraldCord.split(" ")[0]) + 0.5;
            double y = Double.parseDouble(emeraldCord.split(" ")[1]);
            double z = Double.parseDouble(emeraldCord.split(" ")[2]) + 0.5;

            Location loc1 = new Location(Bukkit.getServer().getWorld("world"), x, y + 1.2, z);
            Location loc2 = new Location(Bukkit.getServer().getWorld("world"), x, y + 0.9, z);
            Location loc3 = new Location(Bukkit.getServer().getWorld("world"), x, y + 0.8, z);

            ArmorStand armor1 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc1, EntityType.ARMOR_STAND);
            ArmorStand armor2 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc2, EntityType.ARMOR_STAND);
            ArmorStand armor3 = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc3, EntityType.ARMOR_STAND);

            armor1.setBasePlate(false);
            armor1.setAI(false);
            armor1.setVisible(false);
            armor1.setCustomNameVisible(true);
            armor1.setCustomName("§a§lИзумруды I");
            armor1.setRemoveWhenFarAway(false);
            armor1.setGravity(false);

            armor2.setBasePlate(false);
            armor2.setAI(false);
            armor2.setVisible(false);
            armor2.setCustomNameVisible(true);
            armor2.setCustomName("§e§lДо появления: 1:30");
            armor2.setRemoveWhenFarAway(false);
            armor2.setGravity(false);

            armor3.setBasePlate(false);
            armor3.setAI(false);
            armor3.setVisible(false);
            armor3.setHelmet(new ItemStack(Material.EMERALD_BLOCK, 1));
            armor3.setRemoveWhenFarAway(false);
            armor3.setGravity(false);

            this.getEmeraldArmorStands().add(armor1);
            this.getEmeraldArmorStands().add(armor2);
            this.getEmeraldArmorStands().add(armor3);
        }
    }

    public void returnBeds() {
        for(String name : this.getPlugin().getTeamsNames()){
            String cords_bottom = this.getPlugin().getConfig().getString("teams." + name + ".bed_bottom");
            String cords_top = this.getPlugin().getConfig().getString("teams." + name + ".bed_top");

            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).setBlockData(this.getPlugin().getTeams().get(name).getBedBottom());
            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).setBlockData(this.getPlugin().getTeams().get(name).getBedTop());
        }
    }

    public void increaseDeadTeams() {
        this.deadTeams++;
        this.checkWin();
    }

    public int getDeadTeams() { return this.deadTeams; }
}
