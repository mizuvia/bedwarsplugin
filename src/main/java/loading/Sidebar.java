package loading;

import game.Participant;
import game.Team;
import game.Time;
import main.Config;
import main.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Sidebar{

//    public static final String SIDEBAR_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "BED" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "WARS";
//    private final Time timeClass;
//    private final Plugin plugin;
//    public HashMap<String, String> stringsList = new HashMap<>();
//    public List<String> keyList = new ArrayList<>();
//
//    public Sidebar(Plugin plugin){
//        this.plugin = plugin;
//        this.timeClass = this.getPlugin().getGame().getTime();
//
//        fillWaitingList();
//
//    }
//
//    private void putInList(String key, String value){
//        stringsList.put(key, value);
//        keyList.add(key);
//    }
//
//    public void fillWaitingList(){
//        stringsList.clear();
//        keyList.clear();
//
//        putInList("SERVER_NAME", "   " + ChatColor.DARK_GRAY + Config.getServerName());
//        putInList("GAP1", " ");
//        putInList("PLAYERS", ChatColor.YELLOW + "" + ChatColor.BOLD + "Игроков:");
//        putInList("PLAYERS_AMOUNT", ChatColor.AQUA + "" + this.getPlugin().getOnlinePlayers() + "/" + Config.getMaxPlayers());
//        putInList("GAP2", "  ");
//        putInList("TIME", ChatColor.YELLOW + "" + ChatColor.BOLD + "Времени:");
//        putInList("TIME_AMOUNT", ChatColor.AQUA + "Ожидаем...");
//        putInList("GAP3", "   ");
//        putInList("MAP", ChatColor.YELLOW + "" + ChatColor.BOLD + "Карта:");
//        putInList("MAP_NAME", ChatColor.AQUA + Config.getMapName());
//        putInList("GAP4", "    ");
//        putInList("PROJECT", ChatColor.GOLD + "" + ChatColor.BOLD + "   Mizuvia");
//
//        fillPlayersSidebars();
//    }
//
//    public void fillPlayingList(){
//        stringsList.clear();
//        keyList.clear();
//
//        putInList("SERVER_NAME", "   " + ChatColor.DARK_GRAY + Config.getServerName());
//        putInList("STAGE", ChatColor.AQUA + "" + ChatColor.BOLD + "Алмазы II: " + ChatColor.WHITE + Utils.getTime(getTime().getStage().getTime()));
//        putInList("GAP1", "  ");
//        for(String team : Config.getTeamsNames())
//            putInList("TEAM_" + team.toUpperCase(Locale.ROOT), "§a✔ §7§l| §r" + this.getPlugin().getTeams().get(team).getName().replace("§l", ""));
//        putInList("GAP2", "   ");
//        putInList("KILLS", ChatColor.WHITE + "Убийств: " + ChatColor.RED + "0");
//        putInList("BROKEN_BEDS", ChatColor.WHITE + "Разрушено кроватей: " + ChatColor.RED + "0");
//        putInList("FINAL_KILLS", ChatColor.WHITE + "Финальных убийств: " + ChatColor.RED + "0");
//
//        fillPlayersSidebars();
//
//        for(Participant p : getPlugin().getPlayers().values()){
//            String key = "TEAM_" + p.getTeam().getColor().toUpperCase(Locale.ROOT);
//            updatePlayerSidebar(p, key, getPlayerString(p, key) + ChatColor.GRAY + " ВЫ");
//        }
//
//    }
//
//    public void changePlayersAmount(){
//        updateSidebar("PLAYERS_AMOUNT", ChatColor.AQUA.toString() + (this.getPlugin().getOnlinePlayers()) + "/" + Config.getMaxPlayers());
//    }
//
////    public void changeTime(int time){
////        if(time == -1)
////            updateSidebar("TIME_AMOUNT", "§bОжидаем...");
////        else
////            updateSidebar("TIME_AMOUNT", "§b" + time + " сек.");
////    }
//
//    public void updateStage(Time.Stage stage){
//        updateSidebar("STAGE", stage.getName() + ChatColor.RESET +  ": " + Utils.getTime(stage.getTime()));
//    }
//
//    public void changeKilled(Participant p){
//        updatePlayerSidebar(p, "KILLS", "§7Убито игроков: §c" + p.getKilledPlayers());
//    }
//
//    private void updateSidebar(String key, String value){
//        stringsList.replace(key, value);
//        updatePlayersSidebar(key, value);
//    }
//
//    private void updatePlayersSidebar(String key, String value){
//        for(Participant p : this.getPlugin().getPlayers().values())
//            updatePlayerSidebar(p, key, value);
//    }
//
//    private void updatePlayerSidebar(Participant p, String key, String value){
//        String oldValue = p.getSidebarStrings().get(key);
//        for(Score score : p.getScoreboard().getScores(oldValue)){
//            p.getScoreboard().resetScores(oldValue);
//            p.getObjective().getScore(value).setScore(getScoreIndex(key));
//            break;
//        }
//        p.getSidebarStrings().replace(key, value);
//    }
//
//    public void drawPlayerSidebar(Participant p){
//        for(String entry : p.getScoreboard().getEntries())
//            p.getScoreboard().resetScores(entry);
//
//        HashMap<String, String> list = p.getSidebarStrings();
//
//        for(String key : keyList){
//            p.getObjective().getScore(list.get(key)).setScore(getScoreIndex(key));
//        }
//    }
//
//    public void fillPlayersSidebars(){
//        for(Participant p : this.getPlugin().getPlayers().values()){
//            fillPlayerSidebar(p);
//        }
//    }
//
//    public void fillPlayerSidebar(Participant p){
//        p.getSidebarStrings().clear();
//        for(String key : keyList){
//            p.getSidebarStrings().put(key, stringsList.get(key));
//        }
//        drawPlayerSidebar(p);
//    }
//
//    private int getScoreIndex(String key){
//        return keyList.size() - (keyList.indexOf(key) + 1);
//    }
//
//    private String getPlayerString(Participant p, String key){
//        return p.getSidebarStrings().get(key);
//    }
//
//    public void changeBrokenBeds(Participant p){
//        updatePlayerSidebar(p, "BROKEN_BEDS", ChatColor.WHITE + "Разрушено кроватей: " + ChatColor.RED + p.getBrokenBeds());
//    }
//
//    public Time getTime(){return this.timeClass;}
//
//    public Plugin getPlugin() {return this.plugin; }
//
//    public void setDead(Team team){
//        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
//        String message = ChatColor.RED + "" + ChatColor.BOLD + "×" + ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + team.getName().replace("§l", "");
//        updateSidebar(key, message);
//        for(Participant p : team.getTeammates().values()){
//            updatePlayerSidebar(p, key, message + " " + ChatColor.RESET + ChatColor.GRAY + "ВЫ");
//        }
//    }
//
//    public void decreaseTeammatesAmount(Team team) {
//        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
//        String message = ChatColor.YELLOW + "" + ChatColor.BOLD + team.getTeammatesAmount() + ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + team.getName().replace(ChatColor.BOLD.toString(), "");
//        updateSidebar(key, message);
//        for(Participant p : team.getTeammates().values()){
//            updatePlayerSidebar(p, key, message + " " + ChatColor.RESET + ChatColor.GRAY + "ВЫ");
//        }
//    }
//
//    public void changeFinalKills(Participant p) {
//        updatePlayerSidebar(p, "FINAL_KILLS", ChatColor.WHITE + "Финальных убийств: " + ChatColor.RED + p.getFinalKills());
//
//    }
}
