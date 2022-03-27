/* свят гей */

package tab;

import game.Participant;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import tasks.TaskGUI;
import util.MineColor;

public class Tab /*extends TaskGUI*/ {

    private final Plugin plugin;
    private static final String ANOTHER_TEAM_NAME = "zwithout";

    public Tab(Plugin plugin){ this.plugin = plugin; }

//    @Override
//    protected void execute() {
//
//    }

    public Plugin getPlugin(){ return this.plugin; }

    public void createTab(Scoreboard scoreboard) {
        for(game.Team team : this.getPlugin().getTeams().values()){
            Team t = scoreboard.registerNewTeam(team.getColor());
            t.setPrefix(team.getName());
        }

        scoreboard.registerNewTeam(ANOTHER_TEAM_NAME);
    }

    public void updateTabs(){
        for(Participant tabOwner : plugin.getPlayers().values()){
            updateTab(tabOwner);
        }
    }

    private void updateTab(Participant tabOwner){
        updateTeams(tabOwner);

        Scoreboard sb = tabOwner.getScoreboard();

        for (Participant p : plugin.getPlayers().values()) {
            game.Team team = p.getTeam();
            if (team != null) sb.getTeam(team.getColor()).addEntry(p.getPlayer().getName());
            else sb.getTeam(ANOTHER_TEAM_NAME).addEntry(p.getPlayer().getName());
        }

        tabOwner.getPlayer().setScoreboard(sb);
    }

    private void updateTeams(Participant tabOwner) {
        Scoreboard sb = tabOwner.getScoreboard();
        for (game.Team team : plugin.getTeams().values()) {
            sb.getTeam(team.getColor()).unregister();
            Team t = sb.registerNewTeam(team.getColor());
            t.setPrefix(MineColor.GRAY.BOLD() + "[" + MineColor.RESET + team.getName() + MineColor.GRAY.BOLD() + "]");
        }
        sb.getTeam(ANOTHER_TEAM_NAME).unregister();
        sb.registerNewTeam(ANOTHER_TEAM_NAME);
    }

//    public void updateTab(Player p) {
//
//    }
}