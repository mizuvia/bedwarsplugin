package util;

import com.hoshion.mongoapi.docs.Party;
import com.hoshion.mongoapi.docs.Player;
import game.Participant;
import game.Team;
import main.Config;
import main.Plugin;

import java.util.Collection;
import java.util.UUID;

public class PartyManager{

    public static void addPlayer(Plugin plugin, Party party, Player player){
        for(Team team : plugin.getTeams()){
            if(team.getTeammatesAmount() == Config.getPlayersPerTeam()) continue;
            if(checkPlayerInTeam(party, team.getTeammates())){
                plugin.getPlayer(UUID.fromString(player.uuid)).setTeam(team);
                return;
            }
        }
        addToEmptyTeam(plugin, party, player);
    }

    private static void addToEmptyTeam(Plugin plugin, Party party, Player player){
        for(Team team : plugin.getTeams()){
            if(team.getTeammatesAmount() != 0) continue;
            plugin.getPlayer(UUID.fromString(player.uuid)).setTeam(team);
            return;
        }
        addToRandomTeam(plugin, player);
    }

    private static void addToRandomTeam(Plugin plugin, Player player){
        String teamColor = Config.getTeamsNames().get(0);
        for(Team team : plugin.getTeams())
            if(plugin.getTeam(teamColor).getTeammatesAmount() > team.getTeammatesAmount())
                teamColor = team.getColor();
        plugin.getPlayer(UUID.fromString(player.uuid)).setTeam(plugin.getTeam(teamColor));
    }

    private static boolean checkPlayerInTeam(Party party, Collection<Participant> participants){
        for(Participant p : participants)
            if(party.members.contains(p.getPlayer().getUniqueId().toString()))
                return true;
        return false;
    }
}
