package tasks;

import game.ArmorStands;
import game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import util.Utils;
import util.WorldManager;

import java.util.List;
import java.util.logging.Logger;

public class DiamondSpawner {

    private final Game game;
    private int diamondTimeout = DIAMONDS_PHASES.get(0);
    private int diamondTimeLeft = DIAMONDS_PHASES.get(0);
    private static final int MAX_AMOUNT_OF_DIAMONDS = 6;
    public static final List<Integer> DIAMONDS_PHASES = List.of(28, 22, 12);

    private void setDiamondTimeLeft(int diamondTimeLeft) { this.diamondTimeLeft = diamondTimeLeft; }

    private int getDiamondTimeLeft() {return this.diamondTimeLeft; }

    public int getDiamondTimeout(){ return this.diamondTimeout; }

    public void setDiamondTimeout(int diamondTimeout){ this.diamondTimeout = diamondTimeout; }

    public DiamondSpawner(Game game){ this.game = game; }

    public void updateSpawner(){
        spawnItem();
        if(this.getDiamondTimeLeft() == 0) this.setDiamondTimeLeft(this.getDiamondTimeout());
        changeArmorTime();
        this.diamondTimeLeft--;
    }

    public void changeArmorTime() {
        for(ArmorStands arm : this.getGame().getArmorStandsManager().getDiamondArmorStands()){
            arm.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(this.getDiamondTimeLeft()));
        }
    }

    public void spawnItem(){
        if(this.getDiamondTimeLeft() == 0) {
            for(ArmorStands armorStands : this.getGame().getArmorStandsManager().getDiamondArmorStands()){
                if(!WorldManager.canDropResource(armorStands.getStage(), Material.DIAMOND, MAX_AMOUNT_OF_DIAMONDS)) return;
                ItemStack diamond = Utils.createItem(Material.DIAMOND, 1, "§eАлмаз");
                Item item = WorldManager.dropItem(armorStands.getTime().getLocation(), diamond);
                item.setVelocity(new Vector());
            }
        }
    }

    public Game getGame() {return this.game;}
}
