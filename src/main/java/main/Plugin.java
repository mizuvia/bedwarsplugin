package main;

import events.*;
import game.Game;
import game.Participant;
import game.Team;
import inventories.*;
import loading.Sidebar;
import loading.Waiting;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
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
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;
import tab.Tab;
import util.PlayerInv;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Plugin extends JavaPlugin {

    private FileConfiguration config;
    private File configFile;
    private Tab tab;
    private Scoreboard scoreboard;
    private Sidebar sidebar;
    private Game game;
    public int players_amount;
    public int teams_amount;
    public int players_per_team;
    public Waiting waiting;
    private boolean isLoading = false;
    private boolean isWorking = false;
    public int online_players;
    public String server_name;
    public String map_name;
    public List<String> teams_names;
    public TeamSelectionInventory choose_team;
    public HashMap<String, Team> teams = new HashMap<>();
    public HashMap<String, Participant> players = new HashMap<>();
    private List<String> diamonds;
    private List<String> emeralds;
    private Location center;
    private Jedis jedis;

    public Location getCenter(){return this.center; }

    public boolean isLoading() {return this.isLoading;}

    public boolean isWorking() {return this.isWorking; }

    public void setLoading(boolean isLoading) {this.isLoading = isLoading;}

    public void setWorking(boolean isWorking) {this.isWorking = isWorking;}

    public Sidebar getSidebar() { return this.sidebar; }

    public Scoreboard getScoreboard() { return this.scoreboard; }

    public List<String> getTeamsNames() { return this.teams_names; }

    public int getTeamsAmount(){ return this.teams_amount; }

    public HashMap<String, Team> getTeams() {return this.teams; }

    public List<String> getEmeralds() {return this.emeralds; }

    public void setEmeralds(List<String> emeralds) { this.emeralds = emeralds; }

    public List<String> getDiamonds(){ return this.diamonds; }

    public String getMapName(){ return this.map_name; }

    public HashMap<String, Participant> getPlayers(){ return this.players; }

    public Game getGame(){ return this.game; }

    @NotNull
    public FileConfiguration getConfig(){ return this.config; }

    public File getConfigFile(){return this.configFile; }

    public Tab getTab(){
        return this.tab;
    }

    public TeamSelectionInventory getTeamSelectionInventory() {return this.choose_team; }

    public Waiting getWaiting(){ return this.waiting; }

    private void setDiamonds(List<String> diamonds) {
        this.diamonds = diamonds;
    }

    public void decreaseOnlinePlayers() { this.online_players--; }

    public void increaseOnlinePlayers() { this.online_players++; }

    public int getOnlinePlayers(){ return this.online_players; }

    public int getMaxPlayers() {return this.players_amount; }

    public int getPlayersPerTeam() {return this.players_per_team; }

    public Jedis getJedis(){return this.jedis;}

    @Override
    public void onEnable(){
        this.loadConfig();
        this.jedis = new Jedis("127.0.0.1", 6379);
        this.jedis.publish("bw", this.getConfig().getString("server_name") + " 0");

        this.teams_names = this.getConfig().getStringList("team_list");

        File worlds = new File("./", "worlds");
        for(int i = 0; i < worlds.list().length; i++){
            File w = new File("./worlds/", worlds.list()[i]);
            if(w.isFile()) continue;
            WorldCreator world = new WorldCreator(worlds.list()[i]);
            world.createWorld();
        }

        for(String color : this.getTeamsNames()){
            Team team = new Team(this, color);

            String name = this.getConfig().getString("teams." + color + ".display_name");
            String cord = this.getConfig().getString("teams." + color + ".res_location");
            String spawnCord = this.getConfig().getString("teams." + color + ".spawn_location");
            String cords_bottom = this.getConfig().getString("teams." + color + ".bed_bottom");
            String cords_top = this.getConfig().getString("teams." + color + ".bed_top");

            team.setName(name);
            team.setResourceLocation(Utils.getLocation(cord));
            team.setSpawnLocation(Utils.getLocation(spawnCord));
            team.setBedBottom(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getBlockData().clone());
            team.setBedTop(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getBlockData().clone());

            this.getTeams().put(color, team);
        }

        this.setDiamonds(this.getConfig().getStringList("diamonds"));
        this.setEmeralds(this.getConfig().getStringList("emeralds"));

        this.teams_amount = this.getConfig().getInt("teams_amount");
        this.players_per_team = this.getConfig().getInt("players_amount");
        this.players_amount = this.getPlayersPerTeam() * this.getTeamsAmount();
        this.server_name = this.getConfig().getString("server_name");
        this.map_name = this.getConfig().getString("map_name");

        this.center = Utils.getLocation(this.getConfig().getString("world_center"));

        this.game = new Game(this);

        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.choose_team = new TeamSelectionInventory(new TeamSelection(this), 27, "Выбор команды", this);

        this.sidebar = new Sidebar(this);
        this.online_players = this.getServer().getOnlinePlayers().size();

        this.waiting = new Waiting(this);

        this.tab = new Tab(this);

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "bungeecord:main", new PluginMessageHandler(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "bungeecord:main");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessageHandler(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

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

        for(Player player : this.getServer().getOnlinePlayers()){
            Participant p = new Participant(player, this);

            player.setDisplayName(PlayerManager.getGroupDisplayName(p) + player.getName());
            player.setPlayerListName(player.getDisplayName());

            this.getPlayers().put(player.getName(), p);
            PlayerInv.setWaitingInventory(p);

            player.setScoreboard(this.getScoreboard());
            this.getTab().addPlayer(p);
        }


        getLogger().info("enabled!");
    }


    public void onDisable(){
        getLogger().info("disabled!");

        this.getGame().stop();
    }

    private void loadConfig(){
        this.configFile = new File("./worlds/world", "config.yml");
        if(!this.configFile.exists()){
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = new YamlConfiguration();
        try{
            this.config.load(this.configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void resetTeamSelection() {
        this.choose_team = new TeamSelectionInventory(new TeamSelection(this), 27, "Выбор команды", this);
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