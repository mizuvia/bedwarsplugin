package events;

import main.Plugin;

public class SimpleListener {
    public Plugin plugin;

    public SimpleListener(Plugin plugin){
        this.plugin = plugin;
    }

    public Plugin getPlugin(){return this.plugin;}
}
