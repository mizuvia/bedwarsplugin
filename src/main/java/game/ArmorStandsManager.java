package game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import tasks.TaskGUI;

public class ArmorStandsManager extends TaskGUI{

    private final Game game;
    private float angle = 0;
    private int direction = 1;

    public ArmorStandsManager(Game game){
        this.game = game;
        this.period = 1;
    }

    public void resetData(){
        this.angle = 0;
        this.direction = 1;
    }

    @Override
    public void run() {

        if(!this.getGame().getPlugin().isWorking()) return;

        for(ArmorStand arm : this.getGame().getEmeraldArmorStands()){
            if(arm.getHelmet().getType().equals(Material.AIR)) continue;
            arm.setRotation(angle, 0);

            if(direction == 1) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() - 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
            if(direction == 0) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() + 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
        }
        for(ArmorStand arm : this.getGame().getDiamondArmorStands()){
            if(arm.getHelmet().getType().equals(Material.AIR)) continue;
            arm.setRotation(angle, 0);

            if(direction == 1) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() - 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
            if(direction == 0) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() + 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
        }
        angle += 6;
        if(angle == 360) {
            angle = 0;
            direction = (direction == 0) ? 1 : 0;
        }
    }

    public Game getGame() {return this.game;}

    public void changeStage(int stage) {
        switch (stage) {
            case 0:
                this.getGame().setDiamondTimeout(30);
                for (ArmorStand arm : this.getGame().getDiamondArmorStands()) {
                    if (arm.getCustomName() == null) continue;
                    if (arm.getCustomName().matches("(.*)Алмазы(.*)")) arm.setCustomName("§b§lАлмазы II");
                }

                for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§7Генератор §b§lАлмазов §r§7улучшен до §b§lII §r§7уровня");
                break;
            case 1:
                this.getGame().setEmeraldTimeout(45);
                for (ArmorStand arm : this.getGame().getEmeraldArmorStands()) {
                    if (arm.getCustomName() == null) continue;
                    if (arm.getCustomName().matches("(.*)Изумруды(.*)")) arm.setCustomName("§a§lИзумруды II");
                }

                for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§7Генератор §a§lИзумрудов §r§7улучшен до §a§lII §r§7уровня");
                break;
            case 2:
                this.getGame().setDiamondTimeout(15);
                for (ArmorStand arm : this.getGame().getDiamondArmorStands()) {
                    if (arm.getCustomName() == null) continue;
                    if (arm.getCustomName().matches("(.*)Алмазы(.*)")) arm.setCustomName("§a§lАлмазы III");
                }

                for (Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§7Генератор §b§lАлмазов §r§7улучшен до §b§lIII §r§7уровня");
                break;
            case 3:
                this.getGame().setEmeraldTimeout(30);
                for(ArmorStand arm : this.getGame().getEmeraldArmorStands()){
                    if(arm.getCustomName() == null) continue;
                    if(arm.getCustomName().matches("(.*)Изумруды(.*)")) arm.setCustomName("§a§lИзумруды III");
                }

                for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§7Генератор §a§lИзумрудов §r§7улучшен до §a§lIII §r§7уровня");
                break;
        }
    }
}
