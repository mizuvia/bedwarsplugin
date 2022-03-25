package loading;

import game.Participant;
import game.Time;
import main.Config;
import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import util.MineColor;
import util.Utils;

import java.util.*;

public class Sidebar {
    public static final String SIDEBAR_NAME = MineColor.ORANGE.BOLD() + "BED" + MineColor.PURPLE.BOLD() + "WARS";
    private final Time timeClass;
    private final Plugin plugin;
    public Map<String, String> stringsList = new LinkedHashMap<>();
    public Map<String, String> spectatorStringsList = new LinkedHashMap<>();
    public Scoreboard spectatorScoreboard;
    public Objective spectatorObjective;
    public LinkedList<Team> spectatorTeams = new LinkedList<>();

    public Sidebar(Plugin plugin){
        this.plugin = plugin;
        this.timeClass = this.getPlugin().getGame().getTime();
        createSpectatorSidebar();

        fillWaitingList();
    }

    private void createSpectatorSidebar() {
        spectatorScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        spectatorObjective = spectatorScoreboard.registerNewObjective("sidebar", "dummy", SIDEBAR_NAME);
        spectatorObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    private void putInList(String key, String value, boolean forSpectator){
        stringsList.put(key, value);
        if (forSpectator) spectatorStringsList.put(key ,value);
    }

    public void fillWaitingList() {
        clearSidebar();

        putInList("SERVER_NAME", MineColor.GRAY + Config.getServerName(), false);
        putInList("GAP1", " ", false);
        putInList("PLAYERS", MineColor.YELLOW.BOLD() + "Игроков:", false);
        putInList("PLAYERS_AMOUNT", MineColor.AQUA + "" + this.getPlugin().getOnlinePlayers() + "/" + Config.getMaxPlayers(), false);
        putInList("GAP2", "  ", false);
        putInList("TIME", MineColor.YELLOW.BOLD() + "Времени:", false);
        putInList("TIME_AMOUNT", MineColor.AQUA + "Ожидаем...", false);
        putInList("GAP3", "   ", false);
        putInList("MAP", MineColor.YELLOW.BOLD() + "Карта:", false);
        putInList("MAP_NAME", MineColor.AQUA + Config.getMapName(), false);
        putInList("GAP4", "    ", false);
        putInList("PROJECT", MineColor.ORANGE.BOLD()  + "    Mizuvia", false);

        fillPlayersSidebars();
    }

    public void fillPlayingList(){
        clearSidebar();

        putInList("SERVER_NAME", MineColor.GRAY + Config.getServerName() + "          " + Utils.getTime(0), true);
        putInList("STAGE", MineColor.AQUA.BOLD() + "Алмазы II: " + MineColor.WHITE + Utils.getTime(getTime().getStage().getTime()), true);
        putInList("GAP1", "  ", true);
        for(String team : Config.getTeamsNames())
            putInList("TEAM_" + team.toUpperCase(Locale.ROOT), MineColor.LIME + "✔" + MineColor.LIGHT_GRAY.BOLD() + " | " + MineColor.RESET + this.getPlugin().getTeams().get(team).getName().replace("§l", ""), true);
        putInList("GAP2", "   ", true);
        putInList("KILLS", MineColor.WHITE + "Убийств: " + MineColor.RED + "0", false);
        putInList("BROKEN_BEDS", MineColor.WHITE + "Разрушено кроватей: " + MineColor.RED + "0", false);
        putInList("FINAL_KILLS", MineColor.WHITE + "Финальных убийств: " + MineColor.RED + "0", false);

        fillPlayersSidebars();

        for(Participant p : getPlugin().getPlayers().values()){
            String key = "TEAM_" + p.getTeam().getColor().toUpperCase(Locale.ROOT);
            updatePlayerSidebar(p, key, p.getScoreboard().getTeam(key).getPrefix() + MineColor.LIGHT_GRAY + " ВЫ");
        }
    }

    public Scoreboard getSpectatorScoreboard() {
        return spectatorScoreboard;
    }

    private void clearSidebar() {
        stringsList.clear();
        spectatorStringsList.clear();
        spectatorTeams.clear();
        createSpectatorSidebar();

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
        updateSidebar("PLAYERS_AMOUNT", MineColor.AQUA.toString() + (this.getPlugin().getOnlinePlayers()) + "/" + Config.getMaxPlayers());
    }

    public void changeTime(int time){
        if(time == -1)
            updateSidebar("TIME_AMOUNT", MineColor.AQUA + "Ожидаем...");
        else
            updateSidebar("TIME_AMOUNT", MineColor.AQUA.toString() + time + " сек.");
    }

    public void updateStage(Time.Stage stage){
        updateSidebar("STAGE", stage.getName() + MineColor.RESET +  ": " + Utils.getTime(stage.getTime()));
    }

    public void updateStage(String stage){
        updateSidebar("STAGE", stage);
    }

    public void changeKilled(Participant p){
        updatePlayerSidebar(p, "KILLS", "Убито игроков: " + MineColor.RED + p.getKilledPlayers());
    }

    private void updateSidebar(String key, String value){
        stringsList.replace(key, value);
        updatePlayersSidebar(key, value);
        if (spectatorStringsList.containsKey(key)) spectatorScoreboard.getTeam(key).setPrefix(value);
    }

    private void updatePlayersSidebar(String key, String value){
        for(Participant p : this.getPlugin().getPlayers().values())
            updatePlayerSidebar(p, key, value);
    }

    private void updatePlayerSidebar(Participant p, String key, String value){
        p.getScoreboard().getTeam(key).setPrefix(value);
    }

    public void drawPlayerSidebar(Participant p){
        for (Team team : p.getSidebarTeams()){
            int index = p.getSidebarTeams().indexOf(team);
            team.addEntry(MineColor.values()[index] + "" + MineColor.WHITE);
            p.getSidebarObjective().getScore(MineColor.values()[index] + "" + MineColor.WHITE).setScore(p.getSidebarTeams().size() - index);
        }
    }

    public void fillPlayersSidebars(){
        for(Participant p : this.getPlugin().getPlayers().values()){
            fillPlayerSidebars(p);
        }
        spectatorStringsList.forEach((key, value) -> {
            Team team = spectatorScoreboard.registerNewTeam(key);
            team.setPrefix(value);
            spectatorTeams.add(team);
        });
        spectatorTeams.forEach(team -> {
            int i = spectatorTeams.indexOf(team);
            team.addEntry(MineColor.values()[i] + "" + MineColor.WHITE);
            spectatorObjective.getScore(MineColor.values()[i] + "" + MineColor.WHITE).setScore(spectatorTeams.size() - i);
        });
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
        updatePlayerSidebar(p, "BROKEN_BEDS", MineColor.WHITE + "Разрушено кроватей: " + MineColor.RED + p.getBrokenBeds());
    }

    public Time getTime(){return this.timeClass;}

    public Plugin getPlugin() {return this.plugin; }

    public void setDead(game.Team team){
        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
        String message = MineColor.RED.BOLD() + "×" + MineColor.RESET + MineColor.LIGHT_GRAY.BOLD() + " | " + MineColor.RESET + team.getName().replace("§l", "");
        updateSidebar(key, message);
        for(Participant p : team.getTeammates().values()){
            updatePlayerSidebar(p, key, message + " " + MineColor.LIGHT_GRAY + "ВЫ");
        }
    }

    public void decreaseAliveTeammates(game.Team team) {
        String key = "TEAM_" + team.getColor().toUpperCase(Locale.ROOT);
        String message = MineColor.YELLOW.BOLD() + "" + team.getAliveTeammates() + MineColor.LIGHT_GRAY.BOLD() + " | " + MineColor.RESET + team.getName().replace(ChatColor.BOLD.toString(), "");
        updateSidebar(key, message);
        for(Participant p : team.getTeammates().values()){
            updatePlayerSidebar(p, key, message + " " + MineColor.LIGHT_GRAY + "ВЫ");
        }
    }

    public void changeFinalKills(Participant p) {
        updatePlayerSidebar(p, "FINAL_KILLS", MineColor.WHITE + "Финальных убийств: " + MineColor.RED + p.getFinalKills());
    }

    public void updateMatchTime() {
        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+2"));
        String dateStr = c.get(Calendar.DAY_OF_MONTH) + "." + (c.get(Calendar.MONTH) + 1) + "." + c.get(Calendar.YEAR);
        updateSidebar("SERVER_NAME", MineColor.GRAY + Config.getServerName().toUpperCase(Locale.ROOT) + " | " + dateStr + " | " + Utils.getTime(plugin.getGame().getMatchTime()));
    }
}
