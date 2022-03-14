package util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

public class WorldManager {
    public static boolean canDropResource(Location location, Material material, int maxAmount){
        Arrow ent = (Arrow) Bukkit.getWorld("world").spawnEntity(new Location(location.getWorld(), location.getX(), location.getY() - 2, location.getZ()), EntityType.ARROW);

        int itemsInArea = 0;
        for(Entity entity : ent.getNearbyEntities(5, 5, 5)){
            if(entity instanceof Item && ((Item) entity).getItemStack().getType() == material) itemsInArea += ((Item) entity).getItemStack().getAmount();
        }
        ent.remove();
        return itemsInArea < maxAmount;
    }

    public static boolean canDropResource(Entity ent, Material material, int maxAmount){
        int itemsInArea = 0;
        for(Entity entity : ent.getNearbyEntities(3, 3, 3)){
            if(entity instanceof Item && ((Item) entity).getItemStack().getType() == material) itemsInArea += ((Item) entity).getItemStack().getAmount();
        }
        return itemsInArea < maxAmount;
    }

    public static void dropItem(Location loc, ItemStack item){
        Bukkit.getServer().getWorld("world").dropItem(loc, item);
    }

    public static Location getLocation(String cord){
        return new Location(Bukkit.getWorld("world"), Double.parseDouble(cord.split(" ")[0]) + 0.5, Double.parseDouble(cord.split(" ")[1]), Double.parseDouble(cord.split(" ")[2]) + 0.5, (cord.split(" ").length > 3) ? Float.parseFloat(cord.split(" ")[3]) : 0, (cord.split(" ").length > 4) ? Float.parseFloat(cord.split(" ")[4]) : 0);
    }

    public static Location centralizeLocation(Location loc){
        return new Location(loc.getWorld(), loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5, loc.getYaw(), loc.getPitch());
    }
}
