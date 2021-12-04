package game;

import main.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tasks.DiamondSpawner;
import tasks.EmeraldSpawner;
import tasks.TaskGUI;
import util.Colors;
import util.Utils;
import util.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class ArmorStandsManager extends TaskGUI{

    private final Game game;
    private float angle = 0;
    private int direction = 1;
    private List<ArmorStands> diamondArmorStands = new ArrayList<>();
    private List<ArmorStands> emeraldArmorStands = new ArrayList<>();
    private final DiamondSpawner diamondSpawner;
    private final EmeraldSpawner emeraldSpawner;
    public static final String DIAMOND_COLOR = Colors.fromHex("4beddb");
    public static final String EMERALD_COLOR = Colors.fromHex("41f385");

    public EmeraldSpawner getEmeraldSpawner() { return emeraldSpawner; }

    public DiamondSpawner getDiamondSpawner() { return diamondSpawner; }

    public List<ArmorStands> getDiamondArmorStands(){ return this.diamondArmorStands; }

    public List<ArmorStands> getEmeraldArmorStands(){ return this.emeraldArmorStands; }

    public ArmorStandsManager(Game game){
        this.game = game;
        this.period = 1;
        this.diamondSpawner = new DiamondSpawner(game);
        this.emeraldSpawner = new EmeraldSpawner(game);
    }

    public void resetData(){
        this.angle = 0;
        this.direction = 1;
        this.getDiamondSpawner().resetData();
        this.getEmeraldSpawner().resetData();
        diamondArmorStands = new ArrayList<>();
        emeraldArmorStands = new ArrayList<>();
    }

    public void createArmorStands() {
        for(String cord : Config.getDiamonds()){
            Location loc = WorldManager.getLocation(cord);
            ArmorStands armorStands = new ArmorStands(loc);

            armorStands.getStage().setCustomName(DIAMOND_COLOR + "§lАлмазы I");
            armorStands.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(DiamondSpawner.DIAMONDS_PHASES.get(0)));
            armorStands.getBlock().getEquipment().setHelmet(new ItemStack(Material.DIAMOND_BLOCK, 1));

            this.getDiamondArmorStands().add(armorStands);
        }

        for(String cord : Config.getEmeralds()){
            Location loc = WorldManager.getLocation(cord);
            ArmorStands armorStands = new ArmorStands(loc);

            armorStands.getStage().setCustomName(EMERALD_COLOR + "§lИзумруды I");
            armorStands.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(EmeraldSpawner.EMERALD_PHASES.get(0)));
            armorStands.getBlock().getEquipment().setHelmet(new ItemStack(Material.EMERALD_BLOCK, 1));

            this.getEmeraldArmorStands().add(armorStands);
        }
    }

    @Override
    public void run() {

        if(!this.getGame().getPlugin().isWorking()) return;

        for(ArmorStands arm : this.getEmeraldArmorStands()){
            for(ArmorStand armor : arm.getList())
                setArmorStandPosition(armor);
        }
        for(ArmorStands arm : this.getDiamondArmorStands()){
            for(ArmorStand armor : arm.getList())
                setArmorStandPosition(armor);
        }

        angle += 6;
        if(angle == 360) {
            angle = 0;
            direction = (direction == 0) ? 1 : 0;
        }
    }

    public void changeSpawners() {
        this.getDiamondSpawner().updateSpawner();
        this.getEmeraldSpawner().updateSpawner();
    }

    private void setArmorStandPosition(ArmorStand arm) {
        if(arm.getEquipment().getHelmet().getType().equals(Material.AIR)) return;
        arm.setRotation(angle, 0);

        if(direction == 1) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() - 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
        if(direction == 0) arm.teleport(new Location(arm.getWorld(), arm.getLocation().getX(), arm.getLocation().getY() + 0.01, arm.getLocation().getZ(), arm.getLocation().getYaw(), arm.getLocation().getPitch()));
    }

    public Game getGame() {return this.game;}

    public void changeStage(int stage) {
        int phase = stage / 2;
        String roman = Utils.getRomanNumeral(phase + 2);
        if(stage % 2 == 0){
            this.getDiamondSpawner().setDiamondTimeout(DiamondSpawner.DIAMONDS_PHASES.get(phase + 1));
            for(ArmorStands arm : this.getDiamondArmorStands())
                arm.getStage().setCustomName(DIAMOND_COLOR + "§lАлмазы " + roman);
        } else {
            this.getEmeraldSpawner().setEmeraldTimeout(EmeraldSpawner.EMERALD_PHASES.get(phase + 1));
            for(ArmorStands arm : this.getEmeraldArmorStands())
                arm.getStage().setCustomName(EMERALD_COLOR + "§lИзумруды " + roman);
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage("§7Генератор " + (stage % 2 == 0 ? DIAMOND_COLOR + "§lАлмазов" : EMERALD_COLOR + "§lИзумрудов") + " §r§7улучшен до " + (stage % 2 == 0 ? DIAMOND_COLOR : EMERALD_COLOR) + "§l" + roman + " §r§7уровня");

        }
    }
}
