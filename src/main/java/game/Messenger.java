package game;

import java.util.ArrayDeque;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import main.Plugin;

public class Messenger {
	
	private ArrayDeque<Message> messages;
	private BukkitTask worker;
	
	public Messenger(Plugin plugin, int delayTicks) {
		this.messages = new ArrayDeque<>();
		this.worker = new BukkitRunnable() {
			@Override
			public void run() {
				Message m = messages.poll();
				if (m == null) {
					return;
				}
				messages.addLast(m);
				Bukkit.getOnlinePlayers().forEach(p -> {
					p.sendMessage(m.getLines());
				});
			}
		}.runTaskTimer(plugin, 10, delayTicks);
	}
	
	public void addMessage(Message message) {
		messages.addLast(message);
	}
	
	public void stop() {
		if (worker != null) {
			worker.cancel();
		}
	}
	
	public static class Message {
		
		private String[] lines;
		
		public Message(String[] lines) {
			this.lines = lines;
		}
		
		public String[] getLines() {
			return lines;
		}
		
	}
	
}
