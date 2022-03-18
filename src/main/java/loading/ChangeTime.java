package loading;

import main.Config;
import main.Plugin;
import tasks.TaskGUI;

import java.util.logging.Logger;

public class ChangeTime extends TaskGUI {

    public int time = -1;

    public ChangeTime(){
        this.period = 20;
    }

    @Override
    public void execute() {
        if(!this.getPlugin().isLoading()) return;

        if(this.getTime() == 0) {
            this.getPlugin().setLoading(false);
            this.getPlugin().getSidebar().changeTime(-1);
            this.getPlugin().getGame().start();

            this.getPlugin().getJedis().publish(Plugin.JedisChannel, Config.getServerName() + " -1");
        }
        else this.getPlugin().getSidebar().changeTime(this.getTime());
        if(this.getTime() != -1) this.decreaseTime();
    }

    public void setTime(int time){ this.time = time; }

    public void decreaseTime(){ this.time--; }

    public int getTime(){ return this.time; }
}
