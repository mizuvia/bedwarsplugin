package loading;

import org.bukkit.Bukkit;

import main.Config;
import main.Plugin;

public class Waiting{

    private final Plugin plugin;

    public Waiting(Plugin plugin){
        this.plugin = plugin;
        this.getPlugin().setLoading(true);
    }

    public void checkAmount(){
    	if (Bukkit.getOnlinePlayers().size() > 1) {
    		PlayerSidebar.getTimer().start();
    	}else {
    		PlayerSidebar.getTimer().cancel();
    	}
//        int amount = this.getPlugin().getOnlinePlayers();
//
//        if (amount > 1) this.getChange().setTime(30);
//        else this.getChange().setTime(-1);
//        if (amount == Config.getMaxPlayers()) this.getChange().setTime(10);
    }

    public Plugin getPlugin() {return this.plugin; }

}
