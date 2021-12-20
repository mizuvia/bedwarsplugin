package util;

import com.hoshion.mongoapi.docs.Party;
import com.hoshion.mongoapi.docs.Player;
import game.Participant;
import game.Team;
import main.Config;
import main.Plugin;

import java.util.Collection;

public class PartyManager{

    public static void addPlayer(Plugin plugin, Party party, Player player){
        for(Team team : plugin.getTeams().values()){
            if(team.getTeammatesAmount() == Config.getPlayersPerTeam()) continue;
            if(checkPlayerInTeam(party, team.getTeammates().values())){
                TeamManager.addPlayerToTeam(plugin, team, plugin.getPlayers().get(player.uuid));
                return;
            }
        }
        addToEmptyTeam(plugin, party, player);
    }

    private static void addToEmptyTeam(Plugin plugin, Party party, Player player){
        for(Team team : plugin.getTeams().values()){
            if(team.getTeammatesAmount() != 0) continue;
            TeamManager.addPlayerToTeam(plugin, team, plugin.getPlayers().get(player.uuid));
            return;
        }
        addToRandomTeam(plugin, party, player);
    }

    private static void addToRandomTeam(Plugin plugin, Party party, Player player){
        String teamColor = Config.getTeamsNames().get(0);
        for(Team team : plugin.getTeams().values())
            if(plugin.getTeams().get(teamColor).getTeammatesAmount() > team.getTeammatesAmount())
                teamColor = team.getColor();
        TeamManager.addPlayerToTeam(plugin, plugin.getTeams().get(teamColor), plugin.getPlayers().get(player.uuid));
    }

    private static boolean checkPlayerInTeam(Party party, Collection<Participant> participants){
        for(Participant p : participants)
            if(party.members.contains(p.getPlayer().getUniqueId().toString()))
                return true;
        return false;
    }
}
