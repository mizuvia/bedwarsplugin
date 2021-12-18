package main;

import com.hoshion.mongoapi.MongoService;
import events.*;
import game.Game;
import game.Participant;
import game.Team;
import inventories.*;
import jedis.RedisThread;
import loading.Sidebar;
import loading.Waiting;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftItem;
import org.bukkit.entity.*;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import redis.clients.jedis.Jedis;
import tab.Tab;
import util.PlayerInv;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Plugin extends JavaPlugin {

    public static final String JedisChannel = "bw";
    public static final String PluginName = "BedWarsPlugin";
    private Tab tab;
    private Scoreboard scoreboard;
    private Sidebar sidebar;
    private Game game;
    public Waiting waiting;
    private boolean isLoading = false;
    private boolean isWorking = false;
    public int online_players;
    public TeamSelectionInventory choose_team;
    public HashMap<String, Team> teams = new HashMap<>();
    public HashMap<String, Participant> players = new HashMap<>();
    private Jedis jedis;

    public boolean isLoading() {return this.isLoading;}

    public boolean isWorking() {return this.isWorking; }

    public void setLoading(boolean isLoading) {this.isLoading = isLoading;}

    public void setWorking(boolean isWorking) {this.isWorking = isWorking;}

    public Sidebar getSidebar() { return this.sidebar; }

    public Scoreboard getScoreboard() { return this.scoreboard; }

    public HashMap<String, Team> getTeams() {return this.teams; }

    public HashMap<String, Participant> getPlayers(){ return this.players; }

    public Game getGame(){ return this.game; }

    public Tab getTab(){
        return this.tab;
    }

    public TeamSelectionInventory getTeamSelectionInventory() {return this.choose_team; }

    public Waiting getWaiting(){ return this.waiting; }

    public void decreaseOnlinePlayers() { this.online_players--; }

    public void increaseOnlinePlayers() { this.online_players++; }

    public int getOnlinePlayers(){ return this.online_players; }

    public Jedis getJedis(){return this.jedis;}

    @Override
    public void onEnable(){
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

        this.reloadWorld();
        Config.createInstance(this);

        this.game = new Game(this);
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.choose_team = new TeamSelectionInventory(new TeamSelection(this), 27, "Выбор команды", this);
        this.sidebar = new Sidebar(this);
        for (Player p : Bukkit.getOnlinePlayers()) {
        	Bukkit.getLogger().info(p.getName());
        }
        this.online_players = this.getServer().getOnlinePlayers().size();
        this.waiting = new Waiting(this);
        this.tab = new Tab(this);

        this.loadEvents();
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
        Bukkit.getOnlinePlayers().forEach(p -> Utils.connectToHub(p));
        this.getGame().stop();
    }
    
    private void loadJedis(){
        Jedis subJedis = new Jedis("127.0.0.1", 6379);
        this.jedis = new Jedis("127.0.0.1", 6379);
        this.jedis.publish(Plugin.JedisChannel, Config.getServerName() + " 0");
        Thread jedisThread = new Thread(new RedisThread(subJedis, this));
        jedisThread.start();
    }

    public void resetTeamSelection() {
        this.choose_team = new TeamSelectionInventory(new TeamSelection(this), 27, "Выбор команды", this);
    }

    private void loadEvents(){

        onAsyncPlayerChat onAsyncPlayerChat = new onAsyncPlayerChat(this);
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, onAsyncPlayerChat, EventPriority.NORMAL, onAsyncPlayerChat, this);

        onBlockBreak onBlockBreak = new onBlockBreak(this);
        Bukkit.getPluginManager().registerEvent(BlockBreakEvent.class, onBlockBreak, EventPriority.NORMAL, onBlockBreak, this);

        onBlockPlace onBlockPlace = new onBlockPlace(this);
        Bukkit.getPluginManager().registerEvent(BlockPlaceEvent.class, onBlockPlace, EventPriority.NORMAL, onBlockPlace, this);

        onEntityDamage onEntityDamage = new onEntityDamage(this);
        Bukkit.getPluginManager().registerEvent(EntityDamageEvent.class, onEntityDamage, EventPriority.NORMAL, onEntityDamage, this);

        onEntityDamageByEntity onEntityDamageByEntity = new onEntityDamageByEntity(this);
        Bukkit.getPluginManager().registerEvent(EntityDamageByEntityEvent.class, onEntityDamageByEntity, EventPriority.NORMAL, onEntityDamageByEntity, this);

        onEntityDeath onEntityDeath = new onEntityDeath(this);
        Bukkit.getPluginManager().registerEvent(EntityDeathEvent.class, onEntityDeath, EventPriority.NORMAL, onEntityDeath, this);

        onEntityExplode onEntityExplode = new onEntityExplode(this);
        Bukkit.getPluginManager().registerEvent(EntityExplodeEvent.class, onEntityExplode, EventPriority.NORMAL, onEntityExplode, this);

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
    }
}

//     MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
//        GameProfile skin = new GameProfile(UUID.fromString("55854a2b-2a2c-409a-a5d2-7baa390ad6e9"), "§e§lУЛУЧШЕНИЯ");
//        skin.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNTA2MDEyMzQ5NSwKICAicHJvZmlsZUlkIiA6ICJkODAwZDI4MDlmNTE0ZjkxODk4YTU4MWYzODE0Yzc5OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVCTFJ4eCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80NjIzOTI2OTEyMGVhNmQxN2NmMDRkMjY3MWIxYjQ5MTgzMDg1NGRiOGIxMTFjYjdmMmE3ZDNmNDY2Yzk3MmEiCiAgICB9CiAgfQp9", "oRp5uGdhxK8L1JlDY8aF90TqZjNbqPh+vCUX+4Pw3wEYXhqJsxk7YfQAK0Vr7JqZvZ8FJru/DF6/Dbr9IeeIp5fniQlNeOuscuyJPdEP7y5dPUkhzBLsTe4Y+b+RMVrmOOCl6qY1rJM1Sv6c7qpZgwKjCZS9UA+2ZYS9mXQuzjwo+nKL5n+Q8a+yZcqNka4ZH9BghleqhF1HCnCMNlA78Xu7ov5brUc2Hufb5UXTrzqGp3myqTyVBjJ9hJvj54WWKswsxTbFtvPWERGbv3mZSdTjOlG/Js2nsynqIpehZqnU9C3aEW6bpH2i0uk6KRcpnY8rPWfdYRJgjGRh7w2M+FXPnBtVlOOCIedxsdI7U4YJ+NX8Fq+EjGNuzzzMyMaTRB1xBGnDl2gMbYEUZo27p+dv+pdFKGH1UADow8TzXUU0YLXtTkJEdZD2ZIuriaqQrBbSJvaUzhqhJSjq9HNRvIQfFngxLZlbHKMKZtOGGYN3e2sAyqyfQGXjOWqZJLVG5aKZJaVFwmRjEr9xG1Hjm3qOmSlUsnrCD0wK7e7uqqxPoCLr7KBOfBFL7nEGDSFCTovaFFcP8b90ZXvfn/JU6X+Yq8mswWbLy1e5CtLKVBGQRKdH/+/6rwhKf8blv4XlvyCQji5ge8m0SaT1WiK37CZ2/lm9Ofb3Oid1I8QKRYU="));
//
//        EntityPlayer npc = new EntityPlayer(server, nmsWorld, skin, new PlayerInteractManager(nmsWorld));
//        npc.getBukkitEntity().setDisplayName("Апгрейд Шоп");
//        npc.getBukkitEntity().setRemoveWhenFarAway(false);
//        npc.getBukkitEntity().setPlayerListName("Апгрейд Шоп");
//        Player npcPlayer = npc.getBukkitEntity().getPlayer();
//
//        npcPlayer.setDisplayName("Апргейд Шоп");
//
//        npc.setLocation(Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]), Double.parseDouble(cord.split(" ")[2]) + 0.5, Float.parseFloat(cord.split(" ")[3]), 0);
//
//        for(Player player : Bukkit.getOnlinePlayers()){
//            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (Float.parseFloat(cord.split(" ")[3])/ 360 * 256)));
//            //((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
//        }