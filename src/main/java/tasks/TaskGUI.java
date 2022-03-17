package tasks;

import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class TaskGUI extends BukkitRunnable {

    protected Plugin plugin;
    public long period;
    private BukkitTask task;

    public void startTask(Plugin plugin){
        this.plugin = plugin;
        task = this.runTaskTimer(Bukkit.getPluginManager().getPlugin(Plugin.PluginName), 0L, this.period);
    }

    @Override
    public void run() {
        if (!plugin.isEnabled()) {
            task.cancel();
            return;
        }
        execute();
    }

    protected abstract void execute();

    public void cancel() {
        task.cancel();
    }

    public Plugin getPlugin(){ return this.plugin; }
}
