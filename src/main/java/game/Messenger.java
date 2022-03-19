package game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.Plugin;

public class Messenger {
	
	private final List<String> messages = new ArrayList<>();
	private final BukkitTask worker;
	
	public Messenger(Plugin plugin, int delayTicks) {
		this.worker = new BukkitRunnable() {
			@Override
			public void run() {
				if (messages.size() == 0) return;
				Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(messages.toArray(new String[0])));
			}
		}.runTaskTimer(plugin, 10, delayTicks);
	}
	
	public void addMessage(String message) {
		messages.add(message);
	}
	
	public void stop() {
		if (worker != null) {
			worker.cancel();
		}
	}
}
