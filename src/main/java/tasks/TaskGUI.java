package tasks;

import main.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class TaskGUI extends BukkitRunnable {

    protected Plugin plugin;
    public long period;

    public void startTask(){
        this.runTaskTimer(Bukkit.getPluginManager().getPlugin("BedWarsPlugin"), 0L, this.period);
    }

    public Plugin getPlugin(){ return this.plugin; }
}
