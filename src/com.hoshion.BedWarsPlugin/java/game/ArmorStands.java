package game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmorStands {
    private final ArmorStand stage;
    private final ArmorStand time;
    private final ArmorStand block;
    private final List<ArmorStand> list = new ArrayList<>();

    public ArmorStands(Location loc){
        Location loc1 = loc.clone();
        loc1.setY(loc.getY() + 1.2);
        Location loc2 = loc.clone();
        loc2.setY(loc.getY() + 0.9);
        Location loc3 = loc.clone();
        loc3.setY(loc.getY() + 0.8);

        stage = createArmorStand(loc1);
        time = createArmorStand(loc2);
        block = createArmorStand(loc3);

        stage.setCustomNameVisible(true);
        time.setCustomNameVisible(true);

        list.add(stage);
        list.add(time);
        list.add(block);
    }

    public ArmorStand getStage() {
        return stage;
    }

    public ArmorStand getBlock() {
        return block;
    }

    public ArmorStand getTime() {
        return time;
    }

    public List<ArmorStand> getList() {
        return list;
    }

    private ArmorStand createArmorStand(Location loc){
        ArmorStand armorStand = (ArmorStand) Bukkit.getServer().getWorld("world").spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setAI(false);
        armorStand.setVisible(false);
        armorStand.setRemoveWhenFarAway(false);
        armorStand.setGravity(false);
        return armorStand;
    }
}
