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
        Objective objective = scoreboard.registerNewObjective("tab", "dummy", "§6§lMizuvia");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        for(game.Team team : this.getPlugin().getTeams().values()){
            Team t = scoreboard.registerNewTeam(team.getColor());
            t.setPrefix(team.getName());
        }

        scoreboard.registerNewTeam(ANOTHER_TEAM_NAME);
    }

    public void addPlayerToTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            addPlayerToTab(tabOwner, par);
        }
    }

    private void addPlayerToTab(Participant tabOwner, Participant par){
        Scoreboard sb = tabOwner.getScoreboard();
        if(!par.hasTeam()) sb.getTeam(ANOTHER_TEAM_NAME).addEntry(par.getPlayer().getName());
        else sb.getTeam(par.getTeam().getColor()).addEntry(par.getPlayer().getName());
        tabOwner.getPlayer().setScoreboard(sb);
    }

    public void removePlayerFromTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            removePlayerFromTab(tabOwner, par);
        }
    }

    private void removePlayerFromTab(Participant tabOwner, Participant par){
        Scoreboard sb = tabOwner.getScoreboard();
        if(!par.hasTeam()) sb.getTeam(ANOTHER_TEAM_NAME).removeEntry(par.getPlayer().getName());
        else sb.getTeam(par.getTeam().getColor()).removeEntry(par.getPlayer().getName());
        tabOwner.getPlayer().setScoreboard(sb);
    }

//    public void updateTab(Player p) {
//
//    }
}