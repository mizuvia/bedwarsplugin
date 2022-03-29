package game;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import tasks.TaskGUI;
import com.hoshion.library.MizuviaLibrary.MineColor;
import util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Time extends TaskGUI {

    private final Game game;
    private Stage stage;
    private final List<Stage> stages = new ArrayList<>();
    private Iterator<Stage> it;
    private boolean isFinished = false;

    public Time(Game game){
        this.game = game;
        this.plugin = game.getPlugin();
        this.period = 20;
        resetData();
    }

    public Game getGame() {return this.game;}

    public void resetData(){
        stages.clear();
        setStages();
        it = stages.iterator();
        stage = it.next();
        isFinished = false;
    }

    @Override
    public void execute() {
        if (plugin.getGame() != game) {
            this.cancel();
        }
        if(!plugin.isWorking() || isFinished) return;

        getGame().getArmorStandsManager().changeSpawners();
        changeStage();
        getGame().increaseMatchTime(1);
        plugin.getSidebar().updateMatchTime();
    }

    public void changeStage() {
        if(stage.getTime() == 0) {
            int index = stage.getIndex();
            if(index != 4 && index != 5) getGame().getArmorStandsManager().changeStage(index);
            if(index == 4) breakBeds();
            if(index == 5) {
                finishGame(FinishReason.DRAW);
                return;
            }

            nextStage();
        }

        getGame().getPlugin().getSidebar().updateStage(stage);
        stage.decreaseTime();
    }

    public void finishGame(FinishReason reason) {

        if (isFinished) return;
        isFinished = true;

        if(reason == FinishReason.WIN){
            plugin.getSidebar().updateStage(MineColor.ORANGE.BOLD() + "Победа!");
            Team winningTeam = plugin.getTeams().stream().filter(team -> !team.isDead()).findFirst().orElse(null);

            plugin.getPlayers().forEach(p -> {
                if (p.getTeam() != winningTeam) {
                    String mainTitle = MineColor.DARK_RED.BOLD() + "Поражение!";
                    String secondTitle = "Победила команда " + winningTeam.getName();
                    p.getPlayer().sendTitle(mainTitle, secondTitle, 10, 150, 20);
                }
            });

            for(Participant participant : winningTeam.getTeammates()){
                participant.getPlayer().sendTitle("§6§lПобеда!", "§eСейчас вы будете перемещены в хаб!", 10, 150, 20);
                new BukkitRunnable(){
                    public int amount = 8;
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
                }.runTaskTimer(plugin, 0L, 20L);
            }

            for(Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Utils.connectToHub(player), 200L);
            }

        } else {
            plugin.getSidebar(). updateStage(MineColor.GREEN.BOLD() + "Ничья!");
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle("§2§lНичья!", "§aСейчас вы будете перемещены в хаб!", 10, 150, 20);

                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> Utils.connectToHub(player), 200);
            }
        }

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.reloadGame(), 400);
    }

    private void breakBeds() {
        for (Team team : plugin.getTeams()){
            Bukkit.getWorld("world").getBlockAt(team.getBedBottomLocation()).getDrops().clear();
            Bukkit.getWorld("world").getBlockAt(team.getBedTopLocation()).getDrops().clear();

            Bukkit.getWorld("world").getBlockAt(team.getBedBottomLocation()).setType(Material.AIR, false);
            Bukkit.getWorld("world").getBlockAt(team.getBedTopLocation()).setType(Material.AIR, false);

            team.setBroken(true);
        }

        for (Participant p : plugin.getPlayers()){
            p.getPlayer().sendTitle(MineColor.DARK_RED.BOLD() + "Ваша кровать разрушена", MineColor.RED + "Вы больше не возродитесь", 10, 20, 20);
        }
    }

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
        stages.add(new Stage(360, "§c§lКонец игры"));
    }

    public enum FinishReason {
        DRAW,
        WIN
    }
}
