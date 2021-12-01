package main;

import game.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private static Config instance;
    private static List<String> diamonds;
    private static List<String> emeralds;
    public static List<String> teams_names;
    public static String server_name;
    public static String map_name;
    public static int players_amount;
    public static int teams_amount;
    public static int players_per_team;
    private static Location center;

    public static void createInstance(Plugin plugin) {
        instance = new Config(plugin);
    }

    public static List<String> getEmeralds() {return emeralds; }

    private static void setDiamonds(List<String> diamondsList) {
        diamonds = diamondsList;
    }

    public static void setEmeralds(List<String> emeraldsList) { emeralds = emeraldsList; }

    public static List<String> getDiamonds(){ return diamonds; }

    public static List<String> getTeamsNames() { return teams_names; }

    public static String getMapName(){ return map_name; }

    public static int getMaxPlayers() {return players_amount; }

    public static int getPlayersPerTeam() {return players_per_team; }

    public static int getTeamsAmount(){ return teams_amount; }

    public static Location getCenter(){return center; }

    public static String getServerName(){return server_name;}

    public Config(Plugin plugin){
        File configFile = this.loadConfig();
        YamlConfiguration config = new YamlConfiguration();
        try{
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        this.loadTeams(plugin, config);
        setDiamonds(config.getStringList("diamonds"));
        setEmeralds(config.getStringList("emeralds"));

        teams_amount = config.getInt("teams_amount");
        players_per_team = config.getInt("players_amount");
        players_amount = getPlayersPerTeam() * getTeamsAmount();
        server_name = config.getString("server_name");
        map_name = config.getString("map_name");

        center = Utils.getLocation(config.getString("world_center"));
    }

    private File loadConfig(){
        File configFile = new File("./", "config.yml");
        if(!configFile.exists()){
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return configFile;
    }

    private void loadTeams(Plugin plugin, YamlConfiguration config) {
        teams_names = config.getStringList("team_list");
        for(String color : getTeamsNames()){
            Team team = new Team(plugin, color);

            String name = config.getString("teams." + color + ".display_name");
            String cord = config.getString("teams." + color + ".res_location");
            String spawnCord = config.getString("teams." + color + ".spawn_location");
            String cords_bottom = config.getString("teams." + color + ".bed_bottom");
            String cords_top = config.getString("teams." + color + ".bed_top");
            String shop_cord = config.getString("teams." + color + ".shop");
            String upgrades_cord = config.getString("teams." + color + ".upgrades_shop");

            team.setName(name);
            team.setResourceLocation(Utils.getLocation(cord));
            team.setSpawnLocation(Utils.getLocation(spawnCord));
            team.setBedBottom(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getBlockData().clone());
            team.setBedTop(Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getBlockData().clone());
            team.setShopVillager(Utils.getLocation(shop_cord));
            team.setUpgradesVillager(Utils.getLocation(upgrades_cord));

            plugin.getTeams().put(color, team);
        }
    }
}
