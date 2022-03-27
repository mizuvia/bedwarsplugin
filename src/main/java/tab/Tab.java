/* свят гей */

package tab;

import game.Participant;
import main.PlayerManager;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import util.MineColor;

public class Tab {

    private final Plugin plugin;
    private static final String ANOTHER_TEAM_NAME = "zwithout";

    public Tab(Plugin plugin){ this.plugin = plugin; }

    public Plugin getPlugin(){ return this.plugin; }

    public void createTab(Scoreboard scoreboard) {
        for(game.Team team : this.getPlugin().getTeams().values()){
            Team t = scoreboard.registerNewTeam(team.getColor());
            if (plugin.isWorking()) t.setPrefix(PlayerManager.getCodeColor(team.getColor()));
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
            Player pl = p.getPlayer();
            game.Team team = p.getTeam();
            if (team != null) {
                sb.getTeam(team.getColor()).addEntry(pl.getName());
                String name = PlayerManager.getCodeColor(p) + team.getName().charAt(4) + " | " + PlayerManager.getCodeColor(p) + pl.getName();
                if (plugin.isWorking()) p.getPlayer().setPlayerListName(name);
            }
            else sb.getTeam(ANOTHER_TEAM_NAME).addEntry(p.getPlayer().getName());
        }

        tabOwner.getPlayer().setScoreboard(sb);
    }

    private void updateTeams(Participant tabOwner) {
        Scoreboard sb = tabOwner.getScoreboard();
        for (game.Team team : plugin.getTeams().values()) {
            sb.getTeam(team.getColor()).unregister();
            Team t = sb.registerNewTeam(team.getColor());
            t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OWN_TEAM);
            if (plugin.isWorking()) t.setPrefix(PlayerManager.getCodeColor(team.getColor()) + team.getName().charAt(4) + MineColor.LIGHT_GRAY + " | ");
            else t.setPrefix(MineColor.GRAY.BOLD() + "[" + MineColor.RESET + team.getName() + MineColor.GRAY.BOLD() + "] ");
        }
        sb.getTeam(ANOTHER_TEAM_NAME).unregister();
        sb.registerNewTeam(ANOTHER_TEAM_NAME);
    }
}