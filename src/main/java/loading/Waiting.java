package loading;

import main.Plugin;

public class Waiting{

    private final Plugin plugin;
    private final ChangeTime change;

    public Waiting(Plugin plugin){
        this.plugin = plugin;
        this.change = new ChangeTime(this.getPlugin());
        this.change.startTask();
        this.getPlugin().setLoading(true);
        this.getPlugin().getSidebar().setForLoading();
    }

    public void checkAmount(){
        int amount = this.getPlugin().getOnlinePlayers();

        if (amount > 1) this.getChange().setTime(30);
        else this.getChange().setTime(-1);
        if (amount == this.getPlugin().getMaxPlayers()) this.getChange().setTime(10);
    }

    public Plugin getPlugin() {return this.plugin; }

    public ChangeTime getChange(){ return this.change; }
}
