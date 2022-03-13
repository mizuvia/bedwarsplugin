/* свят гей */

package tab;

import game.Participant;
import main.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Tab {

    private final Plugin plugin;
    private static final String ANOTHER_TEAM_NAME = "zwithout";

    public Tab(Plugin plugin){ this.plugin = plugin; }

    public Plugin getPlugin(){ return this.plugin; }

    public void createTab(Scoreboard scoreboard) {
        Objective objective = scoreboard.registerNewObjective("tab", "dummy", "§6§lMizuvia");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        for(game.Team team : this.getPlugin().getTeams().values()){
            scoreboard.registerNewTeam(team.getColor());
        }

        scoreboard.registerNewTeam(ANOTHER_TEAM_NAME);
    }

    public void addPlayerToTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            addPlayerToTab(tabOwner, par);
        }
    }

    private void addPlayerToTab(Participant tabOwner, Participant par){
        if(!par.hasTeam()) tabOwner.getScoreboard().getTeam(ANOTHER_TEAM_NAME).addEntry(par.getPlayer().getName());
        else tabOwner.getScoreboard().getTeam(par.getTeam().getColor()).addEntry(par.getPlayer().getName());
    }

    public void removePlayerFromTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            removePlayerFromTab(tabOwner, par);
        }
    }

    private void removePlayerFromTab(Participant tabOwner, Participant par){
        if(!par.hasTeam()) tabOwner.getScoreboard().getTeam(ANOTHER_TEAM_NAME).removeEntry(par.getPlayer().getName());
        else tabOwner.getScoreboard().getTeam(par.getTeam().getColor()).removeEntry(par.getPlayer().getName());
    }
}