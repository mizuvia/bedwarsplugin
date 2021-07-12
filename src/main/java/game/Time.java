package game;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import tasks.TaskGUI;
import util.Utils;

import java.util.HashMap;

public class Time extends TaskGUI {

    private final Game game;
    private int diamondTimeLeft = 60;
    private int emeraldTimeLeft = 90;
    private int stage = 0;
    private final HashMap<Integer, String> stages = new HashMap<>();
    private final HashMap<Integer, Integer> stagesTimes = new HashMap<>();

    public Time(Game game){
        this.game = game;
        this.period = 20;
        stages.put(0, "§b§lАлмазы II");
        stages.put(1, "§a§lИзумруды II");
        stages.put(2, "§b§lАлмазы III");
        stages.put(3, "§a§lИзумруды III");
        stages.put(4, "§c§lПоломка кроватей");
        stages.put(5, "§c§lКонец игры");
        stagesTimes.put(0, 300);
        stagesTimes.put(1, 600);
        stagesTimes.put(2, 960);
        stagesTimes.put(3, 1320);
        stagesTimes.put(4, 1560);
        stagesTimes.put(5, 1800);
    }

    public int getDiamondTimeLeft() {return this.diamondTimeLeft; }

    public int getEmeraldTimeLeft() {return this.emeraldTimeLeft; }

    public void setDiamondTimeLeft(int diamondTimeLeft) { this.diamondTimeLeft = diamondTimeLeft; }

    public void setEmeraldTimeLeft(int emeraldTimeLeft) { this.emeraldTimeLeft = emeraldTimeLeft; }

    public Game getGame() {return this.game;}

    public void resetData(){
        this.diamondTimeLeft = 60;
        this.emeraldTimeLeft = 90;
        this.stage = 0;
    }

    @Override
    public void run() {
        if(!this.getGame().getPlugin().isWorking()) return;

        this.changeArmorTime();
        this.changeStage();
    }

    public void changeStage() {
        if(this.getGame().getMatchTime() == this.getStagesTimes().get(this.getStage())) {
            if(this.getStage() != 4 && this.getStage() != 5) this.getGame().getArmorStandsManager().changeStage(this.getStage());
            if(this.getStage() == 4) this.breakBeds();
            if(this.getStage() == 5) this.finishGame(1);

            this.setStage(this.getStage() + 1);
            this.getGame().getPlugin().getSidebar().changeStage(this.getStagesTimes().get(this.getStage()) - this.getStagesTimes().get(this.getStage() - 1), this.getStage());
        }
        else this.getGame().getPlugin().getSidebar().changeStageTime(this.getStagesTimes().get(this.getStage()) - this.getGame().getMatchTime(), this.getStage());

        this.getGame().increaseMatchTime(1);
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
                    Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> {

                        Utils.connectToHub(participant.getPlayer());

                    }, 200L);
                }
            }
        } else {
            for(Participant participant : this.getGame().getPlugin().getPlayers().values()) {
                participant.getPlayer().sendTitle("2§lНичья!", "§aСейчас вы будете перемещены в хаб!", 10, 150, 20);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> {

                    Utils.connectToHub(participant.getPlayer());

                }, 200);
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this.getGame().getPlugin(), () -> getGame().stop(), 250L);
    }

    public HashMap<Integer, Integer> getStagesTimes(){return this.stagesTimes;}

    public HashMap<Integer, String> getStages(){return this.stages;}

    private int getStage() { return this.stage; }

    private void breakBeds() {
        for(String name : this.getGame().getPlugin().getTeamsNames()){
            String cords_bottom = this.getGame().getPlugin().getConfig().getString("teams." + name + ".bed_bottom");
            String cords_top = this.getGame().getPlugin().getConfig().getString("teams." + name + ".bed_top");

            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).getDrops().clear();
            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).getDrops().clear();

            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_bottom)).setType(Material.AIR, false);
            Bukkit.getWorld("world").getBlockAt(Utils.getLocation(cords_top)).setType(Material.AIR, false);

        }
    }

    private void changeArmorTime() {

        for(ArmorStand arm : this.getGame().getDiamondArmorStands()){
            if(arm.getCustomName() == null) continue;
            if(arm.getCustomName().matches("(.*)появления(.*)")) arm.setCustomName("§e§lДо появления: " + Utils.getTime(this.getDiamondTimeLeft()));
        }

        for(ArmorStand arm : this.getGame().getEmeraldArmorStands()){
            if(arm.getCustomName() == null) continue;
            if(arm.getCustomName().matches("(.*)появления(.*)")) arm.setCustomName("§e§lДо появления: " + Utils.getTime(this.getEmeraldTimeLeft()));
        }

        if(this.getDiamondTimeLeft() == 1) this.setDiamondTimeLeft(this.getGame().getDiamondTimeout() + 1);
        if(this.getEmeraldTimeLeft() == 1) this.setEmeraldTimeLeft(this.getGame().getEmeraldTimeout() + 1);

        diamondTimeLeft--;
        emeraldTimeLeft--;
    }

    public void setStage(int stage) { this.stage = stage; }

}
