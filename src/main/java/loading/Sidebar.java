package loading;

import game.Participant;
import game.Time;
import main.Config;
import main.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;
import util.Utils;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Sidebar {
    public static final String SIDEBAR_NAME = ChatColor.GOLD + "" + ChatColor.BOLD + "BED" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "WARS";
    private final Time timeClass;
    private final Plugin plugin;
    public Map<String, String> stringsList = new LinkedHashMap<>();

    public Sidebar(Plugin plugin){
        this.plugin = plugin;
        this.timeClass = this.getPlugin().getGame().getTime();

        fillWaitingList();
    }

    private void putInList(String key, String value){
        stringsList.put(key, value);
    }

    public void fillWaitingList() {
        clearSidebar();

        putInList("SERVER_NAME", "   " + ChatColor.DARK_GRAY + Config.getServerName());
        putInList("GAP1", " ");
        putInList("PLAYERS", ChatColor.YELLOW + "" + ChatColor.BOLD + "Игроков:");
        putInList("PLAYERS_AMOUNT", ChatColor.AQUA + "" + this.getPlugin().getOnlinePlayers() + "/" + Config.getMaxPlayers());
        putInList("GAP2", "  ");
        putInList("TIME", ChatColor.YELLOW + "" + ChatColor.BOLD + "Времени:");
        putInList("TIME_AMOUNT", ChatColor.AQUA + "Ожидаем...");
        putInList("GAP3", "   ");
        putInList("MAP", ChatColor.YELLOW + "" + ChatColor.BOLD + "Карта:");
        putInList("MAP_NAME", ChatColor.AQUA + Config.getMapName());
        putInList("GAP4", "    ");
        putInList("PROJECT", ChatColor.GOLD + "" + ChatColor.BOLD + "   Mizuvia");

        fillPlayersSidebars();
    }

    public void fillPlayingList(){
        clearSidebar();

        putInList("SERVER_NAME", "   " + ChatColor.DARK_GRAY + Config.getServerName());
        putInList("STAGE", ChatColor.AQUA + "" + ChatColor.BOLD + "Алмазы II: " + ChatColor.WHITE + Utils.getTime(getTime().getStage().getTime()));
        putInList("GAP1", "  ");
        for(String team : Config.getTeamsNames())
            putInList("TEAM_" + team.toUpperCase(Locale.ROOT), "§a✔ §7§l| §r" + this.getPlugin().getTeams().get(team).getName().replace("§l", ""));
        putInList("GAP2", "   ");
        putInList("KILLS", ChatColor.WHITE + "Убийств: " + ChatColor.RED + "0");
        putInList("BROKEN_BEDS", ChatColor.WHITE + "Разрушено кроватей: " + ChatColor.RED + "0");
        putInList("FINAL_KILLS", ChatColor.WHITE + "Финальных убийств: " + ChatColor.RED + "0");

        fillPlayersSidebars();

        for(Participant p : getPlugin().getPlayers().values()){
            String key = "TEAM_" + p.getTeam().getColor().toUpperCase(Locale.ROOT);
            updatePlayerSidebar(p, key, p.getScoreboard().getTeam(key).getPrefix() + ChatColor.GRAY + " ВЫ");
        }
    }

    private void clearSidebar() {
        stringsList.clear();

        for (Participant p : getPlugin().getPlayers().values()) {
            for (Team t : p.getSidebarTeams()) {
                String entry = t.getEntries().stream().findFirst().orElse(null);
                p.getScoreboard().resetScores(entry);
                t.unregister();
            }
            p.getSidebarTeams().clear();
        }
    }

    public void changePlayersAmount(){
        updateSidebar("PLAYERS_AMOUNT", ChatColor.AQUA.toString() + (this.getPlugin().getOnlinePlayers()) + "/" + Config.getMaxPlayers());
    }

    public void changeTime(int time){
        if(time == -1)
            updateSidebar("TIME_AMOUNT", "§bОжидаем...");
        else
            updateSidebar("TIME_AMOUNT", "§b" + time + " сек.");
    }

    public void updateStage(Time.Stage stage){
        updateSidebar("STAGE", stage.getName() + ChatColor.RESET +  ": " + Utils.getTime(stage.getTime()));
    }

    public void updateStage(String stage){
        updateSidebar("STAGE", stage);
    }

    public void changeKilled(Participant p){
        updatePlayerSidebar(p, "KILLS", "Убито игроков: §c" + p.getKilledPlayers());
    }

    private void updateSidebar(String key, String value){
        stringsList.replace(key, value);
        updatePlayersSidebar(key, value);
    }

    private void updatePlayersSidebar(String key, String value){
        for(Participant p : this.getPlugin().getPlayers().values())
            updatePlayerSidebar(p, key, value);
    }

    private void updatePlayerSidebar(Participant p, String key, String value){
        p.getScoreboard().getTeam(key).setPrefix(value);
    }

    public void drawPlayerSidebar(Participant p){
        for(Team team : p.getSidebarTeams()){
            int index = p.getSidebarTeams().indexOf(team);
            team.addEntry(ChatColor.values()[index] + "" + ChatColor.WHITE);
            p.getSidebarObjective().getScore(ChatColor.values()[index] + "" + ChatColor.WHITE).setScore(p.getSidebarTeams().size() - index);
        }
    }

    public void fillPlayersSidebars(){
        for(Participant p : this.getPlugin().getPlayers().values()){
            fillPlayerSidebars(p);
        }
    }

    public void fillPlayerSidebars(Participant p) {
        stringsList.forEach((key, value) -> {
            Team team = p.getScoreboard().registerNewTeam(key);
            p.getSidebarTeams().add(team);
            team.setPrefix(value);
        });
        drawPlayerSidebar(p);
    }

    public void changeBrokenBeds(Participant p){
        updatePlayerSidebar(p, "BROKEN_BEDS", ChatColor.WHITE + "Разрушено кроватей: " + ChatColor.RED + p.getBrokenBeds());
    }

    public Time getTime(){return this.timeClass;}

    public Plugin getPlugin() {return this.plugin; }

    public void setDead(game.Team team){
        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
        String message = ChatColor.RED + "" + ChatColor.BOLD + "×" + ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + team.getName().replace("§l", "");
        updateSidebar(key, message);
        for(Participant p : team.getTeammates().values()){
            updatePlayerSidebar(p, key, message + " " + ChatColor.RESET + ChatColor.GRAY + "ВЫ");
        }
    }

    public void decreaseAliveTeammates(game.Team team) {
        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
        String message = ChatColor.YELLOW + "" + ChatColor.BOLD + team.getAliveTeammates() + ChatColor.RESET + " " + ChatColor.GRAY + ChatColor.BOLD + "| " + ChatColor.RESET + team.getName().replace(ChatColor.BOLD.toString(), "");
        updateSidebar(key, message);
        for(Participant p : team.getTeammates().values()){
            updatePlayerSidebar(p, key, message + " " + ChatColor.RESET + ChatColor.GRAY + "ВЫ");
        }
    }

    public void changeFinalKills(Participant p) {
        updatePlayerSidebar(p, "FINAL_KILLS", ChatColor.WHITE + "Финальных убийств: " + ChatColor.RED + p.getFinalKills());
    }
}
