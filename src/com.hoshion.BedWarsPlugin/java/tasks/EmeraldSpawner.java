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

public class EmeraldSpawner {

    private final Game game;
    private int emeraldTimeout = 65;
    private int emeraldTimeLeft = 65;
    private static final int MAX_AMOUNT_OF_EMERALDS = 3;

    public void setEmeraldTimeLeft(int emeraldTimeLeft) { this.emeraldTimeLeft = emeraldTimeLeft; }

    public int getEmeraldTimeLeft() {return this.emeraldTimeLeft; }

    public int getEmeraldTimeout(){ return this.emeraldTimeout; }

    public void setEmeraldTimeout(int emeraldTimeout){ this.emeraldTimeout = emeraldTimeout; }

    public EmeraldSpawner(Game game){ this.game = game; }

    public void updateSpawner() {
        if(this.getEmeraldTimeLeft() == 0) this.setEmeraldTimeLeft(this.getEmeraldTimeout());
        changeArmorTime();
        spawnItem();
        this.emeraldTimeLeft--;
    }

    public void changeArmorTime() {
        for(ArmorStands arm : this.getGame().getArmorStandsManager().getEmeraldArmorStands()){
            arm.getTime().setCustomName("§e§lДо появления: " + Utils.getTime(this.getEmeraldTimeLeft()));
        }
    }

    public void spawnItem(){
        if(this.getEmeraldTimeLeft() == 1) {
            for(ArmorStands armorStands : this.getGame().getArmorStandsManager().getEmeraldArmorStands()){
                int emeraldInTheArea = 0;
                for(Entity entity : armorStands.getStage().getNearbyEntities(3, 3, 3)){
                    if(entity instanceof Item && ((Item) entity).getItemStack().getType() == Material.EMERALD) emeraldInTheArea++;
                }
                if(emeraldInTheArea >= MAX_AMOUNT_OF_EMERALDS) return;

                ItemStack emerald = createEmerald();
                Bukkit.getServer().getWorld("world").dropItem(armorStands.getStage().getLocation(), emerald);
            }
        }
    }

    private ItemStack createEmerald() {
        ItemStack emerald = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = emerald.getItemMeta();
        meta.setDisplayName("§eИзумруд");
        emerald.setItemMeta(meta);
        return emerald;
    }

    public Game getGame() { return this.game; }

}
