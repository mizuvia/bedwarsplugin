package util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;

public class WorldManager {
    public static boolean canDropResource(Location location, Material material, int maxAmount){
        Entity ent = Bukkit.getWorld("world").spawnEntity(location, EntityType.UNKNOWN);
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
}
