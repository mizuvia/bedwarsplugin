package loading;

import game.Participant;
import game.Team;
import main.Plugin;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import util.Utils;

public class Sidebar{

    private final Plugin plugin;
    public final Scoreboard scoreboard;
    private final Objective objective;
    public int time = -1;

    public Sidebar(Plugin plugin){
        this.plugin = plugin;
        this.scoreboard = this.plugin.getScoreboard();
        this.objective = this.scoreboard.registerNewObjective("sidebar", "dummy", "§6§lBED§5§lWARS");

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    }

    public void changePlayersAmount(int i){
        if(this.getPlugin().isLoading()){
            this.scoreboard.resetScores("§b" + (this.getPlugin().online_players - i) + "/" + this.getPlugin().players_amount);
            this.objective.getScore("§b" + (this.getPlugin().online_players) + "/" + this.getPlugin().players_amount).setScore(11);
        }
    }

    public void changeTime(int time){
        if(this.time == -1) this.scoreboard.resetScores("§bОжидаем...");
        else this.scoreboard.resetScores("§b" + this.time + " сек.");
        if(time == -1) this.objective.getScore("§bОжидаем...").setScore(8);
        else this.objective.getScore("§b" + time + " сек.").setScore(8);

        this.time = time;
    }

    public void setForLoading(){

        this.objective.getScore(" ").setScore(13);
        this.objective.getScore("§e§lИгроков:").setScore(12);
        this.objective.getScore("§b" + (this.getPlugin().online_players) + "/" + this.getPlugin().players_amount).setScore(11);
        this.objective.getScore("  ").setScore(10);
        this.objective.getScore("§e§lВремени:").setScore(9);
        this.objective.getScore("§bОжидаем...").setScore(8);
        this.objective.getScore("   ").setScore(7);
        this.objective.getScore("§e§lКарта:").setScore(6);
        this.objective.getScore("§b" + this.plugin.map_name).setScore(5);
        this.objective.getScore("    ").setScore(4);
        this.objective.getScore("§e§lСервер:").setScore(3);
        this.objective.getScore("§b" + this.plugin.server_name).setScore(2);
        this.objective.getScore("     ").setScore(1);
        this.objective.getScore("      §6§lMizu§5§lCraft").setScore(0);

    }

    public void clear(){
        for(String string : this.scoreboard.getEntries()){
            this.scoreboard.resetScores(string);
        }
    }

    public void setForWorking(){

        this.objective.getScore(" ").setScore(6 + this.getPlugin().teams_amount);
        this.objective.getScore("§b§lАлмазы II: §7" + Utils.getTime(this.getPlugin().getGame().getTime().getStagesTimes().get(0))).setScore(5 + this.getPlugin().teams_amount);
        this.objective.getScore("  ").setScore(4 + this.getPlugin().teams_amount);

        int i = 0;

        for(String team : this.getPlugin().getTeamsNames()){

            this.objective.getScore("§a✔ §7§l| §r" + this.getPlugin().getTeams().get(team).getName().replace("§l", "")).setScore(3 + (this.getPlugin().teams_amount - i));

            i++;
        }

        this.objective.getScore("   ").setScore(3);

        for(Participant participant : this.getPlugin().getPlayers().values()){

            ((CraftPlayer) participant.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Убито игроков: §c" + participant.getKilledPlayers(), 2));
            ((CraftPlayer) participant.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Разрушено кроватей: §c" + participant.getBrokenBeds(), 1));
            ((CraftPlayer) participant.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Финальных убийств: §c" + participant.getFinalKills(), 0));

        }

    }

    public void changeStageTime(int time, int stage){
        this.scoreboard.resetScores(this.getPlugin().getGame().getTime().getStages().get(stage) + ": §7" + Utils.getTime(time + 1));
        this.objective.getScore(this.getPlugin().getGame().getTime().getStages().get(stage) + ": §7" + Utils.getTime(time)).setScore(5 + this.getPlugin().teams_amount);
    }

    public void changeStage(int time, int stage){
        this.scoreboard.resetScores(this.getPlugin().getGame().getTime().getStages().get(stage - 1) + ": §7" + Utils.getTime(1));
        this.objective.getScore(this.getPlugin().getGame().getTime().getStages().get(stage) + ": §7" + Utils.getTime(time)).setScore(5 + this.getPlugin().teams_amount);
    }

    public void setBroken(Team team){
        int score = this.objective.getScore("§a✔ §7§l| §r" + team.getName().replace("§l", "")).getScore();
        this.scoreboard.resetScores("§a✔ §7§l| §r" + team.getName().replace("§l", ""));
        this.objective.getScore("§e§l" + team.getTeammatesAmount() + "§r §7§l| §r" + team.getName().replace("§l", "")).setScore(score);
    }

    public void changeKilled(Participant p){
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, "sidebar", "§7Убито игроков: §c" + (p.getKilledPlayers() - 1), 2));
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Убито игроков: §c" + p.getKilledPlayers(), 2));
    }

    public void changeBrokenBeds(Participant p){
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, "sidebar", "§7Разрушено кроватей: §c" + (p.getBrokenBeds() - 1), 1));
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Разрушено кроватей: §c" + p.getBrokenBeds(), 1));
    }

    public Plugin getPlugin() {return this.plugin; }

    public void setDead(Team team){
        int score = this.objective.getScore("§e§l0§r §7§l| §r" + team.getName().replace("§l", "")).getScore();
        this.scoreboard.resetScores("§e§l0§r §7§l| §r" + team.getName().replace("§l", ""));
        this.objective.getScore("§c§l×§r §7§l| §r" + team.getName().replace("§l", "")).setScore(score);
    }

    public void decreaseTeammatesAmount(Team team) {
        int score = this.objective.getScore("§e§l" + (team.getTeammatesAmount() + 1) + "§r §7§l| §r" + team.getName().replace("§l", "")).getScore();
        this.scoreboard.resetScores("§e§l" + (team.getTeammatesAmount() + 1) + "§r §7§l| §r" + team.getName().replace("§l", ""));
        this.objective.getScore("§e§l" + team.getTeammatesAmount() + "§r §7§l| §r" + team.getName().replace("§l", "")).setScore(score);
    }

    public void changeFinalKills(Participant p) {
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, "sidebar", "§7Финальных убийств: §c" + (p.getFinalKills() - 1), 0));
        ((CraftPlayer) p.getPlayer()).getHandle().playerConnection.sendPacket(new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, "sidebar", "§7Финальных убийств: §c" + p.getFinalKills(), 0));

    }
}
