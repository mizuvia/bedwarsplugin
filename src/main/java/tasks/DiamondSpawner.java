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

public class DiamondSpawner {

    private final Game game;
    private int diamondTimeout = 35;
    private int diamondTimeLeft = 35;
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
                int diamondInTheArea = 0;
                for(Entity entity : armorStands.getStage().getNearbyEntities(3, 3, 3)){
                    if(entity instanceof Item && ((Item) entity).getItemStack().getType() == Material.DIAMOND) diamondInTheArea++;
                }
                if(diamondInTheArea >= MAX_AMOUNT_OF_DIAMONDS) return;

                ItemStack diamond = createDiamond();
                Bukkit.getServer().getWorld("world").dropItem(armorStands.getStage().getLocation(), diamond);
            }
        }
    }

    private ItemStack createDiamond() {
        ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = diamond.getItemMeta();
        meta.setDisplayName("§eАлмаз");
        diamond.setItemMeta(meta);
        return diamond;
    }

    public Game getGame() {return this.game;}
}
