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
        for(Team team : plugin.getTeams().values()){
            if(team.getTeammatesAmount() == Config.getPlayersPerTeam()) continue;
            if(checkPlayerInTeam(party, team.getTeammates())){
                plugin.getPlayers().get(UUID.fromString(player.uuid)).setTeam(team);
                return;
            }
        }
        addToEmptyTeam(plugin, party, player);
    }

    private static void addToEmptyTeam(Plugin plugin, Party party, Player player){
        for(Team team : plugin.getTeams().values()){
            if(team.getTeammatesAmount() != 0) continue;
            plugin.getPlayers().get(UUID.fromString(player.uuid)).setTeam(team);
            return;
        }
        addToRandomTeam(plugin, player);
    }

    private static void addToRandomTeam(Plugin plugin, Player player){
        String teamColor = Config.getTeamsNames().get(0);
        for(Team team : plugin.getTeams().values())
            if(plugin.getTeams().get(teamColor).getTeammatesAmount() > team.getTeammatesAmount())
                teamColor = team.getColor();
        plugin.getPlayers().get(UUID.fromString(player.uuid)).setTeam(plugin.getTeams().get(teamColor));
    }

    private static boolean checkPlayerInTeam(Party party, Collection<Participant> participants){
        for(Participant p : participants)
            if(party.members.contains(p.getPlayer().getUniqueId().toString()))
                return true;
        return false;
    }
}
