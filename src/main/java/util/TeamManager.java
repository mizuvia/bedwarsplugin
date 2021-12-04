package util;

import game.Participant;
import game.Team;
import inventories.TeamSelection;
import main.Plugin;
import org.bukkit.ChatColor;

public class TeamManager{

    public static void addPlayerToTeam(Plugin plugin, Team team, Participant participant){
        plugin.getTab().addPlayerToTabs(participant);

        TeamSelection.addPlayerToItem(plugin, team, participant.getPlayer());

        participant.setTeam(team);

        participant.getPlayer().setDisplayName("§8§l[" + team.getName() + "§8§l]§r§7 " + participant.getPlayer().getName());
        participant.getPlayer().setPlayerListName("§8§l[" + team.getName() + "§8§l]§r§7 " + participant.getPlayer().getName());

        team.addTeammate(participant);
        participant.getPlayer().closeInventory();
    }

    public static void removePlayerFromTeam(Plugin plugin, Participant participant){
        plugin.getTab().removePlayerFromTabs(participant);

        TeamSelection.removePlayerFromItem(plugin, participant);

        participant.getTeam().removeTeammate(participant);
    }

}