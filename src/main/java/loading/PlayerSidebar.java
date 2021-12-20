package loading;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import game.Participant;
import game.Time.Stage;
import main.Config;
import main.Plugin;
import util.Utils;

public class PlayerSidebar {
	
	private static Timer timer;
	
	private Plugin plugin;
	private UUID owner;
	private Scoreboard scoreboard;
	private Objective o;
	private int currentLinesCount; 
	
	public PlayerSidebar(Plugin plugin, UUID owner) {
		this.plugin = plugin;
		this.owner = owner;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.o = scoreboard.registerNewObjective("SideBar", "", "§6§lMizuvia");
		this.o.setDisplaySlot(DisplaySlot.SIDEBAR);
		for (int i = 1; i < 16; i++) {
			Team team = scoreboard.registerNewTeam(String.valueOf(i));
			team.addEntry(ChatColor.values()[i] + "" + ChatColor.WHITE);
		}
		for (game.Team team : plugin.getTeams().values()) {
			Team t = scoreboard.registerNewTeam(team.getName());
			t.setPrefix("§8§l[" + team.getName().substring(0, 1) + "§8§l]§r ");
		}
		this.currentLinesCount = 0;
		update();
	}
	
	public UUID getOwnerId() {
		return owner;
	}
	
	public Scoreboard getScoreboard() {
		return scoreboard;
	}
	
	public void joinTeam(String entry, game.Team team) {
		getScoreboard().getTeam(team.getName()).addEntry(entry);
	}
	
	public void leaveTeam(String entiry, game.Team team) {
		getScoreboard().getTeam(team.getName()).removeEntry(entiry);
	}
	
	public void update() {
		List<String> lines = plugin.isWorking() ? buildPlayingLines() : buildWaitingLines();
		if (currentLinesCount != lines.size()) {
			for (String s : getScoreboard().getEntries()) {
				getScoreboard().resetScores(s);
			}
			for (int i = 1; i < lines.size() + 1; i++) {
				o.getScore(ChatColor.values()[i] + "" + ChatColor.WHITE).setScore(i);
			}
			currentLinesCount = lines.size();
		}
		for (int i = 1; i < lines.size() + 1; i++) {
			setLine(i, lines.get(i - 1));
		}
	}
	
	private void setLine(int i, String line) {
		Validate.isTrue(i > 0);
		Validate.isTrue(i < 16);
		Validate.isTrue(line.length() <= 64);
		getScoreboard().getTeam(String.valueOf(i)).setPrefix(line);
	}
	
	private List<String> buildWaitingLines() {
		List<String> lines = new ArrayList<>();
		String space = " ";
		lines.add(ChatColor.GOLD + "" + ChatColor.BOLD + "   Mizuvia");
		lines.add(space.repeat(3));
		lines.add(getTimer().getTime() == -1 ? ChatColor.AQUA + "Ожидаем..." : ChatColor.AQUA + String.valueOf(getTimer().getTime()));
		lines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "Времени:");
		lines.add(space.repeat(2));
		lines.add(ChatColor.AQUA + "" + Bukkit.getOnlinePlayers().size() + "/" + Config.getMaxPlayers());
		lines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "Игроков: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
		lines.add(space.repeat(1));
		lines.add(ChatColor.AQUA + Config.getMapName());
		lines.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "Карта:");
		lines.add(space.repeat(0));
		lines.add(ChatColor.DARK_GRAY + Config.getServerName());
		return lines;
	}
	
	private List<String> buildPlayingLines() {
		List<String> lines = new ArrayList<>();
		String space = " ";
		Participant p = plugin.getPlayers().get(getOwnerId());
		lines.add(ChatColor.WHITE + "Финальных убийств: " + ChatColor.RED + String.valueOf(p.getFinalKills()));
		lines.add(ChatColor.WHITE + "Разрушено кроватей: " + ChatColor.RED + String.valueOf(p.getBrokenBeds()));
		lines.add(ChatColor.WHITE + "Убийств: " + ChatColor.RED + p.getKilledPlayers());
		lines.add(space.repeat(0));
		for (game.Team team : plugin.getTeams().values()) {
			String part1 = team.isDead() ? ChatColor.RED + "" + ChatColor.BOLD + "×" : team.isBroken() ? ChatColor.YELLOW + "" + ChatColor.BOLD + String.valueOf(team.getTeammatesAmount()) : ChatColor.GREEN + "" + ChatColor.BOLD + "✔";
			String part2 = "§7§l| §r" + team.getName().replace("§l", "") + (team.equals(p.getTeam()) ? " §7ВЫ" : "");
			lines.add(part1 + " " + part2);
		}
		lines.add(space.repeat(1));
		Stage stage = plugin.getGame().getTime().getStage();
		lines.add(ChatColor.AQUA + "" + ChatColor.BOLD + stage.getName() + ChatColor.WHITE + Utils.getTime(stage.getTime()));
		lines.add(ChatColor.DARK_GRAY + Config.getServerName());
		return lines;
	}
	
	public static void setTimer(Timer timer) {
		PlayerSidebar.timer = timer;
	}
	
	public static Timer getTimer() {
		return timer;
	}
	
	public static class Timer {
		
		private Plugin plugin;
		private int time;
		private BukkitTask bt;
		
		public Timer(Plugin plugin) {
			this.plugin = plugin;
			this.time = -1;
			this.bt = null;
		}
		
		public int getTime() {
			return time;
		}
		
		public void start() {
			if (bt == null) {
				time = 20;
				bt = new BukkitRunnable() {
					@Override
					public void run() {
						if (time-- == 0) {
				            plugin.setLoading(false);
				            plugin.getGame().start();
				            plugin.getJedis().publish(Plugin.JedisChannel, Config.getServerName() + " -1");
							this.cancel();
						}
					}
				}.runTaskTimer(plugin, 20, 20);
			}
		}
		
		public void cancel() {
			if (bt != null && !plugin.isWorking()) {
				bt.cancel();
				time = -1;
			}
		}
		
	}
	
	
}
