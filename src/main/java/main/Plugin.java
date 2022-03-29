package main;

import com.hoshion.mongoapi.MongoService;
import events.*;
import game.Game;
import game.Participant;
import game.PlayerKiller;
import game.Team;
import inventories.*;
import jedis.RedisSubscription;
import loading.Sidebar;
import loading.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.entity.*;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import redis.clients.jedis.Jedis;
import tab.Tab;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Plugin extends JavaPlugin {

    public static final String JedisChannel = "bw";
    public static final String PluginName = "BedWarsPlugin";
    private Tab tab;
    private Scoreboard scoreboard;
    private Game game;
    public Waiting waiting;
    private boolean isLoading = false;
    private boolean isWorking = false;
    public TeamSelectionInventory choose_team;
    public HashMap<String, Team> teams = new HashMap<>();
    public HashMap<UUID, Participant> players;
    private Sidebar sidebar;
    private Jedis jedis;

    public boolean isLoading() {return this.isLoading;}

    public boolean isWorking() {return this.isWorking; }

    public void setLoading(boolean isLoading) {this.isLoading = isLoading;}

    public void setWorking(boolean isWorking) {this.isWorking = isWorking;}

    public Scoreboard getScoreboard() { return this.scoreboard; }

    public Collection<Team> getTeams() {
        return this.teams.values();
    }

    public Team getTeam(String color){
        return this.teams.get(color);
    }

    public void addTeam(Team team) {
        this.teams.put(team.getColor(), team);
    }

    public void removePlayer(Entity p) {
        players.remove(p.getUniqueId());
    }

    public Collection<Participant> getPlayers() {
        return this.players.values();
    }

    public Participant getPlayer(Entity p){
        return getPlayer(p.getUniqueId());
    }

    public Participant getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void addPlayer(Participant p) {
        players.put(p.getPlayer().getUniqueId(), p);
    }

    public Game getGame(){ return this.game; }

    public Tab getTab(){
        return this.tab;
    }

    public TeamSelectionInventory getTeamSelectionInventory() {return this.choose_team; }

    public Waiting getWaiting(){ return this.waiting; }
    
    public int getOnlinePlayers() {return Bukkit.getOnlinePlayers().size();}

    public Jedis getJedis(){return this.jedis;}

    @Override
    public void onEnable(){
    	this.players = new HashMap<>();
        File worlds = new File("./", "worlds");
        for(int i = 0; i < worlds.list().length; i++){
            File w = new File("./worlds/", worlds.list()[i]);
            if(w.isFile()) continue;
            Bukkit.unloadWorld(worlds.list()[i], false);
            WorldCreator world = new WorldCreator(worlds.list()[i]);
            World wrld =  Bukkit.getServer().createWorld(world);
            wrld.setAutoSave(false);
            wrld.getEntities().forEach(ent -> {
            	if (ent instanceof LivingEntity || ent instanceof CraftItem) {
            		ent.remove();
            	}
            });
        }

        ShopItem.init();
        this.reloadWorld();
        PlayerKiller.createInstance(this);
        Config.createInstance(this);
        this.game = new Game(this);
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.choose_team = new TeamSelectionInventory(new TeamSelection(this), 27, "Выбор команды", this);
        this.sidebar = new Sidebar(this);
        this.waiting = new Waiting(this);
        this.tab = new Tab(this);

        this.loadEvents();
        Stats.createInstance(this);
        MongoService.createInstance();
        this.loadJedis();

        for(Player player : this.getServer().getOnlinePlayers()){

            PlayerJoinEvent event = new PlayerJoinEvent(player, "wow");
            onPlayerJoin executor = new onPlayerJoin(this);

            try {
                executor.execute(executor, event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        }
        getLogger().info("enabled!");
        jedis.publish("plugin_reload", PluginName + "," + Config.getServerName());
    }

    public void reloadGame() {
        Logger.getLogger("").info("Game is stopping");
        this.setWorking(false);
        this.reloadWorld();
        Config.reloadValues();
        this.game = new Game(this);
        Config.loadTeams(this);
        this.getSidebar().fillWaitingList();
        this.setLoading(true);
        this.getJedis().publish("bw", Config.getServerName() + " " + this.getOnlinePlayers());
        //this.messenger.stop();
    }

    public void reloadWorld(){
        Bukkit.unloadWorld(Bukkit.getWorld("world"), false);

        System.gc();

        try{
            File world = new File(Bukkit.getWorldContainer(), "world");
            File srcDir = new File("./presets/", "world");
            FileUtils.deleteDirectory(world);
            world.mkdir();
            FileUtils.copyDirectory(srcDir, world);
        } catch(IOException e){
            e.printStackTrace();
        }

        WorldCreator wc = new WorldCreator("world");
        Bukkit.getServer().createWorld(wc);
    }

    public void onDisable(){
        getLogger().info("disabled!");
        Bukkit.getOnlinePlayers().forEach(Utils::connectToHub);
        this.reloadGame();
    }
    
    private void loadJedis(){
        this.jedis = new Jedis("127.0.0.1", 6379);
        this.jedis.publish(Plugin.JedisChannel, Config.getServerName() + " 0");
        new RedisSubscription(this, Config.getServerName());
    }

    private void loadEvents(){

        onAsyncPlayerChat onAsyncPlayerChat = new onAsyncPlayerChat(this);
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, onAsyncPlayerChat, EventPriority.NORMAL, onAsyncPlayerChat, this);

        onBlockBreak onBlockBreak = new onBlockBreak(this);
        Bukkit.getPluginManager().registerEvent(BlockBreakEvent.class, onBlockBreak, EventPriority.NORMAL, onBlockBreak, this);

        onBlockFromTo onBlockFromTo = new onBlockFromTo(this);
        Bukkit.getPluginManager().registerEvent(BlockFromToEvent.class, onBlockFromTo, EventPriority.NORMAL, onBlockFromTo, this);

        onBlockPhysics onBlockPhysics = new onBlockPhysics(this);
        Bukkit.getPluginManager().registerEvent(BlockPhysicsEvent.class, onBlockPhysics, EventPriority.NORMAL, onBlockPhysics, this);

        onBlockPlace onBlockPlace = new onBlockPlace(this);
        Bukkit.getPluginManager().registerEvent(BlockPlaceEvent.class, onBlockPlace, EventPriority.NORMAL, onBlockPlace, this);

        onBlockSpread onBlockSpread = new onBlockSpread(this);
        Bukkit.getPluginManager().registerEvent(BlockSpreadEvent.class, onBlockSpread, EventPriority.NORMAL, onBlockSpread, this);

        onEntityDamage onEntityDamage = new onEntityDamage(this);
        Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, onEntityDamage, EventPriority.NORMAL, onEntityDamage, this);

        onEntityDamageByEntity onEntityDamageByEntity = new onEntityDamageByEntity(this);
        Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, onEntityDamageByEntity, EventPriority.NORMAL, onEntityDamageByEntity, this);

        onEntityDeath onEntityDeath = new onEntityDeath(this);
        Bukkit.getPluginManager().registerEvent(EntityDeathEvent.class, onEntityDeath, EventPriority.NORMAL, onEntityDeath, this);

        onEntityExplode onEntityExplode = new onEntityExplode(this);
        Bukkit.getPluginManager().registerEvent(EntityExplodeEvent.class, onEntityExplode, EventPriority.NORMAL, onEntityExplode, this);

        onEntityPickupItem onEntityPickupItem = new onEntityPickupItem(this);
        Bukkit.getPluginManager().registerEvent(EntityPickupItemEvent.class, onEntityPickupItem, EventPriority.NORMAL, onEntityPickupItem, this);

        onEntityTarget onEntityTarget = new onEntityTarget(this);
        Bukkit.getPluginManager().registerEvent(EntityTargetEvent.class, onEntityTarget, EventPriority.NORMAL, onEntityTarget, this);

        onExplosionPrime onExplosionPrime = new onExplosionPrime(this);
        Bukkit.getPluginManager().registerEvent(ExplosionPrimeEvent.class, onExplosionPrime, EventPriority.NORMAL, onExplosionPrime, this);

        onFoodLevelChange onFoodLevelChange = new onFoodLevelChange(this);
        Bukkit.getPluginManager().registerEvent(FoodLevelChangeEvent.class, onFoodLevelChange, EventPriority.NORMAL, onFoodLevelChange, this);

        onInventoryClick onInventoryClick = new onInventoryClick(this);
        Bukkit.getPluginManager().registerEvent(InventoryClickEvent.class, onInventoryClick, EventPriority.NORMAL, onInventoryClick, this);

        onInventoryDrag onInventoryDrag = new onInventoryDrag(this);
        Bukkit.getPluginManager().registerEvent(InventoryDragEvent.class, onInventoryDrag, EventPriority.NORMAL, onInventoryDrag, this);

        onInventoryOpen onInventoryOpen = new onInventoryOpen(this);
        Bukkit.getPluginManager().registerEvent(InventoryOpenEvent.class, onInventoryOpen, EventPriority.NORMAL, onInventoryOpen, this);

        onPlayerDeath onPlayerDeath = new onPlayerDeath(this);
        Bukkit.getPluginManager().registerEvent(PlayerDeathEvent.class, onPlayerDeath, EventPriority.NORMAL, onPlayerDeath, this);

        onPlayerDropItem onPlayerDropItem = new onPlayerDropItem(this);
        Bukkit.getPluginManager().registerEvent(PlayerDropItemEvent.class, onPlayerDropItem, EventPriority.NORMAL, onPlayerDropItem, this);

        onPlayerFish onPlayerFish = new onPlayerFish(this);
        Bukkit.getPluginManager().registerEvent(PlayerFishEvent.class, onPlayerFish, EventPriority.NORMAL, onPlayerFish, this);

        onPlayerInteract onPlayerInteract = new onPlayerInteract(this);
        Bukkit.getPluginManager().registerEvent(PlayerInteractEvent.class, onPlayerInteract, EventPriority.NORMAL, onPlayerInteract, this);

        onPlayerInteractEntity onPlayerInteractEntity = new onPlayerInteractEntity(this);
        Bukkit.getPluginManager().registerEvent(PlayerInteractEntityEvent.class, onPlayerInteractEntity, EventPriority.NORMAL, onPlayerInteractEntity, this);

        onPlayerItemConsume onPlayerItemConsume = new onPlayerItemConsume(this);
        Bukkit.getPluginManager().registerEvent(PlayerItemConsumeEvent.class, onPlayerItemConsume, EventPriority.NORMAL, onPlayerItemConsume, this);

        onPlayerJoin onPlayerJoin = new onPlayerJoin(this);
        Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, onPlayerJoin, EventPriority.NORMAL, onPlayerJoin, this);

        onPlayerMove onPlayerMove = new onPlayerMove(this);
        Bukkit.getPluginManager().registerEvent(PlayerMoveEvent.class, onPlayerMove, EventPriority.NORMAL, onPlayerMove, this);

        onPlayerQuit onPlayerQuit = new onPlayerQuit(this);
        Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, onPlayerQuit, EventPriority.NORMAL, onPlayerQuit, this);

        onPlayerRespawn onPlayerRespawn = new onPlayerRespawn(this);
        Bukkit.getPluginManager().registerEvent(PlayerRespawnEvent.class, onPlayerRespawn, EventPriority.NORMAL, onPlayerRespawn, this);

        onPrepareItemCraft onPrepareItemCraft = new onPrepareItemCraft(this);
        Bukkit.getPluginManager().registerEvent(PrepareItemCraftEvent.class, onPrepareItemCraft, EventPriority.NORMAL, onPrepareItemCraft, this);

        onProjectileHit onProjectileHit = new onProjectileHit(this);
        Bukkit.getPluginManager().registerEvent(ProjectileHitEvent.class, onProjectileHit, EventPriority.NORMAL, onProjectileHit, this);
    }

    public Sidebar getSidebar() {
        return sidebar;
    }
}
