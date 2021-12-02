/* свят гей */

package tab;

import game.Participant;
import main.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class Tab {

    private final Plugin plugin;
    public final Scoreboard scoreboard;

    public Tab(Plugin plugin){
        this.plugin = plugin;
        this.scoreboard = this.plugin.getScoreboard();
        Objective objective = this.scoreboard.registerNewObjective("scoreboard", "dummy", "§6§lMizu§5§lCraft");

        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        for(game.Team team : this.getPlugin().getTeams().values()){
            this.scoreboard.registerNewTeam(team.getColor());
        }

        this.scoreboard.registerNewTeam("zwithout");

        for(Player p : this.plugin.getServer().getOnlinePlayers()){
            String serverName = "§6§lMizu§5§lCraft";
            p.setPlayerListHeader("§e§l》— ⚝ —《\n\n" + serverName + "\n ");
            p.setPlayerListFooter("\n§e§lСайт: §6§lmizucraft.konch\n\n§e§l》— ⚝ —《");
        }
    }

    public void addPlayer(Participant p){
        if(!p.hasTeam()) this.scoreboard.getTeam("zwithout").addEntry(p.getPlayer().getName());
        else this.scoreboard.getTeam(p.getTeam().getColor()).addEntry(p.getPlayer().getName());

    }

    public void removePlayer(Participant p){
        if(!p.hasTeam()) this.scoreboard.getTeam("zwithout").removeEntry(p.getPlayer().getName());
        else this.scoreboard.getTeam(p.getTeam().getColor()).removeEntry(p.getPlayer().getName());
    }

    public Plugin getPlugin(){ return this.plugin; }

    public void createTab(Scoreboard scoreboard) {
        Objective objective = scoreboard.registerNewObjective("tab", "dummy", "§6§lMizuvia");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);

        scoreboard.registerNewTeam("all");
    }

    public void addPlayerToTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            addPlayerToTab(tabOwner, par);
        }
    }

    private void addPlayerToTab(Participant tabOwner, Participant par){
        tabOwner.getScoreboard().getTeam("all").addEntry(par.getPlayer().getName());
    }

    public void removePlayerFromTabs(Participant par){
        for(Participant tabOwner : this.getPlugin().getPlayers().values()){
            removePlayerFromTab(tabOwner, par);
        }
    }

    private void removePlayerFromTab(Participant tabOwner, Participant par){
        tabOwner.getScoreboard().getTeam("all").removeEntry(par.getPlayer().getName());
    }
}