package tasks;

import game.ArmorStands;
import game.Game;
import main.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import util.Utils;
import util.WorldManager;

import java.util.ArrayList;
import java.util.List;

public class EmeraldSpawner {

    private final Game game;
    private int emeraldTimeout;
    private int emeraldTimeLeft;
    private static final int MAX_AMOUNT_OF_EMERALDS = 3;
    public static final List<Integer> EMERALD_PHASES = new ArrayList<>();

    public void setEmeraldTimeLeft(int emeraldTimeLeft) { this.emeraldTimeLeft = emeraldTimeLeft; }

    public int getEmeraldTimeLeft() {return this.emeraldTimeLeft; }

    public int getEmeraldTimeout(){ return this.emeraldTimeout; }

    public void setEmeraldTimeout(int emeraldTimeout){ this.emeraldTimeout = emeraldTimeout; }

    public EmeraldSpawner(Game game){
        this.game = game;
        if(Config.getPlayersPerTeam() == 1 || Config.getPlayersPerTeam() == 2)
            EMERALD_PHASES.addAll(List.of(65, 45, 30));
        if(Config.getPlayersPerTeam() == 3 || Config.getPlayersPerTeam() == 4 || Config.getPlayersPerTeam() == 8)
            EMERALD_PHASES.addAll(List.of(56, 40, 28));
        this.emeraldTimeLeft = EMERALD_PHASES.get(0);
        this.emeraldTimeout = EMERALD_PHASES.get(0);
    }

    public void updateSpawner() {
        spawnItem();
        if(this.getEmeraldTimeLeft() == 0) this.setEmeraldTimeLeft(this.getEmeraldTimeout());
        changeArmorTime();
        this.emeraldTimeLeft--;
    }

    public void changeArmorTime() {
        for(ArmorStands arm : this.getGame().getArmorStandsManager().getEmeraldArmorStands()){
            arm.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(this.getEmeraldTimeLeft()));
        }
    }

    public void spawnItem(){
        if(this.getEmeraldTimeLeft() == 0) {
            for(ArmorStands armorStands : this.getGame().getArmorStandsManager().getEmeraldArmorStands()){
                if(!WorldManager.canDropResource(armorStands.getStage(), Material.EMERALD, MAX_AMOUNT_OF_EMERALDS)) return;
                ItemStack emerald = Utils.createItem(Material.EMERALD, 1, "§eИзумруд");
                Item item = WorldManager.dropItem(armorStands.getTime().getLocation(), emerald);
                item.setVelocity(new Vector());
            }
        }
    }

    public Game getGame() { return this.game; }
}
