package tasks;

import game.ArmorStands;
import game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import util.Utils;
import util.WorldManager;

public class DiamondSpawner {

    private final Game game;
    private int diamondTimeout = 28;
    private int diamondTimeLeft = 22;
    private static final int MAX_AMOUNT_OF_DIAMONDS = 6;

    private void setDiamondTimeLeft(int diamondTimeLeft) { this.diamondTimeLeft = diamondTimeLeft; }

    private int getDiamondTimeLeft() {return this.diamondTimeLeft; }

    public int getDiamondTimeout(){ return this.diamondTimeout; }

    public void setDiamondTimeout(int diamondTimeout){ this.diamondTimeout = diamondTimeout; }

    public DiamondSpawner(Game game){ this.game = game; }

    public void updateSpawner(){
        if(this.getDiamondTimeLeft() == 0) this.setDiamondTimeLeft(this.getDiamondTimeout());
        changeArmorTime();
        spawnItem();
        this.diamondTimeLeft--;
    }

    public void changeArmorTime() {
        for(ArmorStands arm : this.getGame().getArmorStandsManager().getDiamondArmorStands()){
            arm.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(this.getDiamondTimeLeft()));
        }
    }

    public void spawnItem(){
        if(this.getDiamondTimeLeft() == 1) {
            for(ArmorStands armorStands : this.getGame().getArmorStandsManager().getDiamondArmorStands()){
                if(!WorldManager.canDropResource(armorStands.getStage(), Material.DIAMOND, MAX_AMOUNT_OF_DIAMONDS)) return;
                ItemStack diamond = Utils.createItem(Material.DIAMOND, 1, "§eАлмаз");
                Bukkit.getServer().getWorld("world").dropItem(armorStands.getStage().getLocation(), diamond);
            }
        }
    }

    public Game getGame() {return this.game;}
}
