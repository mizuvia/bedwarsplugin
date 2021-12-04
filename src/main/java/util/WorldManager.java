package util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class WorldManager {
    public static boolean canDropResource(Location location, Material material, int maxAmount){
        Entity ent = Bukkit.getWorld("world").spawnEntity(location, EntityType.EGG);
        int itemsInArea = 0;
        for(Entity entity : ent.getNearbyEntities(3, 3, 3)){
            if(entity instanceof Item && ((Item) entity).getItemStack().getType() == material) itemsInArea++;
        }
        ent.remove();
        return itemsInArea < maxAmount;
    }

    public static boolean canDropResource(Entity ent, Material material, int maxAmount){
        int itemsInArea = 0;
        for(Entity entity : ent.getNearbyEntities(3, 3, 3)){
            if(entity instanceof Item && ((Item) entity).getItemStack().getType() == material) itemsInArea++;
        }
        return itemsInArea < maxAmount;
    }

    public static void dropItem(Location loc, ItemStack item){
        Bukkit.getServer().getWorld("world").dropItem(loc, item);
    }
}
