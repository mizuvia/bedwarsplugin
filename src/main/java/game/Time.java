package game;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import tasks.TaskGUI;
import util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Time extends TaskGUI {

    private final Game game;
    private Stage stage;
    private final List<Stage> stages = new ArrayList<>();
    private Iterator<Stage> it;

    public Time(Game game){
        this.game = game;
        this.period = 20;
        resetData();
    }

    public Game getGame() {return this.game;}

    public void resetData(){
        stages.clear();
        setStages();
        it = stages.iterator();
        stage = it.next();
    }

    @Override
    public void run() {
        if(!this.getGame().getPlugin().isWorking()) return;

        getGame().getArmorStandsManager().changeSpawners();
        changeStage();
        getGame().increaseMatchTime(1);
    }

    public void changeStage() {
        if(stage.getTime() == 0) {
            int index = stage.getIndex();
            if(index != 4 && index != 5) getGame().getArmorStandsManager().changeStage(index);
            if(index == 4) breakBeds();
            if(index == 5) finishGame(1);

            nextStage();
        }

        getGame().getPlugin().getSidebar().updateStage(stage);
        stage.decreaseTime();
    }

    public void finishGame(int reason) {
        //if(reason == 1)

        if(reason == 0){

            for(Team team : this.getGame().getPlugin().getTeams().values()){
                if(team.isDead()) continue;

                for(Participant participant : team.getTeammates().values()){
                    participant.getPlayer().sendTitle("§6§lПобеда!", "§eСейчас вы будете перемещены в хаб!", 10, 150, 20);
                    new BukkitRunnable(){
                        public int amount = 4;
                        @Override
                        public void run() {
                            if(amount == 0) this.cancel();

                            Firework firework = (Firework) Bukkit.getWorld("world").spawnEntity(participant.getPlayer().getLocation(), EntityType.FIREWORK);
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.addEffect(FireworkEffect.builder().flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(Color.ORANGE).withFade(Color.YELLOW).build());
                            meta.setPower(1);
                            firework.setFireworkMeta(meta);

                            amount--;
                        }
                    }.runTaskTimer(this.getGame().getPlugin(), 0L, 20L);
                }

                for(Participant participant : this.getGame().getPlugin().getPlayers().values()) {
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> Utils.connectToHub(participant.getPlayer()), 200L);
                }
            }
        } else {
            for(Participant participant : this.getGame().getPlugin().getPlayers().values()) {
                participant.getPlayer().sendTitle("§2§lНичья!", "§aСейчас вы будете перемещены в хаб!", 10, 150, 20);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> Utils.connectToHub(participant.getPlayer()), 200);
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> getGame().stop(), 250L);
    }

    private void breakBeds() {
        for(Team team : this.getGame().getPlugin().getTeams().values()){
            Bukkit.getWorld("world").getBlockAt(team.getBedBottomLocation()).getDrops().clear();
            Bukkit.getWorld("world").getBlockAt(team.getBedTopLocation()).getDrops().clear();

            Bukkit.getWorld("world").getBlockAt(team.getBedBottomLocation()).setType(Material.AIR, false);
            Bukkit.getWorld("world").getBlockAt(team.getBedTopLocation()).setType(Material.AIR, false);
        }
    }

    public List<Stage> getStages(){return this.stages;}

    public Stage getStage() { return this.stage; }

    public void nextStage() {
        this.stage = it.next();
    }

    public class Stage {
        private int time;
        private final String name;

        public Stage(int time, String name){
            this.time = time;
            this.name = name;
        }

        public int getTime(){return this.time;}

        public String getName(){return this.name;}

        public void decreaseTime(){
            this.time--;
        }

        public int getIndex(){ return stages.indexOf(this); }
    }

    private void setStages(){
        stages.add(new Stage(360, ArmorStandsManager.DIAMOND_COLOR + "§lАлмазы II"));
        stages.add(new Stage(360, ArmorStandsManager.EMERALD_COLOR + "§lИзумруды II"));
        stages.add(new Stage(360, ArmorStandsManager.DIAMOND_COLOR + "§lАлмазы III"));
        stages.add(new Stage(360, ArmorStandsManager.EMERALD_COLOR + "§lИзумруды III"));
        stages.add(new Stage(360, "§c§lПоломка кроватей"));
        stages.add(new Stage(600, "§c§lКонец игры"));
    }
}
